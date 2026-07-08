-- V1__init_schemas.sql

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email varchar(255) UNIQUE NOT NULL,
                       password_hash varchar(255) NOT NULL,
                       name varchar(255) NOT NULL,
                       surname varchar(255) NOT NULL,
                       role varchar(255) NOT NULL,
                       datebirth timestamp NOT NULL,
                       phonenumber varchar(255),
                       email_verified boolean NOT NULL,
                       status varchar(255) NOT NULL,
                       created_at timestamp,
                       updated_at timestamp
);

CREATE TABLE hotels (
                        id BIGSERIAL PRIMARY KEY,
                        manager_id bigint NOT NULL,
                        name varchar(255) NOT NULL,
                        city varchar(255) NOT NULL,
                        district varchar(255),
                        address varchar(255) NOT NULL,
                        phone varchar(255),
                        description text,
                        commission_rate decimal(4,2), -- Komisyon oranını hassaslaştırdık (%10.50 gibi)
                        iban_no varchar(255),
                        status varchar(255) NOT NULL,
                        created_at timestamp,
                        updated_at timestamp
);

CREATE TABLE hotel_reviews (
                               id BIGSERIAL PRIMARY KEY,
                               hotel_id bigint NOT NULL,
                               user_id bigint NOT NULL,
                               reservation_id bigint UNIQUE NOT NULL,
                               rating int NOT NULL,
                               comment text,
                               created_at timestamp,
                               updated_at timestamp
);

CREATE TABLE hotel_images (
                              id BIGSERIAL PRIMARY KEY,
                              hotel_id bigint NOT NULL,
                              category_id bigint NOT NULL,
                              image_url varchar(255) NOT NULL,
                              display_order int,
                              created_at timestamp
);

CREATE TABLE image_categories (
                                  id BIGSERIAL PRIMARY KEY,
                                  name varchar(255) UNIQUE NOT NULL,
                                  created_at timestamp
);

CREATE TABLE room_types (
                            id BIGSERIAL PRIMARY KEY,
                            hotel_id bigint NOT NULL,
                            title varchar(255) NOT NULL,
                            max_adults int,
                            max_children int,
                            base_capacity int,
                            bed_configuration varchar(255),
                            total_inventory int NOT NULL,
                            status varchar(255) NOT NULL,
                            created_at timestamp,
                            updated_at timestamp
);

CREATE TABLE room_type_images (
                                  id BIGSERIAL PRIMARY KEY,
                                  room_type_id bigint NOT NULL,
                                  image_url varchar(255) NOT NULL,
                                  display_order int,
                                  created_at timestamp,
                                  updated_at timestamp
);

CREATE TABLE feature_categories (
                                    id BIGSERIAL PRIMARY KEY,
                                    name varchar(255) NOT NULL,
                                    type varchar(255) NOT NULL
);

CREATE TABLE features (
                          id BIGSERIAL PRIMARY KEY,
                          category_id bigint NOT NULL,
                          name varchar(255) NOT NULL,
                          type varchar(255) NOT NULL
);

CREATE TABLE hotel_features (
                                hotel_id bigint NOT NULL,
                                feature_id bigint NOT NULL,
                                PRIMARY KEY (hotel_id, feature_id) -- Çift kayıt önlemek için composite PK ekledik
);

CREATE TABLE room_type_features (
                                    id BIGSERIAL PRIMARY KEY,
                                    room_type_id bigint NOT NULL,
                                    feature_id bigint NOT NULL,
                                    created_at timestamp
);

CREATE TABLE room_prices (
                             id BIGSERIAL PRIMARY KEY,
                             room_type_id bigint NOT NULL,
                             currency varchar(255),
                             start_date date,
                             end_date date,
                             price_per_night decimal(10,2) NOT NULL,
                             created_at timestamp,
                             updated_at timestamp
);

CREATE TABLE bookings (
                          id BIGSERIAL PRIMARY KEY,
                          booking_number varchar(255) UNIQUE NOT NULL,
                          user_id bigint NOT NULL,
                          total_amount decimal(10,2) NOT NULL,
                          status varchar(255) NOT NULL,
                          payment_status varchar(255) NOT NULL,
                          created_at timestamp,
                          updated_at timestamp
);

CREATE TABLE reservations (
                              id BIGSERIAL PRIMARY KEY,
                              booking_id bigint NOT NULL,
                              hotel_id bigint NOT NULL,
                              room_type_id bigint NOT NULL,
                              check_in_date date NOT NULL,
                              check_out_date date NOT NULL,
                              adult_count int NOT NULL,
                              child_count int DEFAULT 0,
                              price_per_night decimal(10,2),
                              total_price decimal(10,2),
                              status varchar(255) NOT NULL,
                              created_at timestamp,
                              updated_at timestamp
);

CREATE TABLE reservation_guests (
                                    id BIGSERIAL PRIMARY KEY,
                                    reservation_id bigint NOT NULL,
                                    name varchar(255) NOT NULL,
                                    surname varchar(255) NOT NULL,
                                    guest_type varchar(255) NOT NULL,
                                    is_primary_guest boolean DEFAULT false,
                                    created_at timestamp
);

CREATE TABLE payments (
                          id BIGSERIAL PRIMARY KEY,
                          booking_id bigint NOT NULL,
                          amount decimal(10,2),
                          payment_method varchar(255),
                          status varchar(255),
                          currency varchar(255),
                          transaction_id varchar(255),
                          created_at timestamp
);

CREATE TABLE favorites (
                           id BIGSERIAL PRIMARY KEY,
                           user_id bigint NOT NULL,
                           hotel_id bigint NOT NULL,
                           created_at timestamp
);

CREATE TABLE refresh_tokens (
                                id BIGSERIAL PRIMARY KEY,
                                user_id bigint NOT NULL,
                                token_hash varchar(255) NOT NULL,
                                expires_at timestamp,
                                revoked_at timestamp,
                                created_at timestamp
);

CREATE TABLE audit_logs (
                            id BIGSERIAL PRIMARY KEY,
                            user_id bigint,
                            action varchar(255),
                            entity_name varchar(255),
                            entity_id bigint,
                            endpoint varchar(255),
                            ip_address varchar(255),
                            status_code int,
                            created_at timestamp
);

-- --- FOREIGN KEY TANIMLAMALARI ---

ALTER TABLE hotels ADD FOREIGN KEY (manager_id) REFERENCES users (id);
ALTER TABLE hotel_reviews ADD FOREIGN KEY (hotel_id) REFERENCES hotels (id);
ALTER TABLE hotel_reviews ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE hotel_reviews ADD FOREIGN KEY (reservation_id) REFERENCES reservations (id);
ALTER TABLE hotel_images ADD FOREIGN KEY (hotel_id) REFERENCES hotels (id);
ALTER TABLE hotel_images ADD FOREIGN KEY (category_id) REFERENCES image_categories (id);
ALTER TABLE room_types ADD FOREIGN KEY (hotel_id) REFERENCES hotels (id);
ALTER TABLE room_type_images ADD FOREIGN KEY (room_type_id) REFERENCES room_types (id);
ALTER TABLE features ADD FOREIGN KEY (category_id) REFERENCES feature_categories (id);
ALTER TABLE hotel_features ADD FOREIGN KEY (hotel_id) REFERENCES hotels (id);
ALTER TABLE hotel_features ADD FOREIGN KEY (feature_id) REFERENCES features (id);
ALTER TABLE room_type_features ADD FOREIGN KEY (room_type_id) REFERENCES room_types (id);
ALTER TABLE room_type_features ADD FOREIGN KEY (feature_id) REFERENCES features (id);
ALTER TABLE room_prices ADD FOREIGN KEY (room_type_id) REFERENCES room_types (id);
ALTER TABLE bookings ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE reservations ADD FOREIGN KEY (booking_id) REFERENCES bookings (id);
ALTER TABLE reservations ADD FOREIGN KEY (hotel_id) REFERENCES hotels (id);
ALTER TABLE reservations ADD FOREIGN KEY (room_type_id) REFERENCES room_types (id);
ALTER TABLE reservation_guests ADD FOREIGN KEY (reservation_id) REFERENCES reservations (id);
ALTER TABLE payments ADD FOREIGN KEY (booking_id) REFERENCES bookings (id);
ALTER TABLE favorites ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE favorites ADD FOREIGN KEY (hotel_id) REFERENCES hotels (id);
ALTER TABLE refresh_tokens ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE audit_logs ADD FOREIGN KEY (user_id) REFERENCES users (id);