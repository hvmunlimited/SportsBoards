package com.sportsboards2d.sprites;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public class BallSprite extends Sprite{
	
	/*
	 * Variables
	 */
	
	private boolean mGrabbed;
	
	/*
	 * Constructors
	 */
	
	public BallSprite(float x, float y, TextureRegion tex){
		super(x, y, tex);	
	}
	
	/*
	 * @see org.anddev.andengine.entity.shape.Shape#onAreaTouched(org.anddev.andengine.input.touch.TouchEvent, float, float)
	 */
	
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				this.setScale(2.0f);
				this.mGrabbed = true;
				break;
			case TouchEvent.ACTION_MOVE:
				if(this.mGrabbed) {
					this.setPosition(pSceneTouchEvent.getX() - 48 / 2, pSceneTouchEvent.getY() - 48 / 2);
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
}