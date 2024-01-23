INSERT INTO addresses (city, country, street, street_number)
VALUES
    ('Novi Sad', 'Serbia', 'Bulevar Mihajla Pupina', '2'),
    ('Beograd', 'Serbia', 'Knez Mihailova', '13'),
    ('Novi Sad', 'Serbia', 'Dunavska', '16'),
    ('Beograd', 'Serbia', 'Karađorđeva', '15'),
    ('Beograd', 'Serbia', 'Terazije', '16'),
    ('Novi Sad', 'Serbia', 'Zmaj Jovina', '2'),
    ('Novi Pazar', 'Serbia', 'Vuka Karadžića', '5'),
    ('Smederevo', 'Serbia', 'Karađorđeva', '33'),
    ('Kraljevo', 'Serbia', 'Stefana Nemanje', '24'),
    ('Kraljevo', 'Serbia', 'Kralja Petra', '12'),
    ('Niš', 'Serbia', 'Obrenovićeva', '55'),
    ('Novi Sad', 'Serbia', 'Trg Slobode', '1'),
    ('Novi Sad', 'Serbia', 'Cara Dusana', '1');

INSERT INTO users (email, enabled, name, password, phone_number, surname, version)
VALUES
    ('ninabu16@gmail.com', true, 'Nina', '$2a$10$2iKltlyS7qxZPpFEub2U5OsjaPHTLhLYiB8OqLoK5X4mSNPr.emwS', '0628817058', 'Bu', 0), -- password = user
    ('company.admin@gmail.com', true, 'Petar', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Kovacevic', 0), -- password = admin
    ('djeka@gmail.com', true, 'Stefan', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '062555333', 'Djekic', 0),
    ('savic@gmail.com', true, 'Nikola', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '065011509', 'Savic', 0),
    ('djuka@gmail.com', true, 'Luka', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '063120501', 'Djukanovic', 0),
    ('morzy@gmail.com', true, 'Boris', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '064040801', 'Marinkovic', 0),
    ('crvena@gmail.com', true, 'Sinisa', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '062999222', 'Mozetic', 0),
    ('moravac@gmail.com', true, 'Vukasin', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '063011011', 'Moravcevic', 0),
    ('uros.prejaki@gmail.com', true, 'Uros', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '065015015', 'Makevic', 0),
    ('shkodra@gmail.com', true, 'Drasko', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '064343434', 'Begus', 0),
    ('dovlaa@gmail.com', true, 'Vladimir', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '063321321', 'Ninkovic', 0),
    ('shishko@gmail.com', true, 'Sisko', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '063242424', 'Mencetic', 0),
    ('system.admin@gmail.com', true, 'Vuk', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '062578547', 'Pavlovic', 0), -- password = admin
    ('anastasijamarsenic@gmail.com', true, 'Anastasija', '$2a$10$3EA5QPLs153HVBYfZrSvi.vLEAaESeL.cPEBbrZerRMN9RvY.ev9m', '0631234567', 'Marsenic', 0), -- password = anas
    ('vuk.pavlovic912@gmail.com', true, 'Vuk', '$2a$10$2iKltlyS7qxZPpFEub2U5OsjaPHTLhLYiB8OqLoK5X4mSNPr.emwS', '062578547', 'Pavlovic', 0); -- password = user

INSERT INTO companies (name, rating, address_id, description, version)
VALUES
    ('MediQuip', 4.5, 2, 'A test company with a good rating.', 0),
    ('Health Innovations', 3.8, 3, 'Another test company with a moderate rating.', 0),
    ('Wellness Solutions', 2.5, 4, 'A test company with a lower rating.', 0),
    ('MedTech Solutions', 4.0, 5, 'A test company with a higher rating.', 0),
    ('LifeCare Services', 4.2, 6, 'A test company with an average rating.', 0),
    ('Advanced Health', 2.8, 7, 'A test company with a below-average rating.', 0),
    ('Elite Medical Group', 4.7, 8, 'A test company with an excellent rating.', 0),
    ('Precision Health', 4.5, 9, 'A test company with a decent rating.', 0),
    ('Vitality Care', 2.0, 10, 'A test company with a very low rating.', 0),
    ('WellBeing Solutions', 4.2, 11, 'A test company with a good rating.', 0);

INSERT INTO equipment (name, description, price, rating, type, version)
VALUES
    ('MRI Machine', 'State-of-the-art MRI equipment for accurate diagnostics', 500000.0, 4.8, 0, 0),
    ('X-ray Machine', 'High-resolution X-ray machine for detailed imaging', 250000.0, 4.5, 1, 0),
    ('Ultrasound Scanner', 'Advanced ultrasound scanner for versatile medical imaging', 150000.0, 4.2, 2, 0),
    ('CT Scanner', 'Cutting-edge CT scanner for detailed cross-sectional images', 800000.0, 4.9, 0, 0),
    ('Blood Pressure Monitor', 'Precision blood pressure monitor for accurate readings', 500.0, 4.0, 1, 0),
    ('Patient Monitor', 'Continuous patient monitoring system for vital signs', 1200.0, 3.5, 2, 0),
    ('Dental Chair', 'Ergonomic dental chair for comfortable patient care', 3000.0, 4.7, 0, 0),
    ('Surgical Robot', 'Advanced surgical robot for minimally invasive procedures', 1000000.0, 4.9, 1, 0),
    ('Laboratory Centrifuge', 'High-speed laboratory centrifuge for efficient sample separation', 2000.0, 4.1, 2, 0),
    ('Defibrillator', 'Life-saving defibrillator for emergency cardiac care', 5000.0, 4.6, 1, 0);

INSERT INTO registered_users (id, company_info, occupation, penalty_points, address_id)
VALUES
    (1, 'FTN', 'Programer', 0, 1),
    (14, 'FTN', 'Programer', 0, 12),
    (15, 'FTN', 'Programer', 0, 13);

INSERT INTO company_admins (id, company_id, has_changed_password)
VALUES
    (2, 1, false),
    (3, 1, false),
    (4, 2, false),
    (5, 3, false),
    (6, 4, false),
    (7, 5, false),
    (8, 6, false),
    (9, 7, false),
    (10, 8, false),
    (11, 9, false),
    (12, 10, false);

INSERT INTO system_admins (id, has_changed_password)
VALUES
    (13, false);

INSERT INTO company_equipment (company_id, equipment_id, quantity)
VALUES
    (1, 1, 2),
    (1, 3, 5),
    (2, 2, 3),
    (3, 5, 1),
    (3, 6, 2),
    (4, 4, 1),
    (4, 7, 3),
    (5, 8, 4),
    (5, 9, 2),
    (6, 10, 1),
    (7, 1, 2),
    (7, 4, 1),
    (8, 5, 3),
    (8, 7, 2),
    (9, 2, 1),
    (10, 3, 3),
    (10, 8, 2);

INSERT INTO calendars (company_id, work_start_time, work_end_time, works_on_weekends, version)
VALUES
    (1, '08:30:00', '17:30:00', true, 0),  -- MediQuip
    (2, '09:00:00', '18:00:00', false, 0), -- Health Innovations
    (3, '08:45:00', '17:15:00', true, 0),  -- Wellness Solutions
    (4, '08:30:00', '17:30:00', false, 0), -- MedTech Solutions
    (5, '09:15:00', '18:15:00', true, 0),  -- LifeCare Services
    (6, '09:00:00', '18:00:00', false, 0), -- Advanced Health
    (7, '08:45:00', '17:15:00', true, 0),  -- Elite Medical Group
    (8, '09:30:00', '18:30:00', false, 0), -- Precision Health
    (9, '08:30:00', '17:30:00', true, 0),  -- Vitality Care
    (10, '09:15:00', '18:15:00', false, 0); -- WellBeing Solutions

INSERT INTO time_slots (start, is_free, calendar_id, admin_id, version)
VALUES
    (CURRENT_DATE + INTERVAL '08:30:00' + INTERVAL '1 day', true, 1, 2, 0),
    (CURRENT_DATE + INTERVAL '09:15:00' + INTERVAL '1 day', true, 1, 2, 0),
    (CURRENT_DATE + INTERVAL '12:00:00' + INTERVAL '1 day', true, 1, 2, 0),
    (CURRENT_DATE + INTERVAL '14:30:00' + INTERVAL '1 day', true, 1, 2, 0),
    (CURRENT_DATE + INTERVAL '08:45:00' + INTERVAL '2 day', true, 1, 2, 0),
    (CURRENT_DATE + INTERVAL '10:30:00' + INTERVAL '2 day', true, 1, 2, 0),
    (CURRENT_DATE + INTERVAL '12:15:00' + INTERVAL '2 day', true, 1, 2, 0),
    (CURRENT_DATE + INTERVAL '16:00:00' + INTERVAL '2 day', true, 1, 2, 0),
    (CURRENT_DATE + INTERVAL '09:45:00' + INTERVAL '3 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '13:30:00' + INTERVAL '3 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '15:30:00' + INTERVAL '3 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '12:30:00' + INTERVAL '3 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '09:15:00' + INTERVAL '4 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '13:45:00' + INTERVAL '4 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '15:30:00' + INTERVAL '4 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '12:45:00' + INTERVAL '5 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '16:00:00' + INTERVAL '5 day', true, 1, 2, 0),
    (CURRENT_DATE + INTERVAL '09:45:00' + INTERVAL '5 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '13:30:00' + INTERVAL '5 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '15:30:00' + INTERVAL '6 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '12:30:00' + INTERVAL '6 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '09:15:00' + INTERVAL '7 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '13:45:00' + INTERVAL '7 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '13:45:00' + INTERVAL '1 day', true, 3, 5, 0),
    (CURRENT_DATE + INTERVAL '13:45:00' + INTERVAL '2 day', true, 3, 5, 0);

