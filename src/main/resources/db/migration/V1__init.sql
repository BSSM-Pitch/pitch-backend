-- 파일명 규칙: V{버전}__{설명}.sql
-- 적용된 마이그레이션 파일은 절대 수정하지 말 것. 스키마 변경은 새 버전 파일(V2, V3, ...)로 추가한다.

CREATE TABLE users (
    id         UUID PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX uk_users_email ON users (email);

CREATE TABLE refresh_tokens (
    id         UUID PRIMARY KEY,
    user_id    UUID NOT NULL REFERENCES users (id),
    token      VARCHAR(512) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX uk_refresh_tokens_user_id ON refresh_tokens (user_id);
