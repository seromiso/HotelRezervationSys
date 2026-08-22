-- V2__optimize_varchar_lengths.sql

-- 1. Users Tablosu Optimizasyonu
-- V2__optimize_varchar_lengths.sql

-- 1. Hotels Tablosuna Nullable Giriş/Çıkış Saatlerinin Eklenmesi ve Optimizasyonu
ALTER TABLE hotels

    ALTER COLUMN city TYPE varchar(50),
    ALTER COLUMN district TYPE varchar(50),
    ALTER COLUMN phone TYPE varchar(20),
    ALTER COLUMN iban_no TYPE varchar(34),
    ALTER COLUMN status TYPE varchar(20);

-- 2. Users Tablosu Optimizasyonu
ALTER TABLE users
    ALTER COLUMN role TYPE varchar(20),
    ALTER COLUMN status TYPE varchar(20),
    ALTER COLUMN phonenumber TYPE varchar(20);

-- 3. Room Types Tablosu Optimizasyonu
ALTER TABLE room_types
    ALTER COLUMN status TYPE varchar(20),
    ALTER COLUMN bed_configuration TYPE varchar(100);

-- 4. Bookings & Reservations Tablosu Optimizasyonu
ALTER TABLE bookings
    ALTER COLUMN booking_number TYPE varchar(50),
    ALTER COLUMN status TYPE varchar(20),
    ALTER COLUMN payment_status TYPE varchar(20);

ALTER TABLE reservations
    ALTER COLUMN status TYPE varchar(20);

-- 5. Diğer Sistem Kolonları
ALTER TABLE feature_categories ALTER COLUMN type TYPE varchar(20);
ALTER TABLE features ALTER COLUMN type TYPE varchar(20);
ALTER TABLE payments
    ALTER COLUMN payment_method TYPE varchar(30),
    ALTER COLUMN status TYPE varchar(20),
    ALTER COLUMN currency TYPE varchar(10),
    ALTER COLUMN transaction_id TYPE varchar(100);


-- V2 dosyasının en altına ekleyebilirsin:

ALTER TABLE hotels
    ADD COLUMN  IF NOT EXISTS averageRating NUMERIC(3, 1) DEFAULT 0.0;

ALTER TABLE room_types ADD COLUMN  IF NOT EXISTS description TEXT;

--otel kaydı için doküman tutma
ALTER TABLE hotels ADD COLUMN  IF NOT EXISTS license_document_url VARCHAR(255);
-- cities tablosundaki id kolonunu BIGINT (64-bit) yapıyoruz
ALTER TABLE cities ALTER COLUMN id TYPE BIGINT;

ALTER TABLE districts ALTER COLUMN city_id TYPE BIGINT;
-- Eğer ilçenin kendi ID'si de serial ise onu da düzeltebilirsin:
ALTER TABLE districts ALTER COLUMN id TYPE BIGINT;