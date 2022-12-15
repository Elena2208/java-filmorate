# java-filmorate
Template repository for Filmorate project.

![Модель базы данных](filmorate.png)

Примеры запросов к БД:

Получить пользователя по идентефикатоу
SELECT * FROM users WHERE user_id = ?

Получить фильм по идентификатору
Select * from films where film_id=?

Удалить фильм 
delete from films where film_id=?

Добавить лайк фильму
insert into likes(user_id,film_id) values (?,?)

