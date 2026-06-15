-- 파일명 규칙: V{버전}__{설명}.sql
-- 적용된 마이그레이션 파일은 절대 수정하지 말 것. 스키마 변경은 새 버전 파일(V2, V3, ...)로 추가한다.

CREATE TABLE user_installed_tools (
    id      UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users (id),
    tool    VARCHAR(255) NOT NULL
);

CREATE INDEX idx_user_installed_tools_user_id ON user_installed_tools (user_id);
CREATE UNIQUE INDEX uk_user_installed_tools_user_id_tool ON user_installed_tools (user_id, tool);

-- 테스트용 시드: 해당 유저가 존재할 때만 'code', 'terminal' 도구를 추가
INSERT INTO user_installed_tools (id, user_id, tool)
SELECT gen_random_uuid(), id, tool
FROM users
CROSS JOIN (VALUES ('code'), ('terminal')) AS tools (tool)
WHERE id = 'b2dbc936-0236-4924-9f4e-0e86965470dc'
ON CONFLICT DO NOTHING;
