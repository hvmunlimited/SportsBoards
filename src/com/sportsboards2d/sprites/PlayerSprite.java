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
}