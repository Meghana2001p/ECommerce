CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    order_status ENUM('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(50, 30) NOT NULL CHECK (total_amount > 0),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
CREATE TABLE order_item (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 1),
    price DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);
CREATE TABLE payment (
   payment_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    payment_method ENUM('CREDIT_CARD', 'DEBIT_CARD', 'UPI', 'NET_BANKING', 'WALLET', 'COD') NOT NULL,
    status ENUM('SUCCESS', 'FAILED', 'PENDING', 'CANCELLED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    transaction_id VARCHAR(100),
    paid_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
CREATE TABLE delivery_status (
   delivery_status_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    status ENUM('PENDING', 'SHIPPED', 'IN_TRANSIT', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED') NOT NULL,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tracking_number VARCHAR(100),
    carrier VARCHAR(100),
    estimated_delivery_date DATE,
    delivery_type ENUM('STANDARD', 'EXPRESS', 'SAME_DAY', 'NEXT_DAY') NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
CREATE TABLE order_status_history (
    order_status_history_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    status ENUM(
        'PENDING',
        'CONFIRMED',
        'PROCESSING',
        'SHIPPED',
        'OUT_FOR_DELIVERY',
        'DELIVERED',
        'CANCELLED',
        'RETURN_REQUESTED',
        'RETURNED',
        'REFUNDED'
    ) NOT NULL,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

CREATE TABLE return_request (
    return_request_id INT PRIMARY KEY AUTO_INCREMENT,
    order_item_id INT NOT NULL,
    reason VARCHAR(500) NOT NULL,
    status ENUM('REQUESTED', 'APPROVED', 'REJECTED', 'PICKED_UP', 'COMPLETED') NOT NULL DEFAULT 'REQUESTED',
    requested_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_item_id) REFERENCES order_item(order_item_id)
);
CREATE TABLE refund (
    refund_id INT PRIMARY KEY AUTO_INCREMENT,
    payment_id INT NOT NULL,
    amount DECIMAL(60,30) NOT NULL CHECK (amount > 0),
    status ENUM('INITIATED', 'PROCESSING', 'SUCCESS', 'FAILED') NOT NULL DEFAULT 'INITIATED',
    refunded_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payment_id) REFERENCES payment(payment_id)
);
CREATE TABLE coupon (
    coupon_id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(100) NOT NULL UNIQUE,
    discount_percent DECIMAL(60,30) NOT NULL CHECK (discount_percent > 0 AND discount_percent <= 100),
    expiry_date DATE NOT NULL,
    usage_limit INT NOT NULL CHECK (usage_limit >= 1)
);

CREATE TABLE applied_coupon (
   applied_coupon_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    coupon_id INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (coupon_id) REFERENCES coupon(coupon_id)
);
CREATE TABLE gift_order (
    gift_order_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL UNIQUE,
    gift_message VARCHAR(500),
    hide_price BOOLEAN DEFAULT FALSE,
    gift_wrapping BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
