
CREATE TYPE transaction_type AS ENUM ('D', 'P', 'I');
CREATE TYPE payment_method_type AS ENUM ('P', 'D','A', 'S', 'N', 'B');

CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    hash_password VARCHAR(255) NOT NULL
);

CREATE TABLE payment_method (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    closing_day INT,
    method_type payment_method_type
);

CREATE TABLE expense (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    method_id INT NOT NULL REFERENCES payment_method (id),
    reserved NUMERIC(15, 2) NOT NULL DEFAULT 0,
    spent NUMERIC(15, 2) NOT NULL DEFAULT 0
);

CREATE TABLE expense_period (
    id SERIAL PRIMARY KEY,
    expense_id INT NOT NULL REFERENCES expense (id),
    name VARCHAR(255) NOT NULL,
    method_id INT NOT NULL REFERENCES payment_method (id),
    reference_month DATE NOT NULL,
    reserved NUMERIC(15, 2) NOT NULL DEFAULT 0,
    spent NUMERIC(15, 2) NOT NULL DEFAULT 0
);

CREATE TABLE transaction (
    id SERIAL PRIMARY KEY,
    type transaction_type NOT NULL,
    linked_expense_id INT REFERENCES expense_period (id),
    method_id INT NOT NULL REFERENCES payment_method (id),
    date DATE NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    user_id INT NOT NULL REFERENCES "user" (id)
);
