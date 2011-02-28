package com.sportsboards2d.sprites;

import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
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

public class PlayerSprite extends Sprite implements IOnMenuItemClickListener{
	
	
	
	/*
	 * Variables + Setters
	 */
	
	private PlayerInfo pInfo;
	public PlayerInfo getPlayerInfo(){ return pInfo;}
	
	protected boolean mGrabbed;
	
	protected HoldDetector mHold;
	public void setHoldDetector(HoldDetector mHold){ this.mHold = mHold;}
	public HoldDetector getHoldDetector(){ return mHold;}
	
	protected AnalogOnScreenControl directionControl;
	
	/*
	 * Constructors
	 */
	
	public PlayerSprite(PlayerInfo pInfo, TextureRegion tex){
		super(pInfo.getX(), pInfo.getY(), tex);
		this.pInfo = pInfo;
	}
	/* Overrides
	 * @see org.anddev.andengine.entity.shape.Shape#onAreaTouched(org.anddev.andengine.input.touch.TouchEvent, float, float)
	 */
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		
		return true;
	}
	/* (non-Javadoc)
	 * @see org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener#onMenuItemClicked(org.anddev.andengine.entity.scene.menu.MenuScene, org.anddev.andengine.entity.scene.menu.item.IMenuItem, float, float)
	 */
	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	
}