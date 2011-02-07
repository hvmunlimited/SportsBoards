package com.sportsboards.boards;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import com.sportsboards.sprites.Ball;
import com.sportsboards.sprites.Player;

/**
 * @author Mike Bonar
 * 
 */
public class BBallBoard extends BaseBoard{
	
	// ===========================================================
	// Constants
	// ===========================================================

	
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
		this.NUM_PLAYERS = 5;
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
	public Scene onLoadScene() {
		
		final Scene mMainScene = super.onLoadScene();
		mMainScene.getLayer(0).addEntity(new Sprite(0, 0, this.mBackGroundTextureRegion));

		final int centerX = CAMERA_WIDTH / 2 -24;
		final int centerY = CAMERA_HEIGHT / 2 -24;
		final Ball ball = new Ball(centerX, centerY, this.mBallTextureRegion);
		
		addPlayer(new Player(0, "GK", 518, 177, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "GK", 581, 229, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "GK", 538, 276, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "GK", 581, 323, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "GK", 518, 370, this.mRedPlayerTextureRegion), mRedTeam);

		addPlayer(new Player(0, "GK", 457, 177, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 397, 229, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 440, 276, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 397, 323, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 457, 370, this.mBluePlayerTextureRegion), mBlueTeam);
		
		mMainScene.getTopLayer().addEntity(ball);
		mMainScene.setOnAreaTouchTraversalFrontToBack();
		mMainScene.registerTouchArea(ball);

		return mMainScene;
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
