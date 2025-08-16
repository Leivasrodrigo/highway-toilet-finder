--liquibase formatted sql

-- changeset rodrigo:create-places-table
CREATE TABLE places (
    id BINARY(16) PRIMARY KEY,
    status VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    name VARCHAR(255),
    address VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    google_place_id VARCHAR(255),
    CONSTRAINT uk_google_place_id UNIQUE (google_place_id)
);

-- changeset rodrigo:create-users-table
CREATE TABLE users (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255)
);

-- changeset rodrigo:create-toilet-table
CREATE TABLE toilets (
    id BINARY(16) PRIMARY KEY,
    status VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    gender VARCHAR(10) NULL,
    price VARCHAR(10) NULL,
    has_shower BOOLEAN NULL,
    has_accessible BOOLEAN NULL,
    has_baby_changer BOOLEAN NULL,
    avg_rating DOUBLE,
    total_reviews INT,
    place_id BINARY(16) NOT NULL UNIQUE,
    CONSTRAINT fk_toilet_place FOREIGN KEY (place_id) REFERENCES places(id) ON DELETE CASCADE
);

-- changeset rodrigo:create-review-table
CREATE TABLE reviews (
    id BINARY(16) PRIMARY KEY,
    status VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    toilet_id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    rating_general INT,
    rating_cleanliness INT,
    rating_maintenance INT,
    comment VARCHAR(500),
    created_at TIMESTAMP,
    CONSTRAINT fk_review_toilet FOREIGN KEY (toilet_id) REFERENCES toilets(id),
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- changeset rodrigo:create-refresh-token-table
CREATE TABLE refresh_token (
    id BINARY(16) PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    user_id BINARY(16) NOT NULL,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_refresh_token_token UNIQUE (token)
);
