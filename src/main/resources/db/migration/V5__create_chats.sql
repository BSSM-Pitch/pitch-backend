-- 파일명 규칙: V{버전}__{설명}.sql
-- 적용된 마이그레이션 파일은 절대 수정하지 말 것. 스키마 변경은 새 버전 파일(V2, V3, ...)로 추가한다.

CREATE TABLE chats (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID NOT NULL REFERENCES users (id),
    title      VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_chats_user_id ON chats (user_id);
CREATE INDEX idx_chats_user_id_updated_at ON chats (user_id, updated_at DESC);

CREATE TABLE messages (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    chat_id    UUID NOT NULL REFERENCES chats (id),
    role       VARCHAR(50) NOT NULL,
    content    TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_messages_chat_id ON messages (chat_id);

CREATE TABLE curriculums (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    chat_id    UUID NOT NULL UNIQUE REFERENCES chats (id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 테스트용 시드: 채팅 3개 + 메시지 + 커리큘럼 (updated_at 차이로 정렬·페이지네이션 검증 가능)
DO $$
DECLARE
    v_user_id  UUID := 'b2dbc936-0236-4924-9f4e-0e86965470dc';
    v_chat1_id UUID := gen_random_uuid();
    v_chat2_id UUID := gen_random_uuid();
    v_chat3_id UUID := gen_random_uuid();
BEGIN
    IF NOT EXISTS (SELECT 1 FROM users WHERE id = v_user_id) THEN
        RETURN;
    END IF;

    -- 채팅 3개 (updated_at 내림차순으로 chat1 > chat2 > chat3 순 정렬 예상)
    INSERT INTO chats (id, user_id, title, created_at, updated_at) VALUES
        (v_chat1_id, v_user_id, 'C언어 학습 상담',    now() - INTERVAL '2 days',  now() - INTERVAL '1 hour'),
        (v_chat2_id, v_user_id, '자료구조 복습',      now() - INTERVAL '5 days',  now() - INTERVAL '3 days'),
        (v_chat3_id, v_user_id, '알고리즘 문제 풀이', now() - INTERVAL '10 days', now() - INTERVAL '7 days');

    -- chat1: 메시지 5건
    INSERT INTO messages (id, chat_id, role, content) VALUES
        (gen_random_uuid(), v_chat1_id, 'user',      'C언어에서 포인터가 뭔가요?'),
        (gen_random_uuid(), v_chat1_id, 'assistant', '포인터는 메모리 주소를 저장하는 변수입니다.'),
        (gen_random_uuid(), v_chat1_id, 'user',      '예제 코드 보여주세요.'),
        (gen_random_uuid(), v_chat1_id, 'assistant', 'int *p = &a; 이렇게 사용합니다.'),
        (gen_random_uuid(), v_chat1_id, 'user',      '감사합니다!');

    -- chat2: 메시지 3건
    INSERT INTO messages (id, chat_id, role, content) VALUES
        (gen_random_uuid(), v_chat2_id, 'user',      '링크드 리스트를 설명해주세요.'),
        (gen_random_uuid(), v_chat2_id, 'assistant', '링크드 리스트는 노드들이 포인터로 연결된 자료구조입니다.'),
        (gen_random_uuid(), v_chat2_id, 'user',      '배열과의 차이점은?');

    -- chat3: 메시지 0건 (빈 채팅)

    -- chat1에 커리큘럼 1건
    INSERT INTO curriculums (id, chat_id) VALUES
        (gen_random_uuid(), v_chat1_id);
END $$;
