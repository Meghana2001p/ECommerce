CREATE TABLE email_log (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    email_type ENUM(
        'ORDER_CONFIRMATION',
        'PASSWORD_RESET',
        'SHIPPING_UPDATE',
        'PROMOTION',
        'REFUND'
    ) NOT NULL,
    order_id INT,
    recipient_email VARCHAR(255) NOT NULL,
    sent_at DATETIME NOT NULL,
    status ENUM('SENT', 'FAILED', 'QUEUED', 'RETRYING') NOT NULL,
    error_message TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);
CREATE TABLE email_notification (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type ENUM(
        'ORDER_CONFIRMATION',
        'PASSWORD_RESET',
        'SHIPPING_UPDATE',
        'PROMOTION',
        'REFUND'
    ) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body_template TEXT NOT NULL,
    is_html BOOLEAN DEFAULT TRUE
);
CREATE TABLE notification_queue (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    type ENUM('EMAIL', 'SMS', 'IN_APP') NOT NULL,
    message TEXT NOT NULL,
    status ENUM('PENDING', 'SENT', 'FAILED', 'CANCELLED') NOT NULL,
    scheduled_at DATETIME NOT NULL
);
