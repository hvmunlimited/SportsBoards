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
public class Player extends PlayerInfo{

	private float mX, mY;
	private String teamColor;

	public Player(float mX, float mY, String teamColor, int id, int num, String type, String name) {
		super(id, num, type, name);
		this.setTeamColor(teamColor);
		this.setX(mX);
		this.setY(mY);
		// TODO Auto-generated constructor stub
	}

	public void setX(float mX) {
		this.mX = mX;
	}

	public float getX() {
		return mX;
	}

	public void setY(float mY) {
		this.mY = mY;
	}

	public float getY() {
		return mY;
	}

	public void setTeamColor(String teamColor) {
		this.teamColor = teamColor;
	}

	public String getTeamColor() {
		return teamColor;
	}

}
