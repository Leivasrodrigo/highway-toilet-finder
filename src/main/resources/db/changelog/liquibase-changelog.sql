--liquibase formatted sql

-- changeset rodrigo:create-places-table
CREATE TABLE places (
    id CHAR(36) PRIMARY KEY,
    status VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    name VARCHAR(255),
    address VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    google_place_id VARCHAR(255)
);

-- changeset rodrigo:create-users-table
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    password_hash VARCHAR(255)
);

-- changeset rodrigo:create-toilet-table
CREATE TABLE toilets (
    id CHAR(36) PRIMARY KEY,
    status VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    gender VARCHAR(10) NOT NULL,
    has_shower BOOLEAN,
    has_accessible BOOLEAN,
    has_baby_changer BOOLEAN,
    avg_rating DOUBLE,
    total_reviews INT,
    place_id CHAR(36),
    CONSTRAINT fk_toilet_place FOREIGN KEY (place_id) REFERENCES places(id)
);

-- changeset rodrigo:create-review-table
CREATE TABLE reviews (
    id CHAR(36) PRIMARY KEY,
    status VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    toilet_id CHAR(36) NOT NULL,
    user_id CHAR(36) NOT NULL,
    rating_general INT,
    rating_cleanliness INT,
    rating_maintenance INT,
    comment VARCHAR(500),
    created_at TIMESTAMP,
    CONSTRAINT fk_review_toilet FOREIGN KEY (toilet_id) REFERENCES toilets(id),
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id)
);
