package com.sportsboards.launcher;

import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.sportsboards.R;
import com.sportsboards.boards.BBallBoard;
import com.sportsboards.boards.SoccerBoard;

/**
 * Coded by Nathan King
 */

enum Activity{

	/*
	 * List of Activities (aka sports)
	 */
	
	SOCCER(SoccerBoard.class, R.string.soccer_string),
	//FOOTBALL(FootBallBoard.class, R.string.football_string),
	BBALL(BBallBoard.class, R.string.basketball_string);
	//XMLPARSETEST(XMLParseTest.class, R.string.xmltest_string);

	public final Class<? extends BaseGameActivity> Class;
	public final int id;
	
	private Activity(final Class<? extends BaseGameActivity> pActClass, final int pName){
		this.Class = pActClass;
		this.id = pName;
	}
	
}

