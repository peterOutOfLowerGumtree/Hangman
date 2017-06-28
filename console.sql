DROP TABLE IF EXISTS words;

CREATE TABLE words (
  id INT PRIMARY KEY AUTO_INCREMENT,
  word VARCHAR(255) NOT NULL
);

SHOW VARIABLES LIKE "secure_file_priv";
LOAD DATA LOCAL INFILE 'C:\\Users\\Administrator\\IdeaProjects\\Hangman\\wordList.txt' INTO TABLE words (@col) set word=@col;