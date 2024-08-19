--SAMPLE DATA
INSERT INTO budget (type, date, amount, category, description, budget)
VALUES ('Income', '2024-07-01', 1000.00, 'Salary', 'Monthly Salary', 1000.00);

INSERT INTO budget (type, date, amount, category, description, budget)
VALUES ('Expense', '2024-07-02', 10.00, 'Groceries', 'Monthly grocery shopping', 990.00);

INSERT INTO budget (type, date, amount, category, description, budget)
VALUES ('Expense', '2024-07-03', 200.00, 'Shopping', 'Clothing and accessories', 790.00);

INSERT INTO budget (type, date, amount, category, description, budget)
VALUES ('Expense', '2024-07-04', 50.00, 'Entertainment', 'Movie night', 740.00);

INSERT INTO budget (type, date, amount, category, description, budget)
VALUES ('Expense', '2024-07-05', 1500.00, 'Rent', 'Monthly rent payment', -760.00);

INSERT INTO budget (type, date, amount, category, description, budget)
VALUES ('Income', '2024-07-06', 1000.00, 'Salary', 'Monthly Salary', 1000.00);

INSERT INTO budget (type, date, amount, category, description, budget)
VALUES ('Expense', '2024-07-07', 240.00, 'Takeout', 'Dinner takeout', 00.00);
--USERS
INSERT INTO sec_user (email, encryptedPassword, enabled)
VALUES ('pakv@sheridancollege.ca', '$2a$10$1ltibqiyyBJMJQ4hqM7f0OusP6np/IHshkYc4TjedwHnwwNChQZCy', 1);

INSERT INTO sec_user (email, encryptedPassword, enabled)
VALUES ('user@sheridancollege.ca', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);

INSERT INTO sec_role (roleName)
VALUES ('ROLE_USER');

INSERT INTO sec_role (roleName)
VALUES ('ROLE_ADMIN');

-- Assign the correct role IDs
INSERT INTO user_role (userId, roleId)
VALUES (1, 2); -- pakv@sheridancollege.ca gets ROLE_ADMIN

INSERT INTO user_role (userId, roleId)
VALUES (2, 1); -- user@sheridancollege.ca gets ROLE_USER
