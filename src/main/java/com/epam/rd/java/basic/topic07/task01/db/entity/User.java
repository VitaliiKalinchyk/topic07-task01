package com.epam.rd.java.basic.topic07.task01.db.entity;

import java.util.Objects;

public class User {

	private final int id = 0;

	private final String login;

	public User(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	@Override
	public String toString() {
		return login;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(login, user.login);
	}

	public static User createUser(String login) {
		return new User(login);
	}
}