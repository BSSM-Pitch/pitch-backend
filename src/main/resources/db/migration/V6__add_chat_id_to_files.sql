-- 파일명 규칙: V{버전}__{설명}.sql
-- 적용된 마이그레이션 파일은 절대 수정하지 말 것. 스키마 변경은 새 버전 파일(V2, V3, ...)로 추가한다.

-- 업로드만 되고 아직 채팅에 안 붙은 파일이 있을 수 있으므로 nullable
ALTER TABLE files ADD COLUMN chat_id UUID REFERENCES chats (id);

CREATE INDEX idx_files_chat_id ON files (chat_id);
