/**
 * 
 */
package com.sportsboards2d.db.objects;

import java.util.List;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class FormationEntry {
	
	private String fName;
	private Coordinates ball;
	private List<PlayerEntry> players;
	
	public FormationEntry(String fName, Coordinates ball, List<PlayerEntry>players){
		this.setfName(fName);
		this.setBall(ball);
		this.setPlayers(players);
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getfName() {
		return fName;
	}

	public void setBall(Coordinates ball) {
		this.ball = ball;
	}

	public Coordinates getBall() {
		return ball;
	}

	public void setPlayers(List<PlayerEntry> players) {
		this.players = players;
	}

	public List<PlayerEntry> getPlayers() {
		return players;
	}

}
