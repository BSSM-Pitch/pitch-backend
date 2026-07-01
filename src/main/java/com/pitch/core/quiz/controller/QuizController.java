package com.pitch.core.quiz.controller;

import com.pitch.core.quiz.dto.*;
import com.pitch.core.quiz.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/curricula/{curriculumId}")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/quiz/generate")
    public ResponseEntity<QuizJobResponse> generateQuiz(
            @PathVariable UUID curriculumId,
            @RequestBody @Valid QuizGenerateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(quizService.generateQuiz(curriculumId, request, userDetails.getUsername()));
    }

    @GetMapping("/quiz/jobs/{jobId}/status")
    public ResponseEntity<QuizJobResponse> getJobStatus(
            @PathVariable UUID curriculumId,
            @PathVariable String jobId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(quizService.getJobStatus(curriculumId, jobId, userDetails.getUsername()));
    }

    @GetMapping("/quizzes")
    public ResponseEntity<QuizListResponse> getQuizList(
            @PathVariable UUID curriculumId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(quizService.getQuizList(curriculumId, userDetails.getUsername()));
    }

    @GetMapping("/quizzes/{quizId}")
    public ResponseEntity<QuizDetailResponse> getQuiz(
            @PathVariable UUID curriculumId,
            @PathVariable UUID quizId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(quizService.getQuiz(curriculumId, quizId, userDetails.getUsername()));
    }

    @PostMapping("/quizzes/{quizId}/submit")
    public ResponseEntity<QuizSubmitResponse> submitQuiz(
            @PathVariable UUID curriculumId,
            @PathVariable UUID quizId,
            @RequestBody QuizSubmitRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(quizService.submitQuiz(
                curriculumId, quizId, request, userDetails.getUsername()));
    }
}
