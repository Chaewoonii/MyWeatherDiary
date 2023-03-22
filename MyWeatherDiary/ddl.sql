

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

