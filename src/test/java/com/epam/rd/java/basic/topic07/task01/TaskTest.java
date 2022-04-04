package com.epam.rd.java.basic.topic07.task01;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import org.junit.jupiter.api.*;

import com.epam.rd.java.basic.topic07.task01.Demo;
import com.epam.rd.java.basic.topic07.task01.db.*;
import com.epam.rd.java.basic.topic07.task01.db.entity.*;

/**
 * @author D. Kolesnikov
 */
public class TaskTest {

	private static final String CONNECTION_URL = "jdbc:derby:memory:testdb;create=true";

	private static final String SHUTDOWN_URL = "jdbc:derby:;shutdown=true";

	private static final String APP_PROPS_FILE = "app.properties";

	private static final String APP_CONTENT = "connection.url=" + CONNECTION_URL;

	private static final String CREATE_USERS_TABLE =
			"CREATE TABLE users ("
			+ "	id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," 
			+ "	login VARCHAR(10) UNIQUE" 
			+ ")";

	private static final String CREATE_TEAMS_TABLE = 
			"CREATE TABLE teams ("
			+ "	id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," 
			+ "	name VARCHAR(10)" 
			+ ")";

	private static final String CREATE_USERS_TEAMS_TABLE = 
			"CREATE TABLE users_teams ("
			+ "	user_id INT REFERENCES users(id) on delete cascade," 
			+ "	team_id INT REFERENCES teams(id) on delete cascade,"
			+ "	UNIQUE (user_id, team_id)" 
			+ ")";

	private static final String DROP_USERS_TEAMS_TABLE = "DROP TABLE users_teams";

	private static final String DROP_USERS_TABLE = "DROP TABLE users";

	private static final String DROP_TEAMS_TABLE = "DROP TABLE teams";

	private static final String DERBY_LOG_FILE = "derby.log";

	private static Connection con;
	
	private static String userDefinedAppContent;
	
	@BeforeAll
	static void globalSetUp() throws SQLException, IOException {
		userDefinedAppContent = Files.readString(Path.of(APP_PROPS_FILE));
	    Files.write(Path.of(APP_PROPS_FILE), APP_CONTENT.getBytes());

		con = DriverManager.getConnection(CONNECTION_URL);
	}

	@AfterAll
	static void globalTearDown() throws SQLException, IOException {
		con.close();
		try {
			DriverManager.getConnection(SHUTDOWN_URL);
		} catch (SQLException ex) {
			System.err.println("Derby shutdown");
		}
		Files.delete(Path.of(DERBY_LOG_FILE));
		
		try (PrintWriter out = new PrintWriter(APP_PROPS_FILE)) {
			out.print(userDefinedAppContent);
		}
	}

	private DBManager dbm;
	
	@BeforeEach
	void setUp() throws SQLException {
		dbm = DBManager.getInstance();

		con.createStatement().executeUpdate(CREATE_USERS_TABLE);
		con.createStatement().executeUpdate(CREATE_TEAMS_TABLE);
		con.createStatement().executeUpdate(CREATE_USERS_TEAMS_TABLE);
	}

	@AfterEach
	void tearDown() throws SQLException {
		con.createStatement().executeUpdate(DROP_USERS_TEAMS_TABLE);
		con.createStatement().executeUpdate(DROP_USERS_TABLE);
		con.createStatement().executeUpdate(DROP_TEAMS_TABLE);
	}
	
	@Test
	void testCompliance() throws IOException {
		assertTrue(Files.exists(Path.of("sql/db-create.sql")), "No db-create.sql file was found in a sql directory");
		
		List<String> lines = Files.readAllLines(Path.of("sql/db-create.sql"));

		assertFalse(lines.size() < 8, "Too small count of lines in a db-create.sql file");
		assertTrue(lines.stream().anyMatch(line -> line.toLowerCase().contains("cascade")),
				"sql/db-create.sql must contain CASCADE word");
	}
	
	@Test
	void testDemo() throws DBException, SQLException {
		con.createStatement().executeUpdate("insert into users values (DEFAULT, 'ivanov')");
		con.createStatement().executeUpdate("insert into teams values (DEFAULT, 'teamA')");

		Demo.main(null);
	}

	@Test
	void test1() {
		User user = User.createUser("testUser");
		User user2 = User.createUser("testUser");
		assertEquals("testUser",  user.getLogin());
		assertTrue(user.equals(user2), "Two users must be equaled if their logins are equaled");
	}

	@Test
	void test2() {
		Team team = Team.createTeam("testTeam");
		Team team2 = Team.createTeam("testTeam");
		assertEquals("testTeam",  team.getName());
		assertTrue(team.equals(team2), "Two teams must be equaled if their logins are equaled");
	}

	@Test
	void test3() throws DBException {
		List<User> users = createAndInsertUsers(1, 5);
		List<User> usersFromDB = sort(dbm.findAllUsers(), User::getLogin);
		assertEquals(users, usersFromDB);
	}

	@Test
	void test4() throws DBException {
		List<Team> teams = createAndInsertTeams(1, 5);
		List<Team> teamsFromDB = sort(dbm.findAllTeams(), Team::getName);

		assertEquals(teams, teamsFromDB);
	}

	private static <T, U extends Comparable<? super U>> List<T> 
    		sort(List<T> items, Function<T, U> extractor) {
		items.sort(Comparator.comparing(extractor));
		return items;
	}

	private List<Team> createAndInsertTeams(int from, int to) throws DBException {
		DBManager dbm = DBManager.getInstance();
		List<Team> teams = IntStream.range(from, to)
			.mapToObj(x -> "team" + x)
			.map(Team::createTeam)
			.collect(Collectors.toList());

		for (Team team : teams) {
			dbm.insertTeam(team);
		}
		return teams;
	}

	private List<User> createAndInsertUsers(int from, int to) throws DBException {
		DBManager dbm = DBManager.getInstance();
		List<User> users = IntStream.range(from, to)
			.mapToObj(x -> "user" + x)
			.map(User::createUser)
			.collect(Collectors.toList());

		for (User user : users) {
			dbm.insertUser(user);
		}
		return users;
	}
	
}
