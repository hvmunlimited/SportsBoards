package com.sportsboards.boards;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import com.sportsboards.R;

/**
 * @author Mike Bonar
 * 
 */
public class BBallBoard extends BaseBoard{
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	private final int NUM_PLAYERS = 5;
	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	

	// ===========================================================
	// Getter & Setter
	// ===========================================================/

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public Engine onLoadEngine() {
		resID = R.raw.basketball;
		SPORT_NAME = "BASKETBALL";
		return super.onLoadEngine();
	}
	

	@Override
	public void onLoadResources() {
		super.onLoadResources();		
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "Basketball_cour_final.jpg", 0, 0);
		this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, "Basketball_Ball_48.png", 0, 0);
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
