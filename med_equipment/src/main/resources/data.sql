INSERT INTO addresses (id, city, country, street, street_number)
VALUES
     (1, 'Novi Sad', 'Serbia', 'Bulevar Mihajla Pupina', '2'),
     (2, 'Beograd', 'Serbia', 'Knez Mihailova', '13'),
     (3, 'Novi Sad', 'Serbia', 'Dunavska', '16'),
     (4, 'Beograd', 'Serbia', 'Karađorđeva', '15'),
     (5, 'Beograd', 'Serbia', 'Terazije', '16'),
     (6, 'Novi Sad', 'Serbia', 'Zmaj Jovina', '2'),
     (7, 'Novi Pazar', 'Serbia', 'Vuka Karadžića', '5'),
     (8, 'Smederevo', 'Serbia', 'Karađorđeva', '33'),
     (9, 'Kraljevo', 'Serbia', 'Stefana Nemanje', '24'),
     (10, 'Kraljevo', 'Serbia', 'Kralja Petra', '12'),
     (11, 'Niš', 'Serbia', 'Obrenovićeva', '55');

INSERT INTO users (id, email, enabled, name, password, phone_number, surname)
VALUES
    (1, 'user@gmail.com', true, 'Nina', '$2a$10$2iKltlyS7qxZPpFEub2U5OsjaPHTLhLYiB8OqLoK5X4mSNPr.emwS', '0628817058', 'Bu'), -- password = user
    (2, 'company.admin@gmail.com', true, 'Petar', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '123456789', 'Kovačević'), -- password = admin
    (3, 'system.admin@gmail.com', true, 'Vuk', '$2a$10$h9fD5LSSJ/cxG6pysikeOes5ANhA05FDyYi1tiw0mlSEYk8AKQC12', '987654321', 'Pavlović'), -- password = admin
    (4, 'anas@gmail.com', false, 'Anastasija', '$2a$10$3EA5QPLs153HVBYfZrSvi.vLEAaESeL.cPEBbrZerRMN9RvY.ev9m', '5551234567', 'Marsenić'); -- password = anas

INSERT INTO companies (id, name, rating, address_id, description)
VALUES
    (1, 'MediQuip', 9.5, 2, 'A test company with a good rating.'),
    (2, 'Health Innovations', 5.8, 3, 'Another test company with a moderate rating.'),
    (3, 'Wellness Solutions', 2.5, 4, 'A test company with a lower rating.'),
    (4, 'MedTech Solutions', 8.0, 5, 'A test company with a higher rating.'),
    (5, 'LifeCare Services', 6.2, 6, 'A test company with an average rating.'),
    (6, 'Advanced Health', 4.8, 7, 'A test company with a below-average rating.'),
    (7, 'Elite Medical Group', 9.7, 8, 'A test company with an excellent rating.'),
    (8, 'Precision Health', 8.5, 9, 'A test company with a decent rating.'),
    (9, 'Vitality Care', 3.0, 10, 'A test company with a very low rating.'),
    (10, 'WellBeing Solutions', 8.2, 11, 'A test company with a good rating.');

INSERT INTO equipment (id, name, description, price, rating, type)
VALUES
    (1, 'MRI Machine', 'State-of-the-art MRI equipment for accurate diagnostics', 500000.0, 4.8, 0),
    (2, 'X-ray Machine', 'High-resolution X-ray machine for detailed imaging', 250000.0, 4.5, 1),
    (3, 'Ultrasound Scanner', 'Advanced ultrasound scanner for versatile medical imaging', 150000.0, 4.2, 2),
    (4, 'CT Scanner', 'Cutting-edge CT scanner for detailed cross-sectional images', 800000.0, 4.9, 0),
    (5, 'Blood Pressure Monitor', 'Precision blood pressure monitor for accurate readings', 500.0, 4.0, 1),
    (6, 'Patient Monitor', 'Continuous patient monitoring system for vital signs', 1200.0, 3.5, 2),
    (7, 'Dental Chair', 'Ergonomic dental chair for comfortable patient care', 3000.0, 4.7, 0),
    (8, 'Surgical Robot', 'Advanced surgical robot for minimally invasive procedures', 1000000.0, 4.9, 1),
    (9, 'Laboratory Centrifuge', 'High-speed laboratory centrifuge for efficient sample separation', 2000.0, 4.1, 2),
    (10, 'Defibrillator', 'Life-saving defibrillator for emergency cardiac care', 5000.0, 4.6, 1);

INSERT INTO registered_users (id, company_info, occupation, penalty_points, address_id)
VALUES
    (1, 'FTN', 'Programer', 0, 1);

INSERT INTO company_admins (id, company_id)
VALUES
    (2, 1);

INSERT INTO system_admins (id)
VALUES
    (3);

INSERT INTO company_equipment (company_id, equipment_id)
VALUES
    (1, 1),
    (1, 3),
    (2, 2),
    (3, 5),
    (3, 6),
    (4, 4),
    (4, 7),
    (5, 8),
    (5, 9),
    (6, 10),
    (7, 1),
    (7, 4),
    (8, 5),
    (8, 7),
    (9, 2),
    (10, 3),
    (10, 8);
