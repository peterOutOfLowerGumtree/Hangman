DROP TABLE IF EXISTS words;
DROP TABLE IF EXISTS users;

CREATE TABLE words (
  id INT PRIMARY KEY AUTO_INCREMENT,
  word VARCHAR(255) NOT NULL
);

CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  played INT,
  lost INT,
  winPercentage DOUBLE,
  leastGuesses INT,
  mostGuesses INT
);

# SHOW VARIABLES LIKE "secure_file_priv";
LOAD DATA LOCAL INFILE 'C:\\Users\\Administrator\\IdeaProjects\\Hangman\\wordList.txt' INTO TABLE words (@col) set word=@col;