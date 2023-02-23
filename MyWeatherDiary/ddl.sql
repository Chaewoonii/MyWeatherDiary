create database diarydb;

CREATE TABLE users(
                      id VARCHAR(36) PRIMARY KEY,
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
);