package com.sportsboards2d.boards;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.util.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.sportsboards2d.R;
import com.sportsboards2d.db.Coordinates;
import com.sportsboards2d.db.PlayerInfo;
import com.sportsboards2d.sprites.PlayerSprite;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public class testingboard extends BaseBoard{
	
	
	private Line arrowLineMain;
	private Line arrowLineWingLeft;
	private Line arrowLineWingRight;
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public Engine onLoadEngine() {
		NUM_PLAYERS = 5;
		resID = R.raw.basketball;
		SPORT_NAME = "BASKETBALL";
		BALL_PATH = "Basketball_Ball_";
		return super.onLoadEngine();
	}
	

	@Override
	public void onLoadResources() {
		super.onLoadResources();	
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "basketball_court.jpg", 0, 0);
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture);
	}
	@Override
	public Scene onLoadScene(){
		super.onLoadScene();
		this.mMainScene.setBackground(new ColorBackground(100, 100, 100));
		
		return this.mMainScene;
	}
	
	public void reloadBall(){
		
		this.mBallTexture.clearTextureSources();
		this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallTexture, this, "Basketball_Ball_48.png", 0, 0, 1, 1);
		
	}
	
	@Override
	public void onLoadComplete() {

		this.mMainScene.getChild(BACKGROUND_LAYER).detachChildren();
		
		this.mMainScene.setBackground(new ColorBackground(0, 0, 0));
		
		clearScene();
		
		createPlayer(new PlayerInfo("", "", "", new Coordinates(400, 400)), this.mRedPlayerTextureRegion);

		
		float x = selectedPlayer.getX();
		float y = selectedPlayer.getY();
		
		arrowLineMain = new Line(0, 0, 0, 0, 10);
		arrowLineWingLeft = new Line(0, 0, 0, 0, 10);
		arrowLineWingRight = new Line(0, 0, 0, 0, 10);

		this.mMainScene.getChild(LINE_LAYER).attachChild(arrowLineMain);
		this.mMainScene.getChild(LINE_LAYER).attachChild(arrowLineWingLeft);
		this.mMainScene.getChild(LINE_LAYER).attachChild(arrowLineWingRight);
		
		arrowLineMain.setPosition(x, y, x, y - 300);
		arrowLineWingLeft.setPosition(x, y, x -50, y - 50);
		arrowLineWingRight.setPosition(x, y, x +50, y - 50);

		//this.mMainScene.setChildScene(analog);

		//
		
	}
	
	
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

		PlayerSprite sprite = null; 
			
		float[]Xs = null;
		float[]Ys = null;
		
		float moveX, moveY;
		moveX = 0;
		moveY = 0;
		float diff;
		float angleDeg;
		
	
		if((pTouchArea) instanceof PlayerSprite){
			
			sprite = (PlayerSprite) pTouchArea;
			selectedPlayer = sprite;
			Body body = this.mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(sprite);
			
			switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					
					sprite.setScale(2.0f);	
					sprite.setmGrabbed(true);
					
					//sprite.displayInfo(true);
					path.clear();
					sprite.setStartX(sprite.getX());
					sprite.setStartY(sprite.getY());
					
					
					return true;
					
				case TouchEvent.ACTION_MOVE:
					
					if(sprite.ismGrabbed()) {
	
						sprite.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						body.setTransform(new Vector2(pSceneTouchEvent.getX()/32,  pSceneTouchEvent.getY()/32), 0);
						
						
						moveX = sprite.getX();
						moveY = sprite.getY();
						
						float relativeX = MathUtils.bringToBounds(0, sprite.getWidth(), pTouchAreaLocalX) / sprite.getWidth() - 0.5f;
						float relativeY = MathUtils.bringToBounds(0, sprite.getHeight(), pTouchAreaLocalY) / sprite.getHeight() - 0.5f;

						relativeX = relativeX * 2;
						relativeY = relativeY * 2;
						
						angleDeg = MathUtils.radToDeg((float)Math.atan2(-relativeX, relativeY));

						
						System.out.println("My pX: " +relativeX);
						
						path.add(new Coordinates(sprite.getX(), sprite.getY()));
						
						arrowLineMain.setPosition(moveX, moveY, moveX, moveY - 300);
						arrowLineWingLeft.setPosition(moveX, moveY, moveX - 50, moveY - 50);
						arrowLineWingRight.setPosition(moveX, moveY, moveX + 50, moveY - 50);
												
						
						
						
						if(LINE_ENABLED){
						
							diff = MathUtils.distance(sprite.getStartX(), sprite.getStartY(), moveX, moveY);
							
							if(diff > 30){
								arrowLineMain.setRotation(angleDeg);
								arrowLineWingLeft.setRotation(angleDeg);
								arrowLineWingRight.setRotation(angleDeg);

								//arrowLineMain = LineFactory.createLine(sprite.getStartX(), sprite.getStartY(), moveX, moveY, 8);
								//mMainScene.getChild(LINE_LAYER).attachChild(arrowLineMain);
								sprite.setStartX(moveX);
								sprite.setStartY(moveY);
							}
						}
						
					}
				
					return true;
					
				case TouchEvent.ACTION_UP:
					
					if(sprite.ismGrabbed()) {
						sprite.setmGrabbed(false);
						sprite.setScale(1.0f);
						
		
						//sprite.displayInfo(false);

					}
					return true;
				}
		
		}
		
		return false;
	}
}
