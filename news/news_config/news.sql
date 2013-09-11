DROP TABLE news;
CREATE TABLE news (id integer primary key AutoIncrement,link varchar(1024) unique,newstype int,title varchar(128),source varchar(128),sourcetime varchar(32),content varchar(20000),status int,collectdate datetime);
