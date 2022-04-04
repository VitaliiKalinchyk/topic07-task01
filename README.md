В качестве базы данных использовать реляционную БД.

БД содержит три таблицы:

```
users (id, login)
teams (id, name)
users_teams (user_id, team_id)
```

Изначально таблицы БД должны иметь некоторое наполнение (см. код класса Demo).

В корне создать каталог sql и сохранить в нем скрипт создания базы данных `db-create.sql`

Создать и реализовать типы таким образом, чтобы при запуске класса Demo отрабатывала соответствующая функциональность.

> Пакет: `com.epam.rd.java.basic.topic07.task01`
Классы: 
Demo - содержит демонстрацию функционала, оставить без изменений.
Остальные классы см. репозиторий.

Содержимое класса Demo:
```
public class Demo {

	public static void main(String[] args) throws DBException {
		// users  ==> [ivanov]
		// teams  ==> [teamA]
		DBManager dbManager = DBManager.getInstance();

		dbManager.insertUser(User.createUser("petrov"));
		dbManager.insertUser(User.createUser("obama"));
		System.out.println(dbManager.findAllUsers());
		// users  ==> [ivanov, petrov, obama]

		dbManager.insertTeam(Team.createTeam("teamB"));
		dbManager.insertTeam(Team.createTeam("teamC"));
		System.out.println(dbManager.findAllTeams());
		// teams ==> [teamA, teamB, teamC]
	}
}
```
(1) Метод DBManager#`insertUser` должен модифицировать поле id объекта User.

(2) Метод DBManager#`findAllUsers` возвращает объект `java.util.List<User>`.

(3) Метод DBManager#`insertTeam` должен модифицировать поле id объекта Team.

(4) Метод DBManager#`findAllTeams` возвращает объект `java.util.List<Team>`.

##### Замечание.

Класс User должен содержать:
- метод `getLogin()`, который возвращает логин пользователя;
- метод `toString()`, который возвращает логин пользователя;
- реализацию метода `equals(Object obj)`, согласно которой два объекта User равны тогда и только тогда, когда они имеют один логин;
- статический метод `createUser(String login)`, который создает объект User по логину (идентификатор равен 0).

Класс Team должен содержать:
- метод `getName()`, который возвращает название группы;
- метод `toString()`, который возвращает название группы;
- реализацию метода `equals(Object obj)`, согласно которой два объекта Team равны тогда и только тогда, когда они имеют одно название.
- статический метод `createTeam(String name)`, который создает объект Team по имени (идентификатор равен 0).
