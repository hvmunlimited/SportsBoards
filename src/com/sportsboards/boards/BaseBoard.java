package com.sportsboards.boards;

import java.util.ArrayList;
import java.util.List;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchException;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.ScrollDetector;
import org.anddev.andengine.input.touch.detector.SurfaceScrollDetector;
import org.anddev.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sportsboards.R;
import com.sportsboards.sprites.Player;

/**
 * @author Mike Bonar
 * 
 */
public abstract class BaseBoard extends BaseGameActivity implements IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener{
	// ===========================================================
	// Constants
	// ===========================================================
	public static final int CAMERA_WIDTH = 1024;
	public static final int CAMERA_HEIGHT = 600;
	
	protected int NUM_PLAYERS;
	protected Texture mBackgroundTexture;
	protected Texture mBallTexture;
	protected Texture mRedPlayerTexture;
	protected Texture mBluePlayerTexture;
	
	protected TextureRegion mBackGroundTextureRegion;
	protected TextureRegion mBallTextureRegion;
	protected TextureRegion mRedPlayerTextureRegion;
	protected TextureRegion mBluePlayerTextureRegion;
	
	protected List<Player> mRedTeam = new ArrayList<Player>();
	protected List<Player> mBlueTeam = new ArrayList<Player>();
	
	private ZoomCamera mZoomCamera;
	private SurfaceScrollDetector mScrollDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private float mPinchZoomStartedCameraZoomFactor;
	
	protected Scene mMainScene;
	
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
		this.mZoomCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final Engine engine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mZoomCamera));
		
		try {
			if(MultiTouch.isSupported(this)) {
				engine.setTouchController(new MultiTouchController());
				
			} else {
				
			}
		} catch (final MultiTouchException e) {
		}
		return engine;
	}
	@Override
	public void onLoadResources(){
		TextureRegionFactory.setAssetBasePath("gfx/");
		this.mBackgroundTexture = new Texture(1024, 1024, TextureOptions.DEFAULT);
		this.mBallTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "48x48BLUE.png", 0, 0);
		this.mBluePlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mBluePlayerTexture, this, "48x48RED.png", 0, 0);
		
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture, this.mBluePlayerTexture, this.mRedPlayerTexture, this.mBallTexture);
	}
	@Override
	public Scene onLoadScene(){
		
		this.mMainScene = new Scene(1);
		this.mScrollDetector = new SurfaceScrollDetector(this);
		if(MultiTouch.isSupportedByAndroidVersion()) {
			try {
				this.mPinchZoomDetector = new PinchZoomDetector(this);
			} catch (final MultiTouchException e) {
				this.mPinchZoomDetector = null;
			}
		} else {
			this.mPinchZoomDetector = null;
		}
		this.mMainScene.setOnSceneTouchListener(this);
		this.mMainScene.setTouchAreaBindingEnabled(true);
		this.mPinchZoomDetector.setEnabled(false);
		this.mScrollDetector.setEnabled(false);
		
		return mMainScene;
	}
	
	@Override
	public void onScroll(final ScrollDetector pScollDetector, final TouchEvent pTouchEvent, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
		this.mPinchZoomStartedCameraZoomFactor = this.mZoomCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		if(this.mZoomCamera.getZoomFactor() < 1.0f){
			this.mZoomCamera.setZoomFactor(1.0f);
		}
		else{
			this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
		}
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
		if(this.mZoomCamera.getZoomFactor() > 3.0f){
			this.mZoomCamera.setZoomFactor(1.0f);
		}
		else if(this.mZoomCamera.getZoomFactor() < 1.0f){
			this.mZoomCamera.setZoomFactor(1.0f);
		}
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		
		if(this.mPinchZoomDetector != null) {
			this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

			if(this.mPinchZoomDetector.isZooming()) {
				this.mScrollDetector.setEnabled(false);
			} else {
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					//this.mScrollDetector.setEnabled(true);
				}
				this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
			}
		} else {
			this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		}
		
		return true;
	}

	public void saveFormation(){}
	public void loadFormation(){}
	
	public void addPlayer(Player p, List<Player> list){
		mMainScene.getTopLayer().addEntity(p);
		mMainScene.registerTouchArea(p);
		list.add(p);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		 MenuInflater inflater = getMenuInflater();
		 inflater.inflate(R.layout.settings, pMenu);
		 return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()) {
			case R.id.reset:
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			case R.id.save:
				
			case R.id.load:
				
			case R.id.enable:
				
			case R.id.disable:
				
				
			default:
				return false;
		}
	}
	
}

