package com.sportsboards;

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
		this.NUM_PLAYERS = 5;
		return super.onLoadEngine();
	}
	

	@Override
	public void onLoadResources() {
		super.onLoadResources();		
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "soccerboard_background_1024_600.png", 0, 0);
		this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, "soccer_ball_48_48.png", 0, 0);
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture);
	}
	
	@Override
	public Scene onLoadScene() {
		
		if(mRedTeam.isEmpty()){
		
			final Scene scene = super.onLoadScene();
			scene.getLayer(0).addEntity(new Sprite(0, 0, this.mBackGroundTextureRegion));
			
			final int centerX = (CAMERA_WIDTH - this.mBallTextureRegion.getWidth()) / 2 - 100;
			final int centerY = (CAMERA_HEIGHT - this.mBallTextureRegion.getHeight()) / 2;
			final Ball ball = new Ball(centerX, centerY, this.mBallTextureRegion);
						
			int startx = 1;
			int starty = 512;
			
			int interval = 102;
			
			for(int i = 0; i < NUM_PLAYERS; i++){
			
				final Player red_player = new Player(0, "", startx, starty, this.mRedPlayerTextureRegion);
				scene.getTopLayer().addEntity(red_player);
				scene.registerTouchArea(red_player);
				mRedTeam.add(red_player);
				startx += interval;
			}
			
			for(int i = 0; i < NUM_PLAYERS; i++){
				
				final Player blue_player = new Player(0, "", startx, starty, this.mBluePlayerTextureRegion);
				scene.getTopLayer().addEntity(blue_player);
				scene.registerTouchArea(blue_player);
				mBlueTeam.add(blue_player);
				startx += interval;
			}
			
			scene.getTopLayer().addEntity(ball);
			scene.setOnAreaTouchTraversalFrontToBack();
			scene.registerTouchArea(ball);
			
			scene.registerUpdateHandler(new IUpdateHandler(){
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
			});
			
		return scene;
		}
		return null;
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
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
