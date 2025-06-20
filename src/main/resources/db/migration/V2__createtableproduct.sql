

CREATE TABLE product_attribute (
    attribute_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
);
CREATE TABLE product_attribute_value (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    attribute_id INT NOT NULL,
    value VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (attribute_id) REFERENCES product_attribute(attribute_id),
    UNIQUE (product_id, attribute_id)  -- Optional: Prevent duplicate attributes per product
);
CREATE TABLE inventory (
   inventory_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    stock_quantity INT NOT NULL,
    last_updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    in_stock BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);
CREATE TABLE price_history (
    price_history_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    old_price DECIMAL(10, 2) NOT NULL CHECK (old_price > 0),
    new_price DECIMAL(10, 2) NOT NULL CHECK (new_price > 0),
    changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);
CREATE TABLE discount (
    discount_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    discount_percent DECIMAL(5, 2) NOT NULL CHECK (discount_percent >= 0.01 AND discount_percent <= 100.0),
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);
CREATE TABLE product_discount (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    discount_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (discount_id) REFERENCES discount(discount_id),
    UNIQUE (product_id, discount_id)
);
CREATE TABLE related_product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    related_product_id INT NOT NULL,
    relationship_type ENUM('SIMILAR', 'ACCESSORY', 'FREQUENTLY_BOUGHT_TOGETHER', 'RECOMMENDED') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (related_product_id) REFERENCES product(product_id),
    UNIQUE (product_id, related_product_id, relationship_type)
);

CREATE TABLE product_image (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    alt_text VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);
