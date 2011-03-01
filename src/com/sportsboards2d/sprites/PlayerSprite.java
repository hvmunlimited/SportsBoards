package com.sportsboards2d.sprites;

import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.HoldDetector;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

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
	
	
	
	protected HoldDetector mHold;
	public void setHoldDetector(HoldDetector mHold){ this.mHold = mHold;}
	public HoldDetector getHoldDetector(){ return mHold;}

	/*
	 * Constructors
	 */
	
	public PlayerSprite(PlayerInfo pInfo, TextureRegion tex){
		super(pInfo.getX(), pInfo.getY(), tex);
		this.pInfo = pInfo;
		
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1, float arg2, float arg3) {
		
		return false;		
	}
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				this.setScale(2.0f);
				this.setmGrabbed(true);
				break;
			case TouchEvent.ACTION_MOVE:
				if(this.ismGrabbed()) {
					this.setPosition(pSceneTouchEvent.getX() - 48 / 2, pSceneTouchEvent.getY() - 48 / 2);
				}
				break;
			case TouchEvent.ACTION_UP:
				if(this.ismGrabbed()) {
					this.setmGrabbed(false);
					this.setScale(1.0f);
				}
				break;
		}
		return true;
	}
}