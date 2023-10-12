MERGE INTO genre (genre_id, name) VALUES (1, 'Комедия'),
                                         (2, 'Драма'),
                                         (3, 'Мультфильм'),
                                         (4, 'Триллер'),
                                         (5, 'Документальный'),
                                         (6, 'Боевик');

MERGE INTO mpa (mpa_id, name) VALUES (1, 'G'),
                                     (2, 'PG'),
                                     (3, 'PG-13'),
                                     (4, 'R'),
                                     (5, 'NC-17');

--INSERT INTO film (name, description, release_date, duration, mpa_id)
--VALUES ('Фильм 1', 'Описание фильма 1', '2001-01-01', 90, 1);

--INSERT INTO users (name, login, email, birthday)
--VALUES ('Иван 1', 'Логин иван 1', 'иван1@mail.ru', '2001-01-01');