/**
 * 
 */
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
public class HockeyBoard extends BaseBoard {

	@Override
	public Engine onLoadEngine() {
		NUM_PLAYERS = 11;
		resID = R.raw.hockey;
		SPORT_NAME = "hockey";
		BALL_PATH_SMALL = "hockey_puck.png";
		return super.onLoadEngine();
	}
	@Override
	public void onLoadResources() {
		super.onLoadResources();	
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "hockey_rink.jpg", 0, 0);
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture);
	}
	@Override
	public void onLoadComplete() {

	}
}
