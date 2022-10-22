package com.epam.rd.java.basic.topic07.task01.db.entity;

public class User {

	private int id;

	private final String login;

	private User(int id, String login) {
		this.id = id;
		this.login = login;
	}

	public static User createUser(String login) {
		return new User(0, login);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return login.equals(user.login);
	}
}