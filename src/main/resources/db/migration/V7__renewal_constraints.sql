-- 기존 외래키 제약조건들 제거 (PK는 제외)

-- refresh_token 테이블의 외래키 제약조건 제거
ALTER TABLE IF EXISTS refresh_token
    DROP CONSTRAINT IF EXISTS refresh_token_fk_member;

-- article_comment 테이블의 외래키 제약조건 제거
ALTER TABLE IF EXISTS article_comment
    DROP CONSTRAINT IF EXISTS FK_article_comment_table_parent_comment;

-- article 테이블의 외래키 제약조건 제거
ALTER TABLE IF EXISTS article
    DROP CONSTRAINT IF EXISTS FK_article_table_member;

ALTER TABLE IF EXISTS article
    DROP CONSTRAINT IF EXISTS FK_article_table_category;

-- article_comment 테이블의 member, article 관련 외래키 제거 (추정)
ALTER TABLE IF EXISTS article_comment
    DROP CONSTRAINT IF EXISTS FK_article_comment_table_member;

ALTER TABLE IF EXISTS article_comment
    DROP CONSTRAINT IF EXISTS FK_article_comment_table_article;

-- article_category 테이블의 unique 제약조건 제거
ALTER TABLE IF EXISTS article_category
    DROP CONSTRAINT IF EXISTS article_category_name_key;
