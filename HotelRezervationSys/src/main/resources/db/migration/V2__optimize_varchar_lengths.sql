-- V2__optimize_varchar_lengths.sql

-- 1. Users Tablosu Optimizasyonu
ALTER TABLE users
    ALTER COLUMN role TYPE varchar(20),
    ALTER COLUMN status TYPE varchar(20),
    ALTER COLUMN phonenumber TYPE varchar(20);

-- 2. Hotels Tablosu Optimizasyonu
ALTER TABLE hotels
    ALTER COLUMN city TYPE varchar(50),
    ALTER COLUMN district TYPE varchar(50),
    ALTER COLUMN phone TYPE varchar(20),
    ALTER COLUMN iban_no TYPE varchar(34), -- TR IBAN'ları standart 26 karakterdir, esneklik payıyla 34 idealdir
    ALTER COLUMN status TYPE varchar(20);

-- 3. Room Types Tablosu Optimizasyonu
ALTER TABLE room_types
    ALTER COLUMN status TYPE varchar(20),
    ALTER COLUMN bed_configuration TYPE varchar(100);

-- 4. Bookings & Reservations Tablosu Optimizasyonu
ALTER TABLE bookings
    ALTER COLUMN booking_number TYPE varchar(50),
    ALTER COLUMN status TYPE varchar(20),
    ALTER COLUMN payment_status TYPE varchar(20);



-- 5. Diğer Sistem Kolonları
ALTER TABLE feature_categories ALTER COLUMN type TYPE varchar(20);
ALTER TABLE features ALTER COLUMN type TYPE varchar(20);
ALTER TABLE payments
    ALTER COLUMN payment_method TYPE varchar(30),
    ALTER COLUMN status TYPE varchar(20),
    ALTER COLUMN currency TYPE varchar(10), -- TRY, USD, EUR için 10 fazlasıyla yeterli
    ALTER COLUMN transaction_id TYPE varchar(100);