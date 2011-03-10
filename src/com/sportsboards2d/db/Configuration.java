/**
 * 
 */
package com.sportsboards2d.db;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */
public class Configuration {

	private boolean lineEnabled;
	private boolean lastLoaded;
	
	private String default_sport;
	private boolean largePlayers;
	
	public Configuration(){}
	
	public Configuration(boolean line, boolean load, String sport, boolean player){
		this.lineEnabled = line;
		this.lastLoaded = load;
		this.default_sport = sport;
		this.largePlayers = player;
	}

	public void setLineEnabled(boolean lineEnabled) {
		this.lineEnabled = lineEnabled;
	}

	public boolean isLineEnabled() {
		return lineEnabled;
	}

	public void setLastLoaded(boolean lastLoaded) {
		this.lastLoaded = lastLoaded;
	}

	public boolean isLastLoaded() {
		return lastLoaded;
	}

	public void setDefault_sport(String default_sport) {
		this.default_sport = default_sport;
	}

	public String getDefault_sport() {
		return default_sport;
	}

	public void setLargePlayers(boolean largePlayers) {
		this.largePlayers = largePlayers;
	}

	public boolean isLargePlayers() {
		return largePlayers;
	}

	

	
}
