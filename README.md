# java-filmorate
Template repository for Filmorate project.
![scheme.](DBFilmorate.jpg)
Примеры запросов:
1. Добавление нового пользователя

insert into users (email, login, name, birthday) values ('email@mail.ru', 'login', 'name', 1987-10-10);

2. Получить список id всех пользователей:
   
select id from users;

3. Получить все данные по пользователю c id=1:

select * from users where id = 1;

4. Получить список id друзей пользователя с id=1:

select friend_id from friendship where user_id = 1;

5. Добавить друга / удалить из друзей:

insert into friendship (user_id, friend_id) values (1, 2);

Delete from friendship where user_id = 1 and friend_id = 2;

6. Добавить фильм:

insert into films (id, name, description, release_date, duration, id_mpa)
Values (1, 'name', 'description', 1995-5-5, 120, 1);

7. Добавить жанры для фильма:

insert into genres_films (genre_id, film_id) values (1, 1);

8. Обновить фильм:

update films set name = 'newname', description = 'newDescription', release_date = 2000-5-5,
duration = 120, id_mpa = 5 where id = 1;

9. Список 10 самых популярных фильмов:

SELECT f.id, count(fu.ID_USER)
FROM FILMS f JOIN FILMS_USERS fu oN f.id = fu.ID_FILM
GROUP BY f.NAME
ORDER BY COUNT(fu.ID_USER) DESC LIMIT 10;