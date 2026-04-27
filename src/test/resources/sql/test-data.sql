INSERT INTO media_type (name)
VALUES ('VHS'),
       ('DVD');

INSERT INTO movie (name, release_date, director, company, is_deleted)
VALUES ('Максимальный удар', '2017-11-30', 'Анджей Бартковяк', 'Czar Pictures', FALSE),
       ('Разборка в Маниле', '2016-02-18', 'Марк Дакаскос', 'Hollywood Storm', FALSE),
       ('Нападение на Рио Браво', '2023-01-17', 'Джо Корнет', 'Hollywood Storm', FALSE),
       ('Бабушка легкого поведения', '2017-08-17', 'Марюс Вайсберг', 'Voice Films', TRUE);

INSERT INTO client (full_name, address, phone_number, image_path, is_deleted)
VALUES ('Александр Невский', 'Лос-Анджелес, Голливуд', '+79990000001', '/images/nevsky.jpg', FALSE),
       ('Сол Гудман', 'Альбукерке, Омаха', '+79990000002', '/images/saul.jpg', FALSE),
       ('Чак Норрис', 'Техас, Навасотта', '+79990000003', '/images/chuck.jpg', TRUE);

INSERT INTO exemplar (movie_id, media_type_id, available, is_deleted)
VALUES (1, 1, TRUE, FALSE),
       (1, 2, TRUE, FALSE),
       (2, 2, TRUE, FALSE),
       (2, 1, TRUE, FALSE),
       (1, 2, TRUE, TRUE);

INSERT INTO "transaction" (exemplar_id, client_id, time, type)
VALUES (1, 1, '2026-01-10T10:00:00+00:00', 'issue'),
       (1, 1, '2026-01-12T10:00:00+00:00', 'returning'),
       (2, 1, '2026-02-01T10:00:00+00:00', 'issue'),
       (3, 2, '2026-02-05T10:00:00+00:00', 'issue'),
       (3, 2, '2026-02-10T10:00:00+00:00', 'returning');

