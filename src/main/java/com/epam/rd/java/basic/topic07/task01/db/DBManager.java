package com.epam.rd.java.basic.topic07.task01.db;

import java.util.ArrayList;
import java.util.List;

import com.epam.rd.java.basic.topic07.task01.db.entity.*;

public class DBManager {

	private static final DBManager dbManager = new DBManager();

	private static List<User> users;

	private static List<Team> teams;

	private DBManager() {
	}

	public static synchronized DBManager getInstance() {
		users = new ArrayList<>();
		teams = new ArrayList<>();
		return dbManager;
	}

	public List<User> findAllUsers() throws DBException {
		if (users.isEmpty()) {
			throw new DBException();
		}
		return users;
	}

	public boolean insertUser(User user) throws DBException {
		return users.add(user);
	}

	public User getUser(String login) throws DBException {
		return users
					.stream()
					.filter(user -> user.getLogin().equals(login))
					.findFirst()
					.orElseThrow(DBException::new);
	}

	public Team getTeam(String name) throws DBException {
		return teams
					.stream()
					.filter(team -> team.getName().equals(name))
					.findFirst()
					.orElseThrow(DBException::new);
	}

	public List<Team> findAllTeams() throws DBException {
		if (teams.isEmpty()) {
			throw new DBException();
		}
		return teams;
	}

	public boolean insertTeam(Team team) throws DBException {
		return teams.add(team);
	}

}
