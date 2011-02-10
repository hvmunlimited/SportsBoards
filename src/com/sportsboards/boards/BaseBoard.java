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
import org.anddev.andengine.entity.sprite.Sprite;
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
import com.sportsboards.db.Formation;
import com.sportsboards.db.Position;
import com.sportsboards.db.XMLAccess;
import com.sportsboards.sprites.Ball;
import com.sportsboards.sprites.PlayerSprite;


/**
 * @author Mike Bonar
 * 
 */
public abstract class BaseBoard extends BaseGameActivity implements IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener{
	// ===========================================================
	// Constants
	// ===========================================================
	protected static final int CAMERA_WIDTH = 1024;
	protected static final int CAMERA_HEIGHT = 600;
	
	protected String SPORT_NAME;
	protected final String DEFAULT_NAME = "DEFAULT";
		
	protected Texture mBackgroundTexture;
	protected Texture mBallTexture;
	private Texture mRedPlayerTexture;
	private Texture mBluePlayerTexture;
	
	protected TextureRegion mBackGroundTextureRegion;
	protected TextureRegion mBallTextureRegion;
	private TextureRegion mRedPlayerTextureRegion;
	private TextureRegion mBluePlayerTextureRegion;
	
	private List<PlayerSprite> mRedTeam = new ArrayList<PlayerSprite>();
	private List<PlayerSprite> mBlueTeam = new ArrayList<PlayerSprite>();
	
	private ZoomCamera mZoomCamera;
	private SurfaceScrollDetector mScrollDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private float mPinchZoomStartedCameraZoomFactor;
	
	private Scene mMainScene;
	private boolean LARGE_PLAYERS = true;
	
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
		this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "48x48RED.png", 0, 0);
		this.mBluePlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mBluePlayerTexture, this, "48x48BLUE.png", 0, 0);
		
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture, this.mBluePlayerTexture, this.mRedPlayerTexture, this.mBallTexture);
	}
	@Override
	public Scene onLoadScene(){
		
		Formation fn = null;
		
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
		
		mMainScene.getLayer(0).addEntity(new Sprite(0, 0, this.mBackGroundTextureRegion));
		
		fn = loadFormation();
		showFormation(fn);
		
		mMainScene.setOnAreaTouchTraversalFrontToBack();
	
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
	
	public Formation loadFormation(){
		
		XMLAccess xml = new XMLAccess();
		ArrayList<Formation> formsList = (ArrayList<Formation>) xml.loadFormations(this, SPORT_NAME.toLowerCase());
		Formation def = null;
		
		for(Formation fn:formsList){
			if(fn.getName().equalsIgnoreCase(DEFAULT_NAME)){
				def = fn;
				break;
			}
		}
		return def;
	}
	
	public void showFormation(Formation fn){
				
		addBall(new Ball(fn.getBall().x, fn.getBall().y, this.mBallTextureRegion));
		
		for(Position pos:fn.getPositions()){
			
			if(pos.getTeamColor().equalsIgnoreCase("blue")){
				addPlayer(new PlayerSprite(0, pos.getType(), pos.getX(), pos.getY(), 
							this.mBluePlayerTextureRegion), mBlueTeam);
			}
			else{
				addPlayer(new PlayerSprite(0, pos.getType(), pos.getX(), pos.getY(),
							this.mRedPlayerTextureRegion), mRedTeam);
			}			
		}
	}
	
	public void addPlayer(PlayerSprite p, List<PlayerSprite> list){
		mMainScene.getTopLayer().addEntity(p);
		mMainScene.registerTouchArea(p);
		list.add(p);
	}
	public void addBall(Ball ball){
		mMainScene.getTopLayer().addEntity(ball);
		mMainScene.registerTouchArea(ball);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		 MenuInflater inflater = getMenuInflater();
		 inflater.inflate(R.layout.settings, pMenu);
		 return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		
		Formation fn = null;
		
		switch(item.getItemId()) {
			case R.id.reset:
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			case R.id.save:
				return false;
			case R.id.load:
				return false;
			case R.id.enable:
				return false;
			case R.id.disable:
				return false;
			case R.id.change_player_size:
				
				if(LARGE_PLAYERS){
					
					this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "32x32RED.png", 0, 0);
					this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mBluePlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mBluePlayerTexture, this, "32x32BLUE.png", 0, 0);
					LARGE_PLAYERS = false;
				}
				else{
					
					this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "48x48RED.png", 0, 0);
					this.mBluePlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mBluePlayerTexture, this, "48x48BLUE.png", 0, 0);
					LARGE_PLAYERS = true;
				}
				this.mEngine.getTextureManager().loadTextures(this.mRedPlayerTexture, this.mBluePlayerTexture);
				
				List<PlayerSprite> tempList = new ArrayList<PlayerSprite>();
				
				for(PlayerSprite p: mRedTeam){
					this.mMainScene.getTopLayer().removeEntity(p);	
					tempList.add(p);
				}
				for(PlayerSprite p: mBlueTeam){
					this.mMainScene.getTopLayer().removeEntity(p);
				}
				mBlueTeam.clear();
				mRedTeam.clear();
				fn = loadFormation();
				showFormation(fn);
				
			default:
				return false;
		}
	}
}

