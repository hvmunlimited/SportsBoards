package com.sportsboards.launcher;

import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.sportsboards.R;
import com.sportsboards.SoccerBoard;
import com.sportsboards.FootBallBoard;

enum Activity{

	SOCCER(SoccerBoard.class, R.string.soccer_string);
	//FOOTBALL(FootBallBoard.class R.string.football_string);
	

	public final Class<? extends BaseGameActivity> Class;
	public final int id;
	
	private Activity(final Class<? extends BaseGameActivity> pActClass, final int pName){
		this.Class = pActClass;
		this.id = pName;
	}
	
}

