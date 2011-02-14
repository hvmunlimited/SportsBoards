package com.sportsboards.boards;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import com.sportsboards.R;

/**
 * @author Mike Bonar
 * 
 */
public class SoccerBoard extends BaseBoard{
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	private final int NUM_PLAYERS = 11;
	
	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public Engine onLoadEngine() {
		resID = R.raw.soccer;
		SPORT_NAME = "SOCCER";
		return super.onLoadEngine();
	}
	@Override
	public void onLoadResources() {
		super.onLoadResources();	
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "Soccer_Field_Final_02.jpg", 0, 0);
		this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, "Soccer_Ball_48.png", 0, 0);
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture);
	}
	
	@Override
	public void onLoadComplete() {

	}
	
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
