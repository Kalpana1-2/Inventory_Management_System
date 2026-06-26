create table users(
	user_id int primary key,
	user_name varchar(25),
	email_id varchar(100)not null unique,
	password varchar(50)not null,
	phone_no varchar(10),check(phone_no regexp '^[0-9]{10}$'),
	role varchar(30)
	);
	
create table category(
	category_id int primary key auto_increment,
	category_type varchar(20)
	);

create table product(
	product_id int primary key auto_increment,
	product_name varchar(20),
	description varchar(255),
	price decimal(10,2),
	img_url varchar(255),
	category_id int,
	foreign key(category_id) references category(category_id)
	);

CREATE TABLE product_stock (
    stock_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT UNIQUE,
    quantity INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    user_id INT,
    quantity INT NOT NULL,
    order_date DATE,
    order_status VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
create table product_company(
	company_id int primary key,
	product_id int,
	company_name varchar(30),
	compony_address varchar(100),
	email_id varchar(100)not null unique,
	phone_no varchar(10),check(phone_no regexp '^[0-9]{10}$'),
	foreign key(product_id)references product(product_id)
	);

CREATE TABLE order_items (
    orderItem_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT,
    order_price DECIMAL(10,2),
    order_status VARCHAR(30),
    total_amount decimal(10,2),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);
create table user_address(
	user_id int primary key,
	city varchar(50),
	distt varchar(50),
	state varchar(50),
	pincode int,
	foreign key(user_id)references users(user_id)
	);

CREATE TABLE user_payment (
    payment_id INT PRIMARY KEY UNIQUE,
    user_id INT,
    order_id INT,
    payment_method VARCHAR(50),
    total_amount DECIMAL(10,2),
    amount_paid DECIMAL(10,2),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
create table order_payment(
	payment_id int primary key auto_increment,
	order_id int,
	payment_method varchar(50),
	payment_status varchar(50),
	transaction_id varchar(255),
	amount_paid decimal(10,2),
	payment_date datetime,
	foreign key(order_id)references orders(order_id)
	);
	
	
	
	
	
	
	
	
