/**
 * 
 */
package com.sportsboards2d.db.objects;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class PlayerEntry {
	
	private int pID;
	private String pTeam;
	private Coordinates coords;
	
	public PlayerEntry(int pID, String pTeam, Coordinates coords){
		this.setpID(pID);
		this.setpTeam(pTeam);
		this.setCoords(coords);
	}
	
	public void setpID(int pID) {
		this.pID = pID;
	}
	public int getpID() {
		return pID;
	}
	public void setpTeam(String pTeam) {
		this.pTeam = pTeam;
	}
	public String getpTeam() {
		return pTeam;
	}
	public void setCoords(Coordinates coords) {
		this.coords = coords;
	}
	public Coordinates getCoords() {
		return coords;
	}
}
