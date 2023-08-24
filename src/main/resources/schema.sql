create table if not exists member (
    id uuid,
    nickname varchar(255) not null unique,
    email varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(255) not null check (role in ('ADMIN','MEMBER')),
    is_activated boolean default false not null,
    created_at timestamp(6) default CURRENT_TIMESTAMP,
    updated_at timestamp(6) default CURRENT_TIMESTAMP,
    primary key (id)
);

create table if not exists article_category(
    id bigserial,
    name varchar(255) unique,
    created_at timestamp(6) default CURRENT_TIMESTAMP,
    updated_at timestamp(6) default CURRENT_TIMESTAMP,
    primary key (id)
);

create table if not exists article (
    id uuid,
    category_id bigint,
    member_id uuid,
    title varchar(255) not null,
    contents text not null,
    view_count bigint default 0,
    created_at timestamp(6) not null default CURRENT_TIMESTAMP,
    updated_at timestamp(6) default CURRENT_TIMESTAMP,
    primary key (id)
);

create table if not exists article_comment(
    id bigserial,
    member_id uuid,
    article_id uuid,
    parent_id bigint unique,
    contents text,
    created_at timestamp(6) default CURRENT_TIMESTAMP,
    updated_at timestamp(6) default CURRENT_TIMESTAMP,
    primary key (id)
);

alter table if exists article
   add constraint FK6l9vkfd5ixw8o8kph5rj1k7gu
   foreign key (member_id)
   references member;

alter table if exists article
   add constraint FKo15210u2t92il4ihrnhaiqv7a
   foreign key (category_id)
   references article_category;

alter table if exists article_comment
   add constraint FK5yx0uphgjc6ik6hb82kkw501y
   foreign key (article_id)
   references article;

alter table if exists article_comment
   add constraint FKmrrrpi513ssu63i2783jyiv9m
   foreign key (member_id)
   references member;

alter table if exists article_comment
   add constraint FKde3rfu96lep00br5ov0mdieyt
   foreign key (parent_id)
   references article_comment;