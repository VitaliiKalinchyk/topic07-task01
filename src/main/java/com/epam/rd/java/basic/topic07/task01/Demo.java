package com.epam.rd.java.basic.topic07.task01;

import com.epam.rd.java.basic.topic07.task01.db.*;
import com.epam.rd.java.basic.topic07.task01.db.entity.*;

public class Demo {
	
	public static void main(String[] args) throws DBException {
		// users  ==> [ivanov]
		// teams  ==> [teamA]

		DBManager dbManager = DBManager.getInstance();

		dbManager.insertUser(User.createUser("petrov"));
		dbManager.insertUser(User.createUser("obama"));

		System.out.println(dbManager.findAllUsers());
		// users  ==> [ivanov, petrov, obama]

		dbManager.insertTeam(Team.createTeam("teamB"));
		dbManager.insertTeam(Team.createTeam("teamC"));

		System.out.println(dbManager.findAllTeams());
		// teams ==> [teamA, teamB, teamC]        
	}

}
