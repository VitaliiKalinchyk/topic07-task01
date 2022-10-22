package com.epam.rd.java.basic.topic07.task01.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.epam.rd.java.basic.topic07.task01.db.entity.*;

public class DBManager {

	private static DBManager dbManager;

	private enum DBQueries {
		INSERT_USER("INSERT INTO users (login) VALUES (?)"),
		INSERT_TEAM("INSERT INTO teams (name) VALUES (?)"),
		INSERT_TEAMS_FOR_USER("INSERT INTO users_teams (user_id, team_id) VALUES (?, ?)"),
		FIND_ALL_USERS("SELECT * FROM users"),
		FIND_ALL_TEAMS("SELECT * FROM teams"),
		FIND_TEAM_BY_ID("SELECT * FROM teams WHERE id = ?"),
		FIND_USER_TEAMS("SELECT * FROM users_teams WHERE user_id = ?"),
		GET_USER("SELECT * FROM users WHERE login = ?"),
		GET_TEAM("SELECT * FROM teams WHERE name = ?"),
		DELETE_TEAM("DELETE FROM teams WHERE id = ?"),
		DELETE_USER("DELETE FROM users WHERE id = ?"),
		UPDATE_TEAM("UPDATE teams SET name = ? WHERE id = ?");
		private final String query;

		DBQueries(String query) {
			this.query = query;
		}

		public String getQuery() {
			return query;
		}
	}

	private DBManager() {}

	public static synchronized DBManager getInstance() {
		if (dbManager == null) {
			dbManager = new DBManager();
		}
		return dbManager;
	}

	public boolean insertUser(User user) throws DBException {
		String query = DBQueries.INSERT_USER.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, user.getLogin());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new DBException();
			}
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					user.setId(generatedKeys.getInt(1));
				} else {
					throw new DBException();
				}
			}
		} catch (SQLException e) {
			throw new DBException();
		}
		return true;
	}

	public User getUser(String login) throws DBException {
		User user = User.createUser(login);
		String query = DBQueries.GET_USER.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query))
		{
			ps.setString(1, login);
			ResultSet resultSet = ps.executeQuery();
			user.setId(resultSet.getInt(Fields.USER_ID));
		} catch (SQLException e) {
			throw new DBException();
		}
		return user;
	}

	public List<User> findAllUsers() throws DBException {
		List<User> users = new ArrayList<>();
		String query = DBQueries.FIND_ALL_USERS.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query))
		{
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()){
				users.add(getUser(resultSet));
			}
		} catch (SQLException e) {
			throw new DBException();
		}
		return users;
	}

	private User getUser(ResultSet resultSet) throws SQLException {
		User user = User.createUser(resultSet.getString(Fields.USER_LOGIN));
		user.setId(resultSet.getInt(Fields.USER_ID));
		return user;
	}


	public boolean insertTeam(Team team) throws DBException {
		String query = DBQueries.INSERT_TEAM.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, team.getName());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new DBException();
			}
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					team.setId(generatedKeys.getInt(1));
				} else {
					throw new DBException();
				}
			}
		} catch (SQLException e) {
			throw new DBException();
		}
		return true;
	}

	public Team getTeam(String name) throws DBException {
		Team team = Team.createTeam(name);
		String query = DBQueries.GET_TEAM.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query))
		{
			ps.setString(1, name);
			ResultSet resultSet = ps.executeQuery();
			team.setId(resultSet.getInt(Fields.TEAM_ID));
		} catch (SQLException e) {
			throw new DBException();
		}
		return team;
	}

	public List<Team> findAllTeams() throws DBException {
		List<Team> teams = new ArrayList<>();
		String query = DBQueries.FIND_ALL_TEAMS.getQuery();
		try (Connection con = DriverManager.getConnection(getCONNECTION_URL());
			 PreparedStatement ps = con.prepareStatement(query))
		{
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()){
				teams.add(getTeam(resultSet));
			}
		} catch (SQLException e) {
			throw new DBException();
		}
		return teams;
	}

	private Team getTeam(ResultSet resultSet) throws SQLException {
		Team team = Team.createTeam(resultSet.getString(Fields.TEAM_NAME));
		team.setId(resultSet.getInt(Fields.TEAM_ID));
		return team;
	}

	private String getCONNECTION_URL() {
		String URL = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("app.properties"));
			URL = properties.getProperty("connection.url");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return URL;
	}
}