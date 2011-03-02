package com.sportsboards2d.boards;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import com.sportsboards2d.R;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public class SoccerBoard extends BaseBoard{
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public Engine onLoadEngine() {
		NUM_PLAYERS = 11;
		resID = R.raw.soccer;
		SPORT_NAME = "SOCCER";
		BALL_PATH = "Soccer_Ball_";
		return super.onLoadEngine();
	}
	@Override
	public void onLoadResources() {
		super.onLoadResources();	
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "soccer_field.jpg", 0, 0);
		this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallTexture, this, BALL_PATH + "48.png", 0, 0, 1, 1);
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture);
	}
	@Override
	public void onLoadComplete() {

	}
}
