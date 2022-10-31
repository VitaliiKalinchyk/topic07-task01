package com.epam.rd.java.basic.topic07.task01;

public final class Constants {

	private Constants() {
	}

	public static final String SETTINGS_FILE = "app.properties";

	public static final String URL_PROPERTY = "connection.url";

	public static final String INSERT_USER = "INSERT INTO users (login) VALUES (?)";

	public static final String INSERT_TEAM = "INSERT INTO teams (name) VALUES (?)";

	public static final String INSERT_TEAMS_FOR_USER = "INSERT INTO users_teams (user_id, team_id) VALUES (?, ?)";

	public static final String FIND_ALL_USERS = "SELECT * FROM users";

	public static final String FIND_ALL_TEAMS = "SELECT * FROM teams";

	public static final String GET_USER_TEAMS =
			"SELECT id, teams.name FROM teams JOIN users_teams ON teams.id = users_teams.team_id WHERE user_id = ?";

	public static final String GET_TEAM_USERS =
			"SELECT id, login FROM users JOIN users_teams ON users.id = users_teams.user_id WHERE team_id = ?";

	public static final String GET_USER = "SELECT * FROM users WHERE login = ?";

	public static final String GET_TEAM = "SELECT * FROM teams WHERE teams.name = ?";

	public static final String DELETE_TEAM = "DELETE FROM teams WHERE id = ?";

	public static final String DELETE_USER = "DELETE FROM users WHERE id = ?";

	public static final String UPDATE_TEAM = "UPDATE teams SET teams.name = ? WHERE id = ?";
}