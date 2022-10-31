package com.epam.rd.java.basic.topic07.task01.db.entity;

public class Team {

	private int id;
	private String name;


	private Team(String name) {
		this.name = name;
	}

	public static Team createTeam(String name) {
		return new Team(name);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Team team = (Team) o;
		return name.equals(team.name);
	}
}