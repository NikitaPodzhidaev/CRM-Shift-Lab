TRUNCATE TABLE transactions, sellers RESTART IDENTITY CASCADE;

INSERT INTO sellers (name, contact_info, registration_date, deleted)
VALUES
    ('Иван Петров', 'ivan.petrov@example.com', '2026-05-01T09:15:00', false),
    ('Анна Смирнова', 'anna.smirnova@example.com', '2026-05-01T10:30:00', false),
    ('Олег Иванов', 'oleg.ivanov@example.com', '2026-05-02T11:45:00', false),
    ('Мария Кузнецова', 'maria.kuznetsova@example.com', '2026-05-02T14:20:00', false),
    ('Дмитрий Соколов', 'dmitry.sokolov@example.com', '2026-05-03T08:50:00', false),
    ('Елена Морозова', 'elena.morozova@example.com', '2026-05-03T16:10:00', false),
    ('Алексей Волков', 'alexey.volkov@example.com', '2026-05-04T12:05:00', false),
    ('Наталья Орлова', 'natalia.orlova@example.com', '2026-05-04T17:40:00', false),
    ('Сергей Никитин', 'sergey.nikitin@example.com', '2026-05-05T09:00:00', false),
    ('Ксения Федорова', 'ksenia.fedorova@example.com', '2026-05-05T13:25:00', false),
    ('Джон Уик', 'john.wick@example.com', '2026-05-06T12:00:00', false);

INSERT INTO transactions (seller_id, amount, payment_type, transaction_date)
VALUES
    (1, 1500.00, 'CARD', '2026-05-01T10:15:00'),
    (1, 2300.50, 'CASH', '2026-05-01T14:30:00'),
    (2, 5000.00, 'TRANSFER', '2026-05-01T16:45:00'),
    (2, 1200.00, 'CARD', '2026-05-02T09:20:00'),
    (3, 750.00, 'CASH', '2026-05-02T11:10:00'),
    (3, 3100.00, 'CARD', '2026-05-03T13:25:00'),
    (4, 8700.00, 'TRANSFER', '2026-05-03T17:40:00'),
    (4, 1600.00, 'CARD', '2026-05-04T08:55:00'),
    (5, 4200.00, 'CASH', '2026-05-04T12:00:00'),
    (5, 9900.00, 'TRANSFER', '2026-05-05T18:15:00'),
    (6, 2500.00, 'CARD', '2026-04-10T10:00:00'),
    (6, 3000.00, 'CASH', '2026-04-15T15:30:00'),
    (7, 11000.00, 'TRANSFER', '2026-04-20T09:45:00'),
    (7, 4500.00, 'CARD', '2026-04-25T14:10:00'),
    (8, 800.00, 'CASH', '2026-03-05T11:35:00'),
    (8, 6700.00, 'CARD', '2026-03-18T16:20:00'),
    (9, 12500.00, 'TRANSFER', '2026-02-12T10:50:00'),
    (9, 2200.00, 'CARD', '2026-06-01T09:00:00'),
    (10, 3400.00, 'CASH', '2026-06-10T13:45:00'),
    (10, 7800.00, 'TRANSFER', '2026-07-01T12:30:00');