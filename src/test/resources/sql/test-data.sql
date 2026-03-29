INSERT INTO media_type (id, name)
VALUES (1, 'VHS'),
       (2, 'DVD');

INSERT INTO movie (id, name, release_date, director, company, is_deleted)
VALUES (1, 'Максимальный удар', '2017-11-30', 'Анджей Бартковяк', 'Czar Pictures', FALSE),
       (2, 'Разборка в Маниле', '2016-02-18', 'Марк Дакаскос', 'Hollywood Storm', FALSE),
       (3, 'Нападение на Рио Браво', '2023-01-17', 'Джо Корнет', 'Hollywood Storm', FALSE),
       (4, 'Бабушка легкого поведения', '2017-08-17', 'Марюс Вайсберг', 'Voice Films', TRUE);

INSERT INTO client (id, full_name, address, phone_number, image_path, is_deleted)
VALUES (1, 'Александр Невский', 'Лос-Анджелес, Голливуд', '+79990000001', '/images/nevsky.jpg', FALSE),
       (2, 'Сол Гудман', 'Альбукерке, Омаха', '+79990000002', '/images/saul.jpg', FALSE),
       (3, 'Чак Норрис', 'Техас, Навасотта', '+79990000003', '/images/chuck.jpg', TRUE);

INSERT INTO exemplar (id, movie_id, media_type_id, available, is_deleted)
VALUES (1, 1, 1, TRUE, FALSE),
       (2, 1, 2, TRUE, FALSE),
       (3, 2, 2, TRUE, FALSE),
       (4, 2, 1, TRUE, FALSE),
       (5, 1, 2, TRUE, TRUE);

INSERT INTO "transaction" (id, exemplar_id, client_id, time, type)
VALUES (1, 1, 1, '2026-01-10T10:00:00+00:00', 'issue'),
       (2, 1, 1, '2026-01-12T10:00:00+00:00', 'returning'),
       (3, 2, 1, '2026-02-01T10:00:00+00:00', 'issue'),
       (4, 3, 2, '2026-02-05T10:00:00+00:00', 'issue'),
       (5, 3, 2, '2026-02-10T10:00:00+00:00', 'returning');

