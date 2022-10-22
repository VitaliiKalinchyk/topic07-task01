package com.epam.rd.java.basic.topic07.task01.db.entity;

import java.util.Objects;

public class Team {

	private int id;
		
	private final String name;


	private Team(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static Team createTeam(String name) {
		return new Team(0, name);
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

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Team team = (Team) o;
		return Objects.equals(name, team.name);
	}
}