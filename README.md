# JDBC create/read operations
 
The purpose of this exercise is to train you to work with the JDBC API.  
The estimated workload is *50 min*.

## Description

Please, proceed to `Demo` class and analyze its content. It demonstrates the functionality of types you should implement. All types are described below.

> Don't change the content of `Demo` class.

Proceed to `User` class and implement its methods:

* `String getLogin()` returns the user's login;
* `String toString()` returns the users's login;
* `boolean equals(Object)` works as follow: two User objects are equaled if and only if they both have the same login.
* `static User createUser(String login)` creates a new `User` object by its *login* and *id* = 0.

Proceed to `Team` class and implement its methods:

* `String getName()` returns the team's name;
* `String toString()` returns the team's name;
* `boolean equals(Object)` works as follow: two Team objects are equaled if and only if they both have the same name;
* `static Team createTeam(String name)` creates a new `Team` object by its *name* and *id* = 0.

Proceed to `DBManager` class and implement its methods:

* `static DBManager getInstance()` returns the only one instance of this `singleton` class;
* `List<User> findAllUsers` returns a list of all users;
* `insertTeam(Team)` inserts a `team` into the `teams` table; this method modifies the id of a `team`, if a `team` has been inserted;
* `List<Team> findAllTeams` returns a list of all teams.

### Details

Receive a connection using the following method:

`DriverManager.getConnection(CONNECTION_URL)`  
`CONNECTION_URL` is a connection string that includes *login* and *password* of a user.

You must read a connection string from the properties file `app.properties` using the key `connection.url`. The file should be located at the root of the project. An example of the `app.properties` file content:
```
connection.url = jdbc:mysql://localhost:3306/mydb?user=user&password=pswrd
```

> Do not load the driver class manually (referred to a call to `Class.forName(JDBC-DRIVER-FQN)` exclude that line from the code).

***

### Details about the database

* You can use any relational database. 

* The database contains three tables:  
`users (id, login)`  
`teams (id, name)`  
`users_teams (user_id, team_id)`  

* Initially, the database tables should have a some content (see a source code of the Demo class).

* Create an `sql` directory at the root and save the database creation script in it (`db-create.sql`).
