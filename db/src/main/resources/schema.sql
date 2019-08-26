CREATE TABLE USERS
(
  id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  first_name VARCHAR(100),
  last_name  VARCHAR(100)
);

CREATE TABLE ACCOUNTS
(
  id      INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  user_id INT                            NOT NULL,
  balance DOUBLE,
  FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE TRANSACTIONS
(
  id               INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  account_donor    INT                            NOT NULL,
  account_acceptor INT                            NOT NULL,
  amount           DOUBLE                         NOT NULL,
  FOREIGN KEY (account_donor) REFERENCES accounts (id),
  FOREIGN KEY (account_acceptor) REFERENCES accounts (id)
);

INSERT INTO USERS VALUES (null, 'Tyrion', 'Lannister');
INSERT INTO USERS VALUES (null, 'Cersei', 'Lannister');
INSERT INTO USERS VALUES (null, 'Jaime', 'Lannister');
INSERT INTO USERS VALUES (null, 'Ned', 'Stark');
INSERT INTO USERS VALUES (null, 'Sansa', 'Stark');
INSERT INTO USERS VALUES (null, 'Arya', 'Stark');
INSERT INTO USERS VALUES (null, 'Jon', 'Snow');
INSERT INTO USERS VALUES (null, 'Daenerys', 'Targaryen');
INSERT INTO USERS VALUES (null, 'Rob', 'Stark');
INSERT INTO USERS VALUES (null, 'Tywin', 'Lannister');
INSERT INTO USERS VALUES (null, 'Brienne', 'Tarth');
INSERT INTO USERS VALUES (null, 'Lord', 'Varys');

INSERT INTO ACCOUNTS VALUES (null, 1,  100.23);
INSERT INTO ACCOUNTS VALUES (null, 1,  11.76);
INSERT INTO ACCOUNTS VALUES (null, 2,  110.34);
INSERT INTO ACCOUNTS VALUES (null, 3,  76.98);
INSERT INTO ACCOUNTS VALUES (null, 4,  150.09);
INSERT INTO ACCOUNTS VALUES (null, 5,  180.43);
INSERT INTO ACCOUNTS VALUES (null, 6,  134.21);
INSERT INTO ACCOUNTS VALUES (null, 7,  22.87);
INSERT INTO ACCOUNTS VALUES (null, 8,  0.0);
INSERT INTO ACCOUNTS VALUES (null, 9,  0.0);
INSERT INTO ACCOUNTS VALUES (null, 10, 24.40);
INSERT INTO ACCOUNTS VALUES (null, 11, 0.0);
INSERT INTO ACCOUNTS VALUES (null, 12, 0.0);

INSERT INTO TRANSACTIONS VALUES (null, 1, 11, 11.50);
INSERT INTO TRANSACTIONS VALUES (null, 3, 11, 12.90);

