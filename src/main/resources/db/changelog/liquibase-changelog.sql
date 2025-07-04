--changeset rodrigo:create-toilet-table
CREATE TABLE toilets (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    has_male BOOLEAN,
    has_female BOOLEAN,
    has_accessible BOOLEAN,
    has_baby_changer BOOLEAN,
    avg_rating DOUBLE PRECISION,
    total_reviews INT
);

--changeset rodrigo:create-review-table
CREATE TABLE reviews (
    id UUID PRIMARY KEY,
    toilet_id UUID NOT NULL,
    rating_general INT,
    rating_cleanliness INT,
    rating_maintenance INT,
    comment VARCHAR(500),
    created_at TIMESTAMP,

    CONSTRAINT fk_review_toilet FOREIGN KEY (toilet_id) REFERENCES toilets(id)
);

--changeset rodrigo:add-place-id-to-toilets
ALTER TABLE toilets ADD COLUMN place_id VARCHAR(255);