DROP TABLE IF EXISTS contents CASCADE;
DROP TABLE IF EXISTS posts CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS group_permission CASCADE;
DROP TABLE IF EXISTS permissions CASCADE;
DROP TABLE IF EXISTS user_groups CASCADE;


CREATE TABLE permissions
(
    id   bigint PRIMARY KEY NOT NULL,
    name varchar(20) NOT NULL
);

CREATE TABLE user_groups
(
    id   bigint PRIMARY KEY NOT NULL,
    name varchar(20) NOT NULL
);

CREATE TABLE group_permission
(
    id            bigint PRIMARY KEY NOT NULL,
    group_id      bigint NOT NULL,
    permission_id bigint NOT NULL,
    CONSTRAINT unq_group_id_permission_id UNIQUE (group_id, permission_id),
    CONSTRAINT fk_group_id_for_group_permission FOREIGN KEY (group_id) REFERENCES user_groups (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_permission_id_for_group_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE users
(
    id          binary(16) PRIMARY KEY NOT NULL,
    username    varchar(50) NOT NULL,
    enter_key   varchar(80) NOT NULL,
    diary_title varchar(20) NOT NULL,
    nick_name   varchar(15),
    user_id       varchar(15),
    group_id    bigint NOT NULL,
    CONSTRAINT unq_enter_key UNIQUE (enter_key),
    CONSTRAINT fk_group_id_for_user FOREIGN KEY (group_id) REFERENCES user_groups (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

create table posts(
    id              binary(16) PRIMARY KEY NOT NULL,
    user_id         binary(16) NOT NULL,
    emotion         int,
    post_date       datetime,
    written_date    datetime NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE
);

create table contents(
                         id binary(16) PRIMARY KEY NOT NULL,
                         prefix varchar(20),
                         comment longtext,
                         img_name varchar(20) NOT NULL
);

create table mapped_keys(
    username varchar(10) PRIMARY KEY NOT NULL,
    enter_key varchar(20) NOT NULL
);