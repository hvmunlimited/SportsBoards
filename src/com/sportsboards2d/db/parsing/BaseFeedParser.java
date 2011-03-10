package com.sportsboards2d.db.parsing;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public abstract class BaseFeedParser implements FeedParser{
	
	//XML tag names
	
	//formation tags
	static final String ROOT = "forms";
	static final String FORM = "form";
	static final String NAME = "name";
	static final String BALL = "ball";
	static final String PLAYER = "player";
	static final String TEAM = "team";
	static final String TYPE = "type";
	static final String PNAME = "pName";
	static final String COORDS = "coords";
	
	//config tags
	static final String CONFIG = "config";
	static final String LINE_ENABLED = "line_enabled";
	static final String LOAD_LAST = "last_loaded";
	static final String PLAYER_SIZE = "player_size";
	static final String DEFAULT = "default_sport";
	
}