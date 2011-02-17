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

public class BBallBoard extends BaseBoard{
	
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public Engine onLoadEngine() {
		NUM_PLAYERS = 5;
		resID = R.raw.basketball;
		SPORT_NAME = "BASKETBALL";
		return super.onLoadEngine();
	}
	

	@Override
	public void onLoadResources() {
		super.onLoadResources();		
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "basketball_court.jpg", 0, 0);
		this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, "Basketball_Ball_48.png", 0, 0);
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture);
	}
	
	@Override
	public void onLoadComplete() {

	}
}