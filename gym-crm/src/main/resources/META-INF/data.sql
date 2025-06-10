CREATE SCHEMA IF NOT EXISTS GYM_CRM;
-- Insert Training Types
INSERT INTO training_type (id, name) VALUES (1, 'STRENGTH_TRAINING');
INSERT INTO training_type (id, name) VALUES (2, 'ENDURANCE_TRAINING');
INSERT INTO training_type (id, name) VALUES (3, 'CARDIO_TRAINING');
INSERT INTO training_type (id, name) VALUES (4, 'BODY_WEIGHT_TRAINING');

-- Insert Users (for Trainers and Trainees)
INSERT INTO users (firstname, lastname, username, password, is_active) VALUES ('Olim', 'Karimov', 'olim.karimov', '123456', true);
INSERT INTO users (firstname, lastname, username, password, is_active) VALUES ('Sardor', 'Tursunov', 'sardor.tursunov', 'password2', true);
INSERT INTO users (firstname, lastname, username, password, is_active) VALUES ('Bekzod', 'Mahmudov', 'bekzod.mahmudov', 'password3', true);
INSERT INTO users (firstname, lastname, username, password, is_active) VALUES ('Jasur', 'Norboyev', 'jasur.norboyev', 'password4', true);
INSERT INTO users (firstname, lastname, username, password, is_active) VALUES ('Alisher', 'Qodirov', 'alisher.qodirov', 'password5', true);
INSERT INTO users (firstname, lastname, username, password, is_active) VALUES ('Aziz', 'Murodov', 'aziz.murodov', '12345', true);
INSERT INTO users (firstname, lastname, username, password, is_active) VALUES ('Javohir', 'Sattorov', 'javohir.sattorov', 'password7', true);
INSERT INTO users (firstname, lastname, username, password, is_active) VALUES ('Rustam', 'Ergashev', 'rustam.ergashev', 'password8', true);
INSERT INTO users (firstname, lastname, username, password, is_active) VALUES ('Shahzod', 'Islomov', 'shahzod.islomov', 'password9', true);
INSERT INTO users (firstname, lastname, username, password, is_active) VALUES ('Dilshod', 'Sobirov', 'dilshod.sobirov', '12345', true);

-- Insert Trainers
INSERT INTO trainer (user_id, specialization_id) VALUES (1, 1);
INSERT INTO trainer (user_id, specialization_id) VALUES (2, 2);
INSERT INTO trainer (user_id, specialization_id) VALUES (3, 3);
INSERT INTO trainer (user_id, specialization_id) VALUES (4, 4);
INSERT INTO trainer (user_id, specialization_id) VALUES (5, 4);

-- -- Insert Trainees
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (6, '2000-01-01', 'Tashkent');
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (7, '2000-01-01', 'Samarkand');
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (8, '2000-01-01', 'Bukhara');
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (9, '2000-01-01', 'Khorezm');
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (10, '2000-01-01', 'Fergana');
-- -- Trainings
INSERT INTO training (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration) VALUES (1, 2, 'Wrestling Training', 1, CURRENT_DATE, 60);
INSERT INTO training (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration) VALUES (2, 3, 'Football Preparation', 2, CURRENT_DATE, 90);
INSERT INTO training (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration) VALUES (3, 4, 'Swimming Lesson', 3, CURRENT_DATE, 120);
INSERT INTO training (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration) VALUES (4, 5, 'Running Techniques', 4, CURRENT_DATE, 150);
INSERT INTO training (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration) VALUES (5, 1, 'Judo Practical Training', 1, CURRENT_DATE, 180);
