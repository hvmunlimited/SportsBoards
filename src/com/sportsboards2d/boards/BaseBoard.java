package com.sportsboards2d.boards;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchException;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.HoldDetector;
import org.anddev.andengine.input.touch.detector.HoldDetector.IHoldDetectorListener;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sportsboards2d.R;
import com.sportsboards2d.db.Formation;
import com.sportsboards2d.db.PlayerInfo;
import com.sportsboards2d.db.parsing.XMLAccess;
import com.sportsboards2d.db.parsing.XMLWriter;
import com.sportsboards2d.sprites.BallSprite;
import com.sportsboards2d.sprites.PlayerSprite;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public abstract class BaseBoard extends Interface{
	
	// ===========================================================
	// Constants
	// ===========================================================
	protected static final int CAMERA_WIDTH = 1024;
	protected static final int CAMERA_HEIGHT = 600;
	protected static final int MENU_RESET = 0;
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected int NUM_PLAYERS;
	protected String SPORT_NAME;
	protected String BALL_PATH;
	protected int resID;
	protected final String DEFAULT_NAME = "DEFAULT";
		
	protected Texture mBackgroundTexture;
	protected Texture mBallTexture;
	private Texture mRedPlayerTexture;
	private Texture mBluePlayerTexture;
	private Texture mPlayerMenuFont;
	
	private Font mFont;
	
	protected TextureRegion mBackGroundTextureRegion;
	protected TextureRegion mBallTextureRegion;
	private TextureRegion mRedPlayerTextureRegion;
	private TextureRegion mBluePlayerTextureRegion;
	
	private List<PlayerSprite> players = new ArrayList<PlayerSprite>();
	
	private BallSprite ball;
	
	private ZoomCamera mZoomCamera;
	private Camera mPlayerCamera;
	
	private Scene mMainScene;
	private boolean LARGE_PLAYERS = true;
	
	private MenuScene mPlayerMenu;

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
		
		//load menu textures
		this.mPlayerMenuFont = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(this.mPlayerMenuFont, this, "Droid.ttf", 48, true, Color.RED);
		this.mEngine.getTextureManager().loadTexture(this.mPlayerMenuFont);
		this.mEngine.getFontManager().loadFont(this.mFont);
		
		//load player and ball textures
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
		super.onLoadScene();
		this.mMainScene = new Scene(1);
		
		this.mMainScene.setOnSceneTouchListener(this);
		this.mMainScene.setTouchAreaBindingEnabled(true);
		
		mMainScene.getLayer(0).addEntity(new Sprite(0, 0, this.mBackGroundTextureRegion));
		
		fn = loadFormation();
		showFormation(fn);
		
		return mMainScene;
	}
	
	public void reset(){
		Formation fn = null;
		for(PlayerSprite p: players){
			this.mMainScene.getTopLayer().removeEntity(p);	
		}
		this.mMainScene.getTopLayer().removeEntity(this.ball);
		
		this.ball = null;
		this.players.clear();
		fn = loadFormation();
		showFormation(fn);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		 MenuInflater inflater = getMenuInflater();
		 inflater.inflate(R.layout.settings_menu, pMenu);
		 return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()) {
			case R.id.reset:
				reset();
				return true;
			
			case R.id.change_player_size:
				
				if(LARGE_PLAYERS){
					
					this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "32x32RED.png", 0, 0);
					this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mBluePlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mBluePlayerTexture, this, "32x32BLUE.png", 0, 0);
					this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, BALL_PATH + "32.png", 0, 0);

					LARGE_PLAYERS = false;
				}
				else{
					
					this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "48x48RED.png", 0, 0);
					this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mBluePlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mBluePlayerTexture, this, "48x48BLUE.png", 0, 0);
					this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, BALL_PATH + "48.png", 0, 0);

					LARGE_PLAYERS = true;
					
				}
				this.mEngine.getTextureManager().loadTextures(this.mRedPlayerTexture, this.mBluePlayerTexture, this.mBallTexture);
				
				reset();
				
				return true;
				
			default:
				return false;
		}
	}
	
	// ===========================================================
	// Methods 
	// ===========================================================
	
	
	/*
	 * Capture player & ball positions, save them in XML format to internal storage
	 */
	
	public void saveFormation(){
		
		Formation fn = new Formation();
		ArrayList<PlayerInfo> playerList = new ArrayList<PlayerInfo>();
		PlayerSprite pSprite = null;
		PlayerInfo pInfo = null;
		BallSprite ball = null;
		
		for(int i = 0; i < mMainScene.getTopLayer().getEntityCount(); i++){
			if((IEntity) mMainScene.getTopLayer().getEntity(i) instanceof PlayerSprite){
				pSprite = (PlayerSprite)mMainScene.getTopLayer().getEntity(i);
				pInfo = pSprite.getPlayerInfo();
				pInfo.setCoords(pSprite.getX(), pSprite.getY());
				playerList.add(pInfo);
			}
			else if((IEntity) mMainScene.getTopLayer().getEntity(i) instanceof BallSprite){
				ball = (BallSprite)mMainScene.getTopLayer().getEntity(i);
			}
		}
		fn.setBall(ball.getX(), ball.getY());
		fn.setPlayers(playerList);
		fn.setName("testing");
		XMLWriter.writeFormation(this, fn, SPORT_NAME.toLowerCase());
	}
	
	public Formation loadFormation(){
		
		ArrayList<Formation> formsList = (ArrayList<Formation>) XMLAccess.loadFormations(this, resID);
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
		
		TextureRegion tex = null;
		addBall(new BallSprite(fn.getBall().getX(), fn.getBall().getY(), this.mBallTextureRegion));
		for(PlayerInfo p:fn.getPlayers()){
			
			if(p.getTeamColor().equalsIgnoreCase("blue")){
				tex = this.mBluePlayerTextureRegion;
			}
			else if(p.getTeamColor().equalsIgnoreCase("red")){
				tex = this.mRedPlayerTextureRegion;
			}		
			addPlayer(p, tex);

		}
	}
	
	public void addPlayer(PlayerInfo p, TextureRegion tex){
		
		Line line;
		
		PlayerSprite newPlayer = new PlayerSprite(p, p.getX(), p.getY(), tex){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				float x1, y1, x2, y2;
				Line line = null;
				mHold.onTouchEvent(pSceneTouchEvent);
				switch(pSceneTouchEvent.getAction()) {
					case TouchEvent.ACTION_DOWN:
						x1 = this.getX();
						y1 = this.getY();
						this.setScale(2.0f);	
						this.mGrabbed = true;
						System.out.println(this.getX() + " , " + this.getY());

						break;
					case TouchEvent.ACTION_MOVE:
						if(this.mGrabbed) {

							this.setPosition(pSceneTouchEvent.getX() - 48 / 2, pSceneTouchEvent.getY() - 48 / 2);
							//System.out.println(this.getX() + " , " + this.getY());
						}
						break;
					case TouchEvent.ACTION_UP:
						if(this.mGrabbed) {
							x2 = this.getX();
							y2 = this.getY();
							this.mGrabbed = false;
							this.setScale(1.0f);					
						}
						break;
				}
				return true;
			}
		};
		
		newPlayer.setHoldDetector(new HoldDetector(new IHoldDetectorListener() {
			
			@Override
			public void onHold(HoldDetector arg0, long arg1, float arg2, float arg3) {
				
				//mPlayerMenu.back();
			}

			@Override
			public void onHoldFinished(HoldDetector arg0, long arg1, float arg2, float arg3) {
				
				mPlayerMenu = createPlayerMenu();
				//mPlayerMenu.setOnMenuItemClickListener(p);
				mMainScene.setChildScene(mPlayerMenu, false, true, true);
			}
		}));
		//mPlayerMenu.setOnMenuItemClickListener(newPlayer);
		mMainScene.getTopLayer().addEntity(newPlayer);
		mMainScene.registerTouchArea(newPlayer);
		mMainScene.registerUpdateHandler(newPlayer.getHoldDetector());
		//createPlayerMenu(newPlayer);
		players.add(newPlayer);
	}
	
	public void addBall(BallSprite ball){
		
		this.ball = ball;
		mMainScene.getTopLayer().addEntity(ball);
		mMainScene.registerTouchArea(ball);
	}
	
	public MenuScene createPlayerMenu(){
		
		
		
		mPlayerMenu = new MenuScene(mZoomCamera);
		
		final IMenuItem resetMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_RESET, this.mFont, "DELETE"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		resetMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mPlayerMenu.addMenuItem(resetMenuItem);
		
		mPlayerMenu.buildAnimations();

		mPlayerMenu.setBackgroundEnabled(false);

		return mPlayerMenu;
	}
	
}

