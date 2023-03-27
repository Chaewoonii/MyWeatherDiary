

--docker run --name diarydb -e MYSQL_PORT_HOST=% -e MYSQL_ROOT_PASSWORD=test1234@# -p3306:3306 -d mysql:8
--docker exec -it diarydb mysql -uroot -p

use diarydb;
create database diarydb;

create table users(
                      id binary(16) primary key,
                      diary_title varchar(50) not null,
                      enter_key varchar(20) not null,
                      nick_name varchar(15),
                      email varchar(50)
);

create table posts(
                      id binary(16) primary key not null,
                      user_id binary(16) not null,
                      emotion int,
                      post_date datetime,
                      written_date datetime not null,

                      foreign key (user_id) references users(id) on update cascade
);

create table contents(
                         id binary(16) primary key not null,
                         prefix varchar(20),
                         comment longtext,
                         img_saved_date datetime not null
);



/*create database diarydb;

CREATE TABLE users(
                      id BINARY(16) PRIMARY KEY,
                      diary_title varchar(50),
                      enter_key varchar(100)
);

CREATE TABLE posts(
                      id VARCHAR(36) PRIMARY KEY,
                      user_id VARCHAR(36) NOT NULL,
                      post_comment VARCHAR(600),
                      feelings INT,
                      post_date DATETIME DEFAULT now(),
                      reg_date DATETIME DEFAULT now(),
                      mod_date DATETIME DEFAULT now(),

                      FOREIGN KEY(user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE event_logs(
                           event varchar(20),
                           ip varchar(15),
                           reg_date datetime DEFAULT now()
);*/

DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS group_permission CASCADE;
DROP TABLE IF EXISTS user_groups CASCADE;
DROP TABLE IF EXISTS permissions CASCADE;

CREATE TABLE permissions
(
    id   bigint      NOT NULL,
    name varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE groups
(
    id   bigint      NOT NULL,
    name varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE group_permission
(
    id            bigint NOT NULL,
    group_id      bigint NOT NULL,
    permission_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unq_group_id_permission_id UNIQUE (group_id, permission_id),
    CONSTRAINT fk_group_id_for_group_permission FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_permission_id_for_group_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE users
(
    id       bigint      NOT NULL,
    login_id varchar(20) NOT NULL,
    passwd   varchar(80) NOT NULL,
    group_id bigint      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unq_login_id UNIQUE (login_id),
    CONSTRAINT fk_group_id_for_user FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);