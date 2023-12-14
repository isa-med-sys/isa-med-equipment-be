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
     ('Niš', 'Serbia', 'Obrenovićeva', '55');

INSERT INTO users (email, enabled, name, password, phone_number, surname)
VALUES
    ('ninabu16@gmail.com', true, 'Nina', '$2a$10$2iKltlyS7qxZPpFEub2U5OsjaPHTLhLYiB8OqLoK5X4mSNPr.emwS', '0628817058', 'Bu'), -- password = user
    ('company.admin@gmail.com', true, 'Petar', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Kovacevic'), -- password = admin
    ('john.doe@example.com', true, 'John', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Doe'),
    ('emma.smith@example.com', true, 'Emma', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Smith'),
    ('alex.jones@example.com', true, 'Alex', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Jones'),
    ('sara.wilson@example.com', true, 'Sara', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Wilson'),
    ('michael.brown@example.com', true, 'Michael', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Brown'),
    ('olivia.martin@example.com', true, 'Olivia', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Martin'),
    ('david.wang@example.com', true, 'David', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Wang'),
    ('jessica.nguyen@example.com', true, 'Jessica', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Nguyen'),
    ('william.jackson@example.com', true, 'William', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Jackson'),
    ('mia.chen@example.com', true, 'Mia', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Chen'),
    ('system.admin@gmail.com', true, 'Vuk', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '987654321', 'Pavlovic'), -- password = admin
    ('anas@gmail.com', false, 'Anastasija', '$2a$10$3EA5QPLs153HVBYfZrSvi.vLEAaESeL.cPEBbrZerRMN9RvY.ev9m', '5551234567', 'Marsenic'); -- password = anas

INSERT INTO companies (name, rating, address_id, description)
VALUES
    ('MediQuip', 4.5, 2, 'A test company with a good rating.'),
    ('Health Innovations', 3.8, 3, 'Another test company with a moderate rating.'),
    ('Wellness Solutions', 2.5, 4, 'A test company with a lower rating.'),
    ('MedTech Solutions', 4.0, 5, 'A test company with a higher rating.'),
    ('LifeCare Services', 4.2, 6, 'A test company with an average rating.'),
    ('Advanced Health', 2.8, 7, 'A test company with a below-average rating.'),
    ('Elite Medical Group', 4.7, 8, 'A test company with an excellent rating.'),
    ('Precision Health', 4.5, 9, 'A test company with a decent rating.'),
    ('Vitality Care', 2.0, 10, 'A test company with a very low rating.'),
    ('WellBeing Solutions', 4.2, 11, 'A test company with a good rating.');

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
    (1, 'FTN', 'Programer', 0, 1);

INSERT INTO company_admins (id, company_id)
VALUES
    (2, 1),
    (3, 1),
    (4, 2),
    (5, 3),
    (6, 4),
    (7, 5),
    (8, 6),
    (9, 7),
    (10, 8),
    (11, 9),
    (12, 10);

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
    (CURRENT_DATE + INTERVAL '09:30:00' + INTERVAL '2 day', true, 1, 2, 0),
    (CURRENT_DATE + INTERVAL '12:15:00' + INTERVAL '2 day', true, 1, 2, 0),
    (CURRENT_DATE + INTERVAL '15:00:00' + INTERVAL '2 day', true, 1, 2, 0),
    (CURRENT_DATE + INTERVAL '08:45:00' + INTERVAL '1 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '13:30:00' + INTERVAL '1 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '14:15:00' + INTERVAL '1 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '12:30:00' + INTERVAL '1 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '09:00:00' + INTERVAL '2 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '13:45:00' + INTERVAL '2 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '14:30:00' + INTERVAL '2 day', true, 1, 3, 0),
    (CURRENT_DATE + INTERVAL '12:45:00' + INTERVAL '2 day', true, 1, 3, 0);

