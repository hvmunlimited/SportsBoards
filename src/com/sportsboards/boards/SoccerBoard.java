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
public class SoccerBoard extends BaseBoard{
	
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
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public Engine onLoadEngine() {
		this.NUM_PLAYERS = 11;
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
	public Scene onLoadScene() {
		
		super.onLoadScene();
		
		this.mMainScene.getLayer(0).addEntity(new Sprite(0, 0, this.mBackGroundTextureRegion));

		final int centerX = CAMERA_WIDTH / 2 -25;
		final int centerY = CAMERA_HEIGHT / 2 -30;
		final Ball ball = new Ball(centerX, centerY, this.mBallTextureRegion);
		int x, y, interval;
		interval = 120;
		//red team
		x = 235;
		y = 90;
		
		addPlayer(new Player(0, "GK", 83, 269, this.mRedPlayerTextureRegion), mRedTeam);
		
		for(int i = 0; i < 4; i++){
			addPlayer(new Player(0, "GK", x, y, this.mRedPlayerTextureRegion), mRedTeam);
			y+=interval;
		}
		x = x + 110;
		y = 90;
		for(int i = 0; i < 4; i++){
			addPlayer(new Player(0, "GK", x, y, this.mRedPlayerTextureRegion), mRedTeam);
			y+=interval;
		}
		x = x + 80;
		y = 210;
		addPlayer(new Player(0, "GK", x, y, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "GK", x, y+interval, this.mRedPlayerTextureRegion), mRedTeam);

		
		x = 740;
		y = 90;
		interval = 120;
		
		addPlayer(new Player(0, "GK", 897, 264, this.mBluePlayerTextureRegion), mBlueTeam);
		
		for(int i = 0; i < 4; i++){
			addPlayer(new Player(0, "GK", x, y, this.mBluePlayerTextureRegion), mBlueTeam);
			y+=interval;
		}
		x = 630;
		y = 90;
		
		for(int i = 0; i < 4; i++){
			addPlayer(new Player(0, "GK", x, y, this.mBluePlayerTextureRegion), mBlueTeam);
			y+=interval;
		}
		
		addPlayer(new Player(0, "GK", 530, 210, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 530, 210 + interval, this.mBluePlayerTextureRegion), mBlueTeam);

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
