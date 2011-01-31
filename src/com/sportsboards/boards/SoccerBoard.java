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
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "Soccer_Field_Final.jpg", 0, 0);
		this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, "Soccer_Ball_48.png", 0, 0);
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture);
	}
	
	@Override
	public Scene onLoadScene() {
		
		final Scene scene = super.onLoadScene();
		scene.getLayer(0).addEntity(new Sprite(0, 0, this.mBackGroundTextureRegion));

		final int centerX = CAMERA_WIDTH / 2 -25;
		final int centerY = CAMERA_HEIGHT / 2 -30;
		final Ball ball = new Ball(centerX, centerY, this.mBallTextureRegion);

		int startx = 1;
		int starty = 512;
		//red team
		addPlayer(new Player(0, "GK", 106, 281, this.mRedPlayerTextureRegion), scene, mRedTeam);
		addPlayer(new Player(0, "", 317, 131, this.mRedPlayerTextureRegion), scene, mRedTeam);
		addPlayer(new Player(0, "", 272, 204, this.mRedPlayerTextureRegion), scene, mRedTeam);
		addPlayer(new Player(0, "", 230, 273, this.mRedPlayerTextureRegion), scene, mRedTeam);
		addPlayer(new Player(0, "", 272, 347, this.mRedPlayerTextureRegion), scene, mRedTeam);
		addPlayer(new Player(0, "", 317, 425, this.mRedPlayerTextureRegion), scene, mRedTeam);
		addPlayer(new Player(0, "", 410, 280, this.mRedPlayerTextureRegion), scene, mRedTeam);
		addPlayer(new Player(0, "", 460, 95, this.mRedPlayerTextureRegion), scene, mRedTeam);
		addPlayer(new Player(0, "", 460, 210, this.mRedPlayerTextureRegion), scene, mRedTeam);
		addPlayer(new Player(0, "", 460, 325, this.mRedPlayerTextureRegion), scene, mRedTeam);
		addPlayer(new Player(0, "", 460, 440, this.mRedPlayerTextureRegion), scene, mRedTeam);

		addPlayer(new Player(0, "GK", 875, 281, this.mBluePlayerTextureRegion), scene, mBlueTeam);
		addPlayer(new Player(0, "GK", 748, 277, this.mBluePlayerTextureRegion), scene, mBlueTeam);
		addPlayer(new Player(0, "GK", 703, 206, this.mBluePlayerTextureRegion), scene, mBlueTeam);
		addPlayer(new Player(0, "GK", 703, 345, this.mBluePlayerTextureRegion), scene, mBlueTeam);
		addPlayer(new Player(0, "GK", 656, 138, this.mBluePlayerTextureRegion), scene, mBlueTeam);
		addPlayer(new Player(0, "GK", 656, 420, this.mBluePlayerTextureRegion), scene, mBlueTeam);
		addPlayer(new Player(0, "GK", 571, 275, this.mBluePlayerTextureRegion), scene, mBlueTeam);
		addPlayer(new Player(0, "GK", 517, 95, this.mBluePlayerTextureRegion), scene, mBlueTeam);
		addPlayer(new Player(0, "GK", 517, 210, this.mBluePlayerTextureRegion), scene, mBlueTeam);
		addPlayer(new Player(0, "GK", 517, 325, this.mBluePlayerTextureRegion), scene, mBlueTeam);
		addPlayer(new Player(0, "GK", 517, 440, this.mBluePlayerTextureRegion), scene, mBlueTeam);

		scene.getTopLayer().addEntity(ball);
		scene.setOnAreaTouchTraversalFrontToBack();
		scene.registerTouchArea(ball);

		/*scene.registerUpdateHandler(new IUpdateHandler(){
			@Override
			public void reset(){}
			@Override
			public void onUpdate(final float pSecondsElapsed){

				for(Player p: mRedTeam){
					if(ball.collidesWith(p)){
						ball.setColor(1, 0, 0);
					}
				}
				for(Player p: mBlueTeam){
					if(ball.collidesWith(p)){
						ball.setColor(0, 0, 1);
					}
				}
			}
		});*/

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}
	
	
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	public void saveFormation(){
		
		for(Player p:mRedTeam){
			System.out.println(p.getX() + " " + p.getY());
		}
		
		
		
	}
	
	public void loadFormation(){
		
		
		
	}
	
	public void addPlayer(Player p, Scene scene, List<Player> list){
		scene.getTopLayer().addEntity(p);
		scene.registerTouchArea(p);
		list.add(p);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
