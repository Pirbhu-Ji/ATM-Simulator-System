-- SQL script to create the ATM Simulator database and tables

-- 1. Create the database
CREATE DATABASE IF NOT EXISTS bankdb;
USE bankdb;

-- 2. Table: login
-- Stores card number and PIN for authentication
CREATE TABLE IF NOT EXISTS login (
    card_number VARCHAR(14) PRIMARY KEY,
    pin VARCHAR(4) NOT NULL
);

-- 3. Table: accounts
-- Stores account details and current balance
CREATE TABLE IF NOT EXISTS accounts (
    accno VARCHAR(11) PRIMARY KEY,
    cardno VARCHAR(14) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    balance DOUBLE NOT NULL,
    pin VARCHAR(4) NOT NULL
);

-- 4. Table: transactions
-- Stores all transaction history
CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cardno VARCHAR(14) NOT NULL,
    type VARCHAR(20) NOT NULL, -- DEPOSIT / WITHDRAW / FASTCASH
    amount DOUBLE NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    balance_after DOUBLE NOT NULL,
    FOREIGN KEY (cardno) REFERENCES accounts(cardno)
);
