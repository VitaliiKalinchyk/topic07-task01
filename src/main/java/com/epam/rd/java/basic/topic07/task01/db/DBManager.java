package com.epam.rd.java.basic.topic07.task01.db;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.epam.rd.java.basic.topic07.task01.db.entity.*;
import static com.epam.rd.java.basic.topic07.task01.Constants.*;
import static com.epam.rd.java.basic.topic07.task01.db.Fields.*;


public class DBManager {

	private static DBManager dbManager;

	private DBManager() {}

	public static synchronized DBManager getInstance() {
		if (dbManager == null) {
			dbManager = new DBManager();
		}
		return dbManager;
	}

	public boolean insertUser(User user) throws DBException {
		try (Connection con = DriverManager.getConnection(URL());
			 PreparedStatement ps = con.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, user.getLogin());
			try {
				ps.execute();
			} catch (SQLException e) {
				return false;
			}
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					user.setId(generatedKeys.getInt(1));
				}
			}
		} catch (SQLException e) {
			throw new DBException("Inserting user failed", e);
		}
		return true;
	}

	public User getUser(String login) throws DBException {
		User user;
		try (Connection con = DriverManager.getConnection(URL());
			 PreparedStatement ps = con.prepareStatement(GET_USER))
		{
			ps.setString(1, login);
			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					user = createUserFromDB(resultSet);
				} else {
					throw new DBException("Getting user failed, no results");
				}
			}
		} catch (SQLException e) {
			throw new DBException("Getting user failed", e);
		}
		return user;
	}

	public List<User> findAllUsers() throws DBException {
		List<User> users = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(URL());
			 PreparedStatement ps = con.prepareStatement(FIND_ALL_USERS);
			 ResultSet resultSet = ps.executeQuery())
		{
			while (resultSet.next()){
				users.add(createUserFromDB(resultSet));
			}
		} catch (SQLException e) {
			throw new DBException("Getting user's list failed", e);
		}
		return users;
	}

	public boolean deleteUsers(User... users) throws DBException {
		try (Connection con = DriverManager.getConnection(URL());
			 PreparedStatement ps = con.prepareStatement(DELETE_USER))
		{
			for (User user : users) {
				ps.setInt(1, user.getId());
				ps.execute();
			}
		} catch (SQLException e) {
			throw new DBException("Deleting users failed", e);
		}
		return true;
	}

	public boolean insertTeam(Team team) throws DBException {
		try (Connection con = DriverManager.getConnection(URL());
			 PreparedStatement ps = con.prepareStatement(INSERT_TEAM, Statement.RETURN_GENERATED_KEYS))
		{
			ps.setString(1, team.getName());
			try {
				ps.executeUpdate();
			} catch (SQLException e) {
				return false;
			}
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					team.setId(generatedKeys.getInt(1));
				} else {
					throw new DBException("Inserting team failed, no ID for new team");
				}
			}
		} catch (SQLException e) {
			throw new DBException("Inserting team failed", e);
		}
		return true;
	}

	public Team getTeam(String name) throws DBException {
		Team team;
		try (Connection con = DriverManager.getConnection(URL());
			 PreparedStatement ps = con.prepareStatement(GET_TEAM))
		{
			ps.setString(1, name);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				team = createTeamFormDB(resultSet);
			} else {
				throw new DBException("Getting team failed, no results");
			}
		} catch (SQLException e) {
			throw new DBException("Getting team failed", e);
		}
		return team;
	}

	public List<Team> findAllTeams() throws DBException {
		List<Team> teams = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(URL());
			 PreparedStatement ps = con.prepareStatement(FIND_ALL_TEAMS))
		{
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()){
				teams.add(createTeamFormDB(resultSet));
			}
		} catch (SQLException e) {
			throw new DBException("Getting team's list failed", e);
		}
		return teams;
	}

	public boolean deleteTeam(Team team) throws DBException {
		try (Connection con = DriverManager.getConnection(URL());
			 PreparedStatement stmt = con.prepareStatement(DELETE_TEAM))
		{
			stmt.setInt(1, team.getId());
			stmt.execute();
		} catch (SQLException e) {
			throw new DBException("Deleting team failed", e);
		}
		return true;
	}

	public boolean setTeamsForUser(User user, Team... teams) throws DBException {
		if (user == null) {
			throw new DBException("No user to set");
		}
		try (Connection con = DriverManager.getConnection(URL());
			 PreparedStatement ps = con.prepareStatement(INSERT_TEAMS_FOR_USER))
		{
			con.setAutoCommit(false);
			try {
				for (Team team : teams) {
					if (team == null) {
						con.rollback();
						return false;
					}
					ps.setString(1, String.valueOf(user.getId()));
					ps.setString(2, String.valueOf(team.getId()));
					ps.execute();
				}
			} catch (SQLException e) {
				con.rollback();
				return false;
			}
			con.commit();
		} catch (SQLException e) {
			throw new DBException("Setting teams for user failed", e);
		}
		return true;
	}

	public List<Team> getUserTeams(User user) throws DBException {
		List<Team> teamList = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(URL());
			 PreparedStatement ps = con.prepareStatement(GET_USER_TEAMS))
		{
			ps.setInt(1, user.getId());
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					teamList.add(createTeamFormDB(resultSet));
				}
			}
		} catch (SQLException e) {
			throw new DBException("Getting user's teams failed", e);
		}
		return teamList;
	}

	public List<User> getTeamUsers(Team team) throws DBException {
		List<User> userList = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(URL());
			 PreparedStatement ps = con.prepareStatement(GET_TEAM_USERS))
		{
			ps.setInt(1, team.getId());
			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					userList.add(createUserFromDB(resultSet));
				}
			}
		} catch (SQLException e) {
			throw new DBException("Getting team's users failed", e);
		}
		return userList;
	}

	public boolean updateTeam(Team team) throws DBException {
		try (Connection con = DriverManager.getConnection(URL());
			 PreparedStatement ps = con.prepareStatement(UPDATE_TEAM))
		{
			ps.setString(1, team.getName());
			ps.setInt(2, team.getId());
			ps.execute();
		} catch (SQLException e) {
			throw new DBException("Updating team failed", e);
		}
		return true;
	}

	private String URL() throws DBException {
		String URL;
		try (FileInputStream inputStream = new FileInputStream(SETTINGS_FILE)) {
			Properties properties = new Properties();
			properties.load(inputStream);
			URL = properties.getProperty(URL_PROPERTY);
		} catch (IOException e) {
			throw new DBException("Loading URL failed");
		}
		return URL;
	}

	private User createUserFromDB(ResultSet resultSet) throws SQLException {
		User user = User.createUser(resultSet.getString(USER_LOGIN));
		user.setId(resultSet.getInt(USER_ID));
		return user;
	}

	private Team createTeamFormDB(ResultSet resultSet) throws SQLException {
		Team team = Team.createTeam(resultSet.getString(TEAM_NAME));
		team.setId(resultSet.getInt(TEAM_ID));
		return team;
	}
}