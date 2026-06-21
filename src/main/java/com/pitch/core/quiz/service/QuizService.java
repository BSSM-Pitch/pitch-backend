package com.pitch.core.quiz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitch.core.curriculum.entity.Curriculum;
import com.pitch.core.curriculum.repository.CurriculumRepository;
import com.pitch.core.global.exception.BusinessException;
import com.pitch.core.global.exception.ErrorCode;
import com.pitch.core.quiz.dto.*;
import com.pitch.core.quiz.entity.Quiz;
import com.pitch.core.quiz.entity.QuizJob;
import com.pitch.core.quiz.entity.QuizQuestion;
import com.pitch.core.quiz.repository.QuizJobRepository;
import com.pitch.core.quiz.repository.QuizRepository;
import com.pitch.core.user.entity.User;
import com.pitch.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizService {

    private final CurriculumRepository curriculumRepository;
    private final QuizJobRepository quizJobRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public QuizJobResponse generateQuiz(Long curriculumId, QuizGenerateRequest request, String email) {
        validateTypes(request.getTypes());
        Curriculum curriculum = getCurriculum(curriculumId, email);
        QuizJob job = QuizJob.create(curriculum, 8);
        quizJobRepository.save(job);
        generateQuizAsync(job.getId(), curriculum.getId(), request.getCount(), request.getTypes());
        return new QuizJobResponse(job);
    }

    public QuizJobResponse getJobStatus(Long curriculumId, String jobId, String email) {
        Curriculum curriculum = getCurriculum(curriculumId, email);
        QuizJob job = quizJobRepository.findByIdAndCurriculum(jobId, curriculum)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_JOB_NOT_FOUND));
        return new QuizJobResponse(job);
    }

    public QuizListResponse getQuizList(Long curriculumId, String email) {
        Curriculum curriculum = getCurriculum(curriculumId, email);
        return new QuizListResponse(quizRepository.findByCurriculumOrderByCreatedAtDesc(curriculum));
    }

    public QuizDetailResponse getQuiz(Long curriculumId, Long quizId, String email) {
        Curriculum curriculum = getCurriculum(curriculumId, email);
        Quiz quiz = quizRepository.findByIdAndCurriculum(quizId, curriculum)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_NOT_FOUND));
        return new QuizDetailResponse(quiz);
    }

    @Transactional
    public QuizSubmitResponse submitQuiz(Long curriculumId, Long quizId, QuizSubmitRequest request, String email) {
        Curriculum curriculum = getCurriculum(curriculumId, email);
        Quiz quiz = quizRepository.findByIdAndCurriculum(quizId, curriculum)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_NOT_FOUND));

        Map<Long, String> answerMap = request.getAnswers().stream()
                .collect(Collectors.toMap(QuizAnswerRequest::getQuestionId, QuizAnswerRequest::getAnswer));

        List<QuizResultItemResponse> results = new ArrayList<>();
        int correctCount = 0;
        for (QuizQuestion q : quiz.getQuestions()) {
            String userAnswer = answerMap.getOrDefault(q.getId(), "");
            boolean correct = q.getAnswer().equalsIgnoreCase(userAnswer.trim());
            if (correct) correctCount++;
            results.add(new QuizResultItemResponse(q.getId(), correct, q.getAnswer(), q.getExplanation()));
        }
        quiz.submit(correctCount);
        return new QuizSubmitResponse(correctCount, quiz.getQuestions().size(), results);
    }

    @Async
    @Transactional
    public void generateQuizAsync(String jobId, Long curriculumId, int count, List<String> types) {
        try {
            QuizJob job = quizJobRepository.findById(jobId).orElseThrow();
            Curriculum curriculum = curriculumRepository.findById(curriculumId).orElseThrow();
            Quiz quiz = Quiz.create(curriculum);
            for (int i = 0; i < count; i++) {
                String type = types.get(i % types.size());
                String options = null;
                if ("multiple_choice".equals(type)) {
                    options = objectMapper.writeValueAsString(List.of("선택지 A", "선택지 B", "선택지 C", "선택지 D"));
                }
                quiz.addQuestion(QuizQuestion.create(
                        type, "문제 " + (i + 1) + ": 샘플 퀴즈 문제입니다.",
                        options, "선택지 A", "정답 해설입니다.", i));
            }
            quizRepository.save(quiz);
            job.markCompleted();
        } catch (Exception e) {
            quizJobRepository.findById(jobId).ifPresent(QuizJob::markFailed);
        }
    }

    private void validateTypes(List<String> types) {
        List<String> valid = List.of("multiple_choice", "short_answer");
        types.forEach(t -> {
            if (!valid.contains(t)) throw new BusinessException(ErrorCode.INVALID_TYPE);
        });
    }

    private Curriculum getCurriculum(Long curriculumId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return curriculumRepository.findByIdAndUser(curriculumId, user)
                .orElseThrow(() -> new BusinessException(ErrorCode.CURRICULUM_NOT_FOUND));
    }
}
