-- 1. BAĞIMSIZ TABLOLAR (En alt seviye)
INSERT INTO users (id, email, password_hash, name, surname, role, datebirth, phonenumber, email_verified, status, created_at) VALUES
(1, 'manager@hotel.com', 'hashed_pw_1', 'Ahmet', 'Yılmaz', 'MANAGER', '1985-05-15', '5551112233', true, 'ACTIVE', CURRENT_TIMESTAMP),
(2, 'customer1@gmail.com', 'hashed_pw_2', 'Ayşe', 'Kaya', 'CUSTOMER', '1992-08-22', '5554445566', true, 'ACTIVE', CURRENT_TIMESTAMP),
(3, 'customer2@yahoo.com', 'hashed_pw_3', 'Mehmet', 'Demir', 'CUSTOMER', '1988-11-10', '5557778899', true, 'ACTIVE', CURRENT_TIMESTAMP);

INSERT INTO image_categories (id, name, created_at) VALUES
(1, 'Dış Mekan', CURRENT_TIMESTAMP),
(2, 'Oda İçi', CURRENT_TIMESTAMP);

INSERT INTO feature_categories (id, name, type) VALUES
(1, 'Otel Olanakları', 'HOTEL'),
(2, 'Oda Olanakları', 'ROOM');

-- 2. YARI BAĞIMLI TABLOLAR
INSERT INTO features (id, category_id, name, type) VALUES
(1, 1, 'Açık Havuz', 'HOTEL'),
(2, 1, 'Ücretsiz Otopark', 'HOTEL'),
(3, 2, 'Klima', 'ROOM'),
(4, 2, 'Minibar', 'ROOM');

INSERT INTO hotels (id, manager_id, name, city, district, address, phone, description, commission_rate, iban_no, status, check_in_time, check_out_time, created_at) VALUES
(1, 1, 'Bosphorus Palace', 'İstanbul', 'Beşiktaş', 'Sahil Yolu No:1', '2121112233', 'Boğaz manzaralı lüks otel', 15.00, 'TR123456789012345678901234', 'ACTIVE', '14:00', '12:00', CURRENT_TIMESTAMP),
(2, 1, 'Aegean Resort', 'İzmir', 'Çeşme', 'Ilıca Mah. No:5', '2324445566', 'Denize sıfır tatil köyü', 12.50, 'TR987654321098765432109876', 'ACTIVE', '15:00', '11:00', CURRENT_TIMESTAMP);

-- 3. OTELLERE BAĞLI TABLOLAR
INSERT INTO hotel_features (hotel_id, feature_id) VALUES
(1, 1), (1, 2), (2, 1);

INSERT INTO hotel_images (id, hotel_id, category_id, image_url, display_order, created_at) VALUES
(1, 1, 1, 'https://example.com/bosphorus1.jpg', 1, CURRENT_TIMESTAMP),
(2, 2, 1, 'https://example.com/aegean1.jpg', 1, CURRENT_TIMESTAMP);

INSERT INTO room_types (id, hotel_id, title, max_adults, max_children, base_capacity, bed_configuration, total_inventory, status, created_at) VALUES
(1, 1, 'Standart Deniz Manzaralı', 2, 1, 2, '1 Çift Kişilik Yatak', 10, 'ACTIVE', CURRENT_TIMESTAMP),
(2, 1, 'Kral Dairesi', 4, 2, 4, '2 Çift Kişilik Yatak', 2, 'ACTIVE', CURRENT_TIMESTAMP),
(3, 2, 'Standart Doğa Manzaralı', 2, 1, 2, '2 Tek Kişilik Yatak', 20, 'ACTIVE', CURRENT_TIMESTAMP);

-- 4. ODALARA BAĞLI TABLOLAR
INSERT INTO room_type_features (id, room_type_id, feature_id, created_at) VALUES
(1, 1, 3, CURRENT_TIMESTAMP),
(2, 1, 4, CURRENT_TIMESTAMP),
(3, 2, 3, CURRENT_TIMESTAMP),
(4, 2, 4, CURRENT_TIMESTAMP),
(5, 3, 3, CURRENT_TIMESTAMP);

INSERT INTO room_type_images (id, room_type_id, image_url, display_order, created_at) VALUES
(1, 1, 'https://example.com/room1.jpg', 1, CURRENT_TIMESTAMP),
(2, 2, 'https://example.com/room2.jpg', 1, CURRENT_TIMESTAMP);

INSERT INTO room_prices (id, room_type_id, currency, start_date, end_date, price_per_night, created_at) VALUES
(1, 1, 'TRY', '2026-06-01', '2026-09-01', 2500.00, CURRENT_TIMESTAMP),
(2, 2, 'TRY', '2026-06-01', '2026-09-01', 8500.00, CURRENT_TIMESTAMP),
(3, 3, 'TRY', '2026-06-01', '2026-09-01', 1800.00, CURRENT_TIMESTAMP);

-- 5. REZERVASYON VE SATIŞ TABLOLARI
INSERT INTO bookings (id, booking_number, user_id, total_amount, status, payment_status, created_at) VALUES
(1, 'BKG-2026-001', 2, 5000.00, 'CONFIRMED', 'PAID', CURRENT_TIMESTAMP),
(2, 'BKG-2026-002', 3, 8500.00, 'PENDING', 'UNPAID', CURRENT_TIMESTAMP);

INSERT INTO reservations (id, booking_id, hotel_id, room_type_id, check_in_date, check_out_date, adult_count, child_count, price_per_night, total_price, status, created_at) VALUES
(1, 1, 1, 1, '2026-07-10', '2026-07-12', 2, 0, 2500.00, 5000.00, 'CONFIRMED', CURRENT_TIMESTAMP),
(2, 2, 1, 2, '2026-08-05', '2026-08-06', 2, 2, 8500.00, 8500.00, 'PENDING', CURRENT_TIMESTAMP);

INSERT INTO reservation_guests (id, reservation_id, name, surname, guest_type, is_primary_guest, created_at) VALUES
(1, 1, 'Ayşe', 'Kaya', 'ADULT', true, CURRENT_TIMESTAMP),
(2, 1, 'Ali', 'Kaya', 'ADULT', false, CURRENT_TIMESTAMP),
(3, 2, 'Mehmet', 'Demir', 'ADULT', true, CURRENT_TIMESTAMP);

INSERT INTO payments (id, booking_id, amount, payment_method, status, currency, transaction_id, created_at) VALUES
(1, 1, 5000.00, 'CREDIT_CARD', 'COMPLETED', 'TRY', 'TRX-9988776655', CURRENT_TIMESTAMP);

-- 6. DİĞER KULLANICI İŞLEMLERİ (Yorumlar, Favoriler, Loglar)
INSERT INTO hotel_reviews (id, hotel_id, user_id, reservation_id, rating, comment, created_at) VALUES
(1, 1, 2, 1, 5, 'Muhteşem bir boğaz manzarası, çalışanlar çok ilgiliydi.', CURRENT_TIMESTAMP);

INSERT INTO favorites (id, user_id, hotel_id, created_at) VALUES
(1, 2, 2, CURRENT_TIMESTAMP),
(2, 3, 1, CURRENT_TIMESTAMP);

INSERT INTO refresh_tokens (id, user_id, token_hash, expires_at, created_at) VALUES
(1, 2, 'random_token_abc123', '2026-12-31 23:59:59', CURRENT_TIMESTAMP);

INSERT INTO audit_logs (id, user_id, action, entity_name, entity_id, endpoint, ip_address, status_code, created_at) VALUES
(1, 2, 'CREATE', 'BOOKING', 1, '/api/v1/bookings', '192.168.1.1', 201, CURRENT_TIMESTAMP);

-- ID sayaçlarını güncelliyoruz (Böylece Java'dan yeni kayıt eklerken ID çakışması hatası almazsın)
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('hotels_id_seq', (SELECT MAX(id) FROM hotels));
SELECT setval('room_types_id_seq', (SELECT MAX(id) FROM room_types));
SELECT setval('bookings_id_seq', (SELECT MAX(id) FROM bookings));