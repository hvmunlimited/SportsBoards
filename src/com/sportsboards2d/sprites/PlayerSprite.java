package com.sportsboards2d.sprites;

import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.sportsboards2d.db.PlayerInfo;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public class PlayerSprite extends BaseSprite{
	
	private final int NAME_DISPLAY = 0;
	private final int POSITION_DISPLAY = 1;
	private final int NUMBER_DISPLAY = 2;
	
	/*
	 * Variables + Setters
	 */
	
	private PlayerInfo pInfo;
	public PlayerInfo getPlayerInfo(){ return pInfo;}
	
	/*
	 * Constructors
	 */
	
	public PlayerSprite(PlayerInfo pInfo, TiledTextureRegion tex){
		super(pInfo.getX(), pInfo.getY(), tex);
		this.pInfo = pInfo;
	}

	public ChangeableText getNameDisplay() {
		return this.displayInfo.get(NAME_DISPLAY);
	}
	public ChangeableText getPositionDisplay(){
		return this.displayInfo.get(POSITION_DISPLAY);
	}
	public ChangeableText getNumberDisplay(){
		return this.displayInfo.get(NUMBER_DISPLAY);
	}
	public void setupDisplay(){
		getNameDisplay().setVisible(false);
		getPositionDisplay().setVisible(false);
		getNumberDisplay().setVisible(false);
		this.attachChild(getNameDisplay());
		this.attachChild(getPositionDisplay());
		this.attachChild(getNumberDisplay());
	}
	public void displayInfo(boolean bool){
		
		if(bool){
			getNameDisplay().setVisible(true);
			getPositionDisplay().setVisible(true);
			getNumberDisplay().setVisible(true);
		}
		else{
			getNameDisplay().setVisible(false);
			getPositionDisplay().setVisible(false);
			getNumberDisplay().setVisible(false);
		}
		
	}
}