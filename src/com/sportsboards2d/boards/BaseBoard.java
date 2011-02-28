package com.sportsboards2d.boards;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
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
import org.anddev.andengine.util.MathUtils;

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
import com.sportsboards2d.util.Math;

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
	
	private Formation currentFormation;
	private List<PlayerSprite> players = new ArrayList<PlayerSprite>();
	private List<Line>lines = new ArrayList<Line>();
	private BallSprite ball;
	
	private ZoomCamera mZoomCamera;
	private Camera mPlayerCamera;
	
	private Scene mMainScene;
	private boolean LARGE_PLAYERS = true;
	
	private MenuScene mPlayerMenu;
	
	private AnalogOnScreenControl analog;
	
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
		this.mFont = FontFactory.createFromAsset(this.mPlayerMenuFont, this, "Droid.ttf", 96, true, Color.RED);
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
		
		super.onLoadScene();
		this.mMainScene = new Scene(1);
		
		this.mMainScene.setOnSceneTouchListener(this);
		this.mMainScene.setTouchAreaBindingEnabled(true);
		
		mMainScene.getLayer(0).addEntity(new Sprite(0, 0, this.mBackGroundTextureRegion));
		
		currentFormation = loadFormation();
		showFormation(currentFormation);
		
		//arrowLineWingLeft = new Line(0, 0, 0, 8);
		//arrowLineWingRight = new Line(0, 0, 0, 8);
		
		return mMainScene;
	}
	
	public void clearScene(){
		
		for(PlayerSprite p: players){
			
			this.mMainScene.getTopLayer().removeEntity(p);	
		}
		
		this.mMainScene.getTopLayer().removeEntity(this.ball);
		
		for(Line line: lines){
			this.mMainScene.getTopLayer().removeEntity(line);
		}
		
		this.ball = null;
		this.players.clear();
		this.lines.clear();
		
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
				
				this.mMainScene.reset();
				
				
				return true;
			
			case R.id.change_player_size:
				
				clearScene();
				this.mRedPlayerTexture.clearTextureSources();
				this.mBluePlayerTexture.clearTextureSources();
				this.mBallTexture.clearTextureSources();
				
				this.mEngine.getTextureManager().unloadTextures(this.mRedPlayerTexture, this.mBluePlayerTexture, this.mBallTexture);
				
				if(LARGE_PLAYERS){
					
					this.mRedPlayerTexture = new Texture(32, 32, TextureOptions.BILINEAR);
					this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "32x32RED.png", 0, 0);
					this.mBluePlayerTexture = new Texture(32, 32, TextureOptions.BILINEAR);
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
				
				showFormation(currentFormation);
				
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
	
	public void addPlayer(final PlayerInfo p, TextureRegion tex){
		
		
				
		PlayerSprite newPlayer = new PlayerSprite(p, tex){
			
			float startX, startY, endX, endY;
			float moveX, moveY;
			Line arrowLineMain = null;
			Line arrowLineWingLeft = null;
			Line arrowLineWingRight = null;
			float xDiff, yDiff;
			float diff;
			float leftWingX, leftWingY;
			float rightWingX, rightWingY;
			float angleRad, angleDeg;
			

			@Override
			
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) throws NullPointerException{
				
				mHold.onTouchEvent(pSceneTouchEvent);
				switch(pSceneTouchEvent.getAction()) {
					case TouchEvent.ACTION_DOWN:
						
						this.setScale(2.0f);	
						this.mGrabbed = true;
						this.setPosition(pSceneTouchEvent.getX()-24, pSceneTouchEvent.getY()-24);
						
						startX = this.getX();
						startY = this.getY();
						
						//arrowLineWingLeft = new Line(startX, startY, startX-30, startY-30, 8);
						//arrowLineWingRight = new Line(startX, startY, startX+30, startY-30, 8);
						
						
						break;
					case TouchEvent.ACTION_MOVE:
						if(this.mGrabbed) {

							this.setPosition(pSceneTouchEvent.getX()-24, pSceneTouchEvent.getY()-24);
							moveX = this.getX();
							moveY = this.getY();
							
							diff = MathUtils.distance(startX, startY, moveX, moveY);
							
							if(diff > 50){
								
								arrowLineMain = new Line(startX+24, startY+24, moveX+24, moveY+24, 8);
								
								mMainScene.getTopLayer().addEntity(arrowLineMain);
								lines.add(arrowLineMain);
								startX = moveX;
								startY = moveY;
							}
							
							//startX = moveX;
							//startY = moveY;
							//System.out.println(this.getX() + " , " + this.getY());
						}
						break;
					case TouchEvent.ACTION_UP:
						if(this.mGrabbed) {
							this.mGrabbed = false;
							this.setScale(1.0f);
							endX = this.getX()+24;
							endY = this.getY()+24;
							
							diff = MathUtils.distance(startX, startY, endX, endY);
							
							System.out.println("Distance between start and end: " + diff);
							
							xDiff = Math.calculateDifference(startX, endX) / diff;
							yDiff = Math.calculateDifference(startY, endY) / diff;
							
							angleRad = (float) java.lang.Math.atan2(-xDiff, yDiff);
							angleDeg = MathUtils.radToDeg(angleRad);
							
							//System.out.println("xDiff: " + xDiff)
							
							System.out.println("pValueX: " + xDiff);
							System.out.println("pValueY: " + yDiff);
							System.out.println("Angle in radians: " + angleRad);
							System.out.println("Angle in degrees: " + angleDeg);
							
							//arrowLineWingLeft.setPosition(endX, endY, endX-30, endY-30);
							//arrowLineWingRight.setPosition(endX, endY, endX+30, endY-30);
							
							//arrowLineWingLeft.setRotation(angleDeg);
							//arrowLineWingRight.setRotation(angleDeg);
							
							//mMainScene.getTopLayer().addEntity(arrowLineWingLeft);
							//mMainScene.getTopLayer().addEntity(arrowLineWingRight);
							
							
							
							//diff = Math.calculateRotation(startx, starty, x2, y2);
							
							//arrowLineWingLeft.setRotation(diff-90);
							//arrowLineWingRight.setRotation(diff-60);
							
							//arrowLineWingLeft.setPosition(x2, y2, x2-30, y2-30);
							//arrowLineWingRight.setPosition(x2, y2, x2+30, y2-30);
							
							
							/*
							if(diff > 90 && diff < 180){ 	//between 90 and 180
								arrowLineWingLeft.setRotation(diff-90);
								arrowLineWingRight.setRotation(diff-60);
							}
							else if(diff < 90 && diff > 0){ // between 0 and 90
								arrowLineWingLeft.setRotation(diff+90);
								arrowLineWingRight.setRotation(diff+60);
							}
							else if(diff > -90 && diff < 0){// between 0 and -90
								arrowLineWingLeft.setRotation(diff+60);
								arrowLineWingRight.setRotation(diff+90);
							}
							else if(diff < -90 && diff > -180){//between -90 and -180
								arrowLineWingLeft.setRotation(diff-60);
								arrowLineWingRight.setRotation(diff-90);
							}*/
							
							
							//if(diff > 10){
							
							//	arrowLineMain = new Line(startx, starty, x2, y2, 8);
								//arrowLineMain.setColor(100, 100, 100);
								//mMainScene.getTopLayer().addEntity(arrowLineMain);
								//lines.add(arrowLineMain);
								
								
							
								//arrowLineWingLeft.setPosition(x2, y2, x2-15, y2-15);
								//arrowLineWingRight.setPosition(x2, y2, x2+15, y2-15);
								//if(diff > 0){
								
							this.setPosition(pSceneTouchEvent.getX()-24, pSceneTouchEvent.getY()-24);

												
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
				mMainScene.getTopLayer().addEntity(mPlayerMenu);
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

