-- 외래키 제약조건 삭제
ALTER TABLE IF EXISTS article_comment
    DROP CONSTRAINT IF EXISTS FK_article_comment_table_parent_comment;

-- 기존 id 컬럼 삭제
ALTER TABLE IF EXISTS article_comment
    DROP COLUMN IF EXISTS id;

-- parent_id 컬럼도 uuid로 변경 (기존 데이터는 NULL로 처리)
ALTER TABLE IF EXISTS article_comment
    DROP COLUMN IF EXISTS parent_id;

-- 새로운 id 컬럼 추가 (uuid)
ALTER TABLE IF EXISTS article_comment
    ADD COLUMN id uuid PRIMARY KEY;

-- 새로운 parent_id 컬럼 추가 (uuid)
ALTER TABLE IF EXISTS article_comment
    ADD COLUMN parent_id uuid;

-- 외래키 제약조건 다시 추가
ALTER TABLE IF EXISTS article_comment
    ADD CONSTRAINT FK_article_comment_table_parent_comment
    FOREIGN KEY(parent_id) REFERENCES article_comment(id) ON DELETE SET NULL;

-- contents 컬럼을 content로 이름 변경 및 타입 변경
ALTER TABLE IF EXISTS article_comment
    ADD COLUMN content VARCHAR(500) NOT NULL DEFAULT '';

-- 기존 contents 데이터를 content로 복사 (데이터가 있다면)
UPDATE article_comment SET content = COALESCE(contents, '') WHERE contents IS NOT NULL;

-- 기존 contents 컬럼 삭제
ALTER TABLE IF EXISTS article_comment
    DROP COLUMN IF EXISTS contents;

ALTER TABLE IF EXISTS article_comment
    ADD COLUMN depth INT DEFAULT 0 NOT NULL;

ALTER TABLE IF EXISTS article_comment
    ADD COLUMN reply_count INT DEFAULT 0 NOT NULL;
