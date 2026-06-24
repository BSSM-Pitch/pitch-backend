-- 파일명 규칙: V{버전}__{설명}.sql
-- 적용된 마이그레이션 파일은 절대 수정하지 말 것. 스키마 변경은 새 버전 파일(V2, V3, ...)로 추가한다.

CREATE TABLE files (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID NOT NULL REFERENCES users (id),
    name        VARCHAR(255) NOT NULL,
    size        BIGINT NOT NULL,
    mime_type   VARCHAR(255) NOT NULL,
    storage_key VARCHAR(512) NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_files_user_id ON files (user_id);
