MERGE INTO RATING (id, name, description)
    VALUES (1, 'G', 'Фильм демонстрируется без ограничений'),
           (2, 'PG', 'Детям рекомендуется смотреть фильм с родителями'),
           (3, 'PG-13', 'Просмотр не желателен детям до 13 лет'),
           (4, 'R', 'Лица, не достигшие 17-летнего возраста, допускаются на фильм только в сопровождении одного из родителей, либо законного представителя'),
           (5, 'NC-17', 'Лица 17-летнего возраста и младше на фильм не допускаются');

MERGE INTO GENRES (id, name)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

MERGE INTO FRIENDSHIP_STATUSES (id, name, DESCRIPTION)
    VALUES (1, 'requested', 'Запрос на дружбу отправлен'),
           (2, 'approved', 'Запрос на дружбу подтвержден'),
           (3, 'rejected', 'Запрос на дружбу отклонен')