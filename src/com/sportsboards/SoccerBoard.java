package com.sportsboards;

import java.util.HashMap;
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

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Texture mCardDeckTexture;
	private HashMap<Card, TextureRegion> mCardTotextureRegionMap;
	
	
	private Texture mTexture;
	private Texture mBallTexture;
	private Texture mBackgroundTexture;
	private TextureRegion mBallTextureRegion;
	private TextureRegion mBackGroundTextureRegion;
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
		
		this.mTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegionFactory.setAssetBasePath("gfx/");
		this.mBackgroundTexture = new Texture(1024, 512, TextureOptions.DEFAULT);
		this.mBackGroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "soccer_field.png", 0, 0);
		
		this.mBallTexture = new Texture(128, 128, TextureOptions.BILINEAR);
		this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, "soccer_ball.png", 0, 0);
		
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture, this.mTexture, this.mBallTexture);
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
		
		
		scene.getTopLayer().addEntity(ball);
		scene.setOnAreaTouchTraversalFrontToBack();
		scene.registerTouchArea(ball);
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
