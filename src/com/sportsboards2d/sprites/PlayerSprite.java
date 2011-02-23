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

public class PlayerSprite extends Sprite implements IOnMenuItemClickListener{
	
	
	
	/*
	 * Variables + Setters
	 */
	
	private PlayerInfo pInfo;
	public PlayerInfo getPlayerInfo(){ return pInfo;}
	
	protected boolean mGrabbed;
	
	protected HoldDetector mHold;
	public void setHoldDetector(HoldDetector mHold){ this.mHold = mHold;
														  mHold.setTriggerHoldMinimumMilliseconds(800);}
	public HoldDetector getHoldDetector(){ return mHold;}
	
	/*
	 * Constructors
	 */
	
	public PlayerSprite(PlayerInfo pInfo, float startx, float starty, TextureRegion tex){
		super(startx, starty, tex);
		this.pInfo = pInfo;
	}
	/* Overrides
	 * @see org.anddev.andengine.entity.shape.Shape#onAreaTouched(org.anddev.andengine.input.touch.TouchEvent, float, float)
	 */
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		mHold.onTouchEvent(pSceneTouchEvent);
		switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				this.setScale(2.0f);	
				this.mGrabbed = true;
				break;
			case TouchEvent.ACTION_MOVE:
				if(this.mGrabbed) {

					this.setPosition(pSceneTouchEvent.getX() - 48 / 2, pSceneTouchEvent.getY() - 48 / 2);
					//System.out.println(this.getX() + " , " + this.getY());
				}
				break;
			case TouchEvent.ACTION_UP:
				if(this.mGrabbed) {
					this.mGrabbed = false;
					this.setScale(1.0f);					
				}
				break;
		}
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