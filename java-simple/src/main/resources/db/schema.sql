CREATE SCHEMA TEST;
CREATE TABLE SystemAccount
(
  id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(30)  NOT NULL,
  password VARCHAR(200) NOT NULL
);