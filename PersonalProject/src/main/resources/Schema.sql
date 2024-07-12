CREATE TABLE budget (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(255),
    date DATE,
    amount DECIMAL(10, 2),
    category VARCHAR(255),
    description VARCHAR(255),
    budget DECIMAL(10, 2)
);