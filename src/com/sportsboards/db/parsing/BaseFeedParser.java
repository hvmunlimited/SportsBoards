package com.sportsboards.db.parsing;

/**
 * Coded by Nathan King
 */

public abstract class BaseFeedParser implements FeedParser{
	
	//XML tag names
	static final String TAG_ROOT = "forms";
	static final String TAG_FORM = "form";
	static final String TAG_FORM_NAME = "name";
	static final String TAG_FORM_BALL = "ball";
	static final String TAG_FORM_BALL_ATTRIBUTE_X = "x";
	static final String TAG_FORM_BALL_ATTRIBUTE_Y = "y";
	static final String TAG_PLAYER = "player";
	static final String TAG_PLAYER_TEAM = "team";
	static final String TAG_PLAYER_TYPE = "type";
	static final String TAG_PLAYER_PNAME = "pName";
	static final String TAG_PLAYER_COORDS = "coords";
	static final String TAG_PLAYER_ATTRIBUTE_X = "x";
	static final String TAG_PLAYER_ATTRIBUTE_Y = "y";
	
}