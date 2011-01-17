package com.sportsboards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

/**
 * @author Mike Bonar
 * 
 */
public class SoccerBoard extends BaseBoard {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 1024;
	private static final int CAMERA_HEIGHT = 600;
	
	private static final int RED_START_X = 1;
	private static final int RED_START_Y = 512;
	
	private static final int BLUE_START_X = 512;
	private static final int BLUE_START_Y = 512;
	
	private static final int INTERVAL = 32;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private HashMap<Card, TextureRegion> mCardTotextureRegionMap;
	
	private Texture mBackgroundTexture;
	private Texture mBallTexture;
	private Texture mRedPlayerTexture;
	private Texture mBluePlayerTexture;
	
	private TextureRegion mBackGroundTextureRegion;
	private TextureRegion mBallTextureRegion;
	private TextureRegion mRedPlayerTextureRegion;
	private TextureRegion mBluePlayerTextureRegion;
	
	private List<Sprite> mRedSpritesList = new ArrayList<Sprite>();
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
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final Engine engine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));

		
		
		
		
		

		return engine;
	}

	@Override
	public void onLoadResources() {
		
		TextureRegionFactory.setAssetBasePath("gfx/");
		//load background textures
		this.mBackgroundTexture = new Texture(1024, 1024, TextureOptions.DEFAULT);
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "soccerboard_background_1024_600.png", 0, 0);
		//load ball textures
		this.mBallTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, "soccer_ball_48_48.png", 0, 0);
		//load player textures
		
		this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);

		this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "red_player.png", 0, 0);
		this.mBluePlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mBluePlayerTexture, this, "blue_player.png", 0, 0);

		
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture, this.mBluePlayerTexture, this.mRedPlayerTexture, this.mBallTexture);
	}
	
	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		final Scene scene = new Scene(1);
		scene.getLayer(0).addEntity(new Sprite(0, 0, this.mBackGroundTextureRegion));
		
		
		
		
		final int centerX = (CAMERA_WIDTH - this.mBallTextureRegion.getWidth()) / 2 - 100;
		final int centerY = (CAMERA_HEIGHT - this.mBallTextureRegion.getHeight()) / 2;
		final Sprite ball = new Sprite(centerX, centerY, this.mBallTextureRegion){
					
		@Override
		public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
			return true;
		}
		};
		
		for(int i = 0; i < 5; i++){
			//Sprite red_player = new Sprite(RED_START_X, RED_START_Y)
		}
		
		final Sprite red_player = new Sprite(1, 512, this.mRedPlayerTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
				return true;
			}
			
		};
		final Sprite blue_player = new Sprite(512, 512, this.mBluePlayerTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
				return true;
			}
			
		};
		
		scene.getTopLayer().addEntity(ball);
		scene.getTopLayer().addEntity(red_player);
		scene.getTopLayer().addEntity(blue_player);

		scene.setOnAreaTouchTraversalFrontToBack();
		
		scene.registerTouchArea(ball);
		scene.registerTouchArea(red_player);
		scene.registerTouchArea(blue_player);

		scene.setTouchAreaBindingEnabled(true);
	//
		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void addPlayer(){
	}
	@Override
	public void addBall(){
		
	}
	
	private void addCard(final Scene pScene, final Card pCard, final int pX, final int pY) {
		final Sprite sprite = new Sprite(pX, pY, this.mCardTotextureRegionMap.get(pCard)) {
			boolean mGrabbed = false;
			
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
					case TouchEvent.ACTION_DOWN:
						this.setScale(1.25f);
						this.mGrabbed = true;
						break;
					case TouchEvent.ACTION_MOVE:
						if(this.mGrabbed) {
							this.setPosition(pSceneTouchEvent.getX() - Card.CARD_WIDTH / 2, pSceneTouchEvent.getY() - Card.CARD_HEIGHT / 2);
						}
						break;
					case TouchEvent.ACTION_UP:
						if(this.mGrabbed) {
							this.mGrabbed = false;
							this.setScale(1.0f);
						}
						break;
				}
				return true;
			}
		};

		pScene.getTopLayer().addEntity(sprite);
		pScene.registerTouchArea(sprite);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
