create database shoeshopdb;
use shoeshopdb;

CREATE TABLE Shoes (
	shoe_id int NOT NULL AUTO_INCREMENT,
	shoe_name varchar(80) NOT NULL,
	shoe_price int NOT NULL,
	shoe_size int NOT NULL,
	shoe_status varchar(10) NOT NULL,
	constraint pk_shoes PRIMARY KEY (shoe_id)
);

CREATE TABLE Employee (
	emp_id int NOT NULL AUTO_INCREMENT,
	emp_name varchar(80) NOT NULL,
	emp_salary int NOT NULL,
	constraint pk_employee PRIMARY KEY (emp_id)
);

CREATE TABLE Customer (
	cust_id int NOT NULL AUTO_INCREMENT,
	cust_name varchar(80) NOT NULL UNIQUE,
	constraint pk_customer PRIMARY KEY (cust_id)
);
		
CREATE TABLE Bill (
	bill_id int NOT NULL AUTO_INCREMENT,
	cust_id int NOT NULL,
	shoe_id int NOT NULL,
	constraint pk_bill PRIMARY KEY (bill_id)
);
