DROP TABLE users;
CREATE TABLE users (id integer primary key AutoIncrement,mobile varchar(15) unique,imei varchar(30),code varchar(32),codedate datetime,createdate datetime);
DROP TABLE userlogin;
CREATE TABLE userlogin (id integer primary key AutoIncrement,userid integer,agent integer,logindate datetime);