ALTER TABLE hotel_reviews
DROP
CONSTRAINT hotel_reviews_reservation_id_fkey;

ALTER TABLE hotels
DROP
CONSTRAINT hotels_manager_id_fkey;

CREATE SEQUENCE IF NOT EXISTS revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE revchanges
(
    rev        BIGINT NOT NULL,
    entityname VARCHAR(255)
);

CREATE TABLE revinfo
(
    rev      BIGINT NOT NULL,
    revtstmp BIGINT,
    CONSTRAINT "pk_revınfo" PRIMARY KEY (rev)
);

ALTER TABLE revchanges
    ADD CONSTRAINT "fk_revchanges_on_default_trackıng_modıfıed_entıtıes_changelog" FOREIGN KEY (rev) REFERENCES revinfo (rev);

ALTER TABLE payments
    ALTER COLUMN amount SET NOT NULL;

ALTER TABLE room_types
    ALTER COLUMN base_capacity SET NOT NULL;

ALTER TABLE room_types
ALTER
COLUMN bed_configuration TYPE VARCHAR(255) USING (bed_configuration::VARCHAR(255));

ALTER TABLE room_types
    ALTER COLUMN bed_configuration SET NOT NULL;

ALTER TABLE bookings
ALTER
COLUMN booking_number TYPE VARCHAR(255) USING (booking_number::VARCHAR(255));

ALTER TABLE hotels
    ALTER COLUMN commission_rate SET NOT NULL;

ALTER TABLE payments
ALTER
COLUMN currency TYPE VARCHAR(3) USING (currency::VARCHAR(3));

ALTER TABLE payments
    ALTER COLUMN currency SET NOT NULL;

ALTER TABLE room_prices
ALTER
COLUMN currency TYPE VARCHAR(20) USING (currency::VARCHAR(20));

ALTER TABLE room_prices
    ALTER COLUMN currency SET NOT NULL;

ALTER TABLE hotels
    ALTER COLUMN district SET NOT NULL;

ALTER TABLE users
ALTER
COLUMN email TYPE VARCHAR(255) USING (email::VARCHAR(255));

ALTER TABLE room_prices
    ALTER COLUMN end_date SET NOT NULL;

ALTER TABLE hotels
    ALTER COLUMN iban_no SET NOT NULL;

ALTER TABLE reservation_guests
    ALTER COLUMN is_primary_guest SET NOT NULL;

ALTER TABLE room_types
    ALTER COLUMN max_adults SET NOT NULL;

ALTER TABLE room_types
    ALTER COLUMN max_children SET NOT NULL;

ALTER TABLE hotels
ALTER
COLUMN name TYPE VARCHAR(100) USING (name::VARCHAR(100));

ALTER TABLE users
ALTER
COLUMN name TYPE VARCHAR(255) USING (name::VARCHAR(255));

ALTER TABLE users
ALTER
COLUMN password_hash TYPE VARCHAR(255) USING (password_hash::VARCHAR(255));

ALTER TABLE payments
ALTER
COLUMN payment_method TYPE VARCHAR(255) USING (payment_method::VARCHAR(255));

ALTER TABLE payments
    ALTER COLUMN payment_method SET NOT NULL;

ALTER TABLE bookings
ALTER
COLUMN payment_status TYPE VARCHAR(255) USING (payment_status::VARCHAR(255));

ALTER TABLE hotels
ALTER
COLUMN phone TYPE VARCHAR(255) USING (phone::VARCHAR(255));

ALTER TABLE hotels
    ALTER COLUMN phone SET NOT NULL;

ALTER TABLE reservations
    ALTER COLUMN price_per_night SET NOT NULL;

ALTER TABLE room_prices
    ALTER COLUMN start_date SET NOT NULL;

ALTER TABLE bookings
ALTER
COLUMN status TYPE VARCHAR(255) USING (status::VARCHAR(255));

ALTER TABLE hotels
ALTER
COLUMN status TYPE VARCHAR(255) USING (status::VARCHAR(255));

ALTER TABLE payments
ALTER
COLUMN status TYPE VARCHAR(255) USING (status::VARCHAR(255));

ALTER TABLE payments
    ALTER COLUMN status SET NOT NULL;

ALTER TABLE reservations
ALTER
COLUMN status TYPE VARCHAR(255) USING (status::VARCHAR(255));

ALTER TABLE room_types
ALTER
COLUMN status TYPE VARCHAR(255) USING (status::VARCHAR(255));

ALTER TABLE users
ALTER
COLUMN surname TYPE VARCHAR(255) USING (surname::VARCHAR(255));

ALTER TABLE room_types
ALTER
COLUMN title TYPE VARCHAR(100) USING (title::VARCHAR(100));

ALTER TABLE reservations
    ALTER COLUMN total_price SET NOT NULL;

ALTER TABLE payments
ALTER
COLUMN transaction_id TYPE VARCHAR(255) USING (transaction_id::VARCHAR(255));

ALTER TABLE room_types ADD COLUMN description TEXT;

ALTER TABLE hotel_reviews ADD CONSTRAINT chk_review_rating CHECK (rating >= 1 AND rating <= 5);

ALTER TABLE hotels
    ADD COLUMN averageRating NUMERIC(3, 1) DEFAULT 0.0;

ALTER TABLE room_types ADD COLUMN description TEXT;