package com.sportsboards.boards;

import java.util.List;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.IUpdateHandler;
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
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "Soccer_Field_Final.png", 0, 0);
		this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, "Soccer_Ball_48.png", 0, 0);
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture);
	}
	
	@Override
	public Scene onLoadScene() {
		
		this.mMainScene = super.onLoadScene();
		mMainScene.getLayer(0).addEntity(new Sprite(0, 0, this.mBackGroundTextureRegion));

		final int centerX = CAMERA_WIDTH / 2 -25;
		final int centerY = CAMERA_HEIGHT / 2 -30;
		final Ball ball = new Ball(centerX, centerY, this.mBallTextureRegion);

		//red team
		addPlayer(new Player(0, "GK", 106, 281, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "", 317, 131, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "", 272, 204, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "", 230, 273, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "", 272, 347, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "", 317, 425, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "", 410, 280, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "", 460, 95, this.mRedPlayerTextureRegion),  mRedTeam);
		addPlayer(new Player(0, "", 460, 210, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "", 460, 325, this.mRedPlayerTextureRegion), mRedTeam);
		addPlayer(new Player(0, "", 460, 440, this.mRedPlayerTextureRegion), mRedTeam);

		addPlayer(new Player(0, "GK", 875, 281, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 748, 277, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 703, 206, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 703, 345, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 656, 138, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 656, 420, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 571, 275, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 517, 95, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 517, 210, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 517, 325, this.mBluePlayerTextureRegion), mBlueTeam);
		addPlayer(new Player(0, "GK", 517, 440, this.mBluePlayerTextureRegion), mBlueTeam);

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
