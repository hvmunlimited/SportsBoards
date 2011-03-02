package com.sportsboards2d.boards;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.shape.GLShape;
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
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.MathUtils;

import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sportsboards2d.R;
import com.sportsboards2d.db.Formation;
import com.sportsboards2d.db.PlayerInfo;
import com.sportsboards2d.db.parsing.XMLAccess;
import com.sportsboards2d.sprites.BallSprite;
import com.sportsboards2d.sprites.LineFactory;
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

	private final int BACKGROUND_LAYER = 0;
	private final int LINE_LAYER = 1;
	private final int PLAYER_LAYER = 2;
	private final int PMENU_LAYER = 3;
	
	private List<IMenuItem> menuItems = new ArrayList<IMenuItem>();
	private final int PMENU_DELETE = 0;
	private final int PMENU_HIDE = 1;

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
	protected TiledTextureRegion mBallTextureRegion;
	private TiledTextureRegion mRedPlayerTextureRegion;
	private TiledTextureRegion mBluePlayerTextureRegion;
	
	private Formation currentFormation;
	private List<PlayerSprite> players = new ArrayList<PlayerSprite>();
	private List<Line>lines = new ArrayList<Line>();
	private BallSprite mBall;
	
	private PlayerSprite selectedPlayer;
	
	
	//private Camera mCamera;
	
	protected Scene mMainScene;
	
	private MenuScene mPlayerMenu;
	
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		Engine engine = new Engine(engineOptions);
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
		this.mPlayerMenuFont = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(this.mPlayerMenuFont, this, "Plok.ttf", 32, true, Color.RED);
		this.mEngine.getTextureManager().loadTexture(this.mPlayerMenuFont);
		this.mEngine.getFontManager().loadFont(this.mFont);
		
		//load player and ball textures
		TextureRegionFactory.setAssetBasePath("gfx/");
		this.mBackgroundTexture = new Texture(1024, 1024, TextureOptions.DEFAULT);
		this.mBallTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mRedPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mRedPlayerTexture, this, "48x48RED.png", 0, 0, 1, 1);
		this.mBluePlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBluePlayerTexture, this, "48x48BLUE.png", 0, 0, 1, 1);
		
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture, this.mBluePlayerTexture, this.mRedPlayerTexture, this.mBallTexture);
	}
	@Override
	public Scene onLoadScene(){
		
		this.mMainScene = super.onLoadScene();
				
		this.mMainScene.setOnSceneTouchListener(this);
		this.mMainScene.setOnAreaTouchListener(this);
		this.mMainScene.setTouchAreaBindingEnabled(true);
		
		createPlayerMenu();
		
		this.mHoldDetector = new HoldDetector(new IHoldDetectorListener(){

			@Override
			public void onHold(HoldDetector arg0, long arg1, float arg2, float arg3){}

			@Override
			public void onHoldFinished(final HoldDetector pHoldDetector, long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY){
				
				BaseBoard.this.loadPlayerMenuItems();
				BaseBoard.this.menuItems.get(PMENU_DELETE).setPosition(pHoldX, pHoldY);
				BaseBoard.this.menuItems.get(PMENU_HIDE).setPosition(pHoldX, pHoldY-48);
				mPlayerMenu.setOnMenuItemClickListener(selectedPlayer);

				BaseBoard.this.menuItems.clear();
				mMainScene.setChildScene(mPlayerMenu, false, true, true);
			}
			
		});
		this.mHoldDetector.setTriggerHoldMinimumMilliseconds(400);
		this.mMainScene.registerUpdateHandler(mHoldDetector);

		this.mMainScene.getLayer(BACKGROUND_LAYER).addEntity(new Sprite(0, 0, this.mBackGroundTextureRegion));
		this.currentFormation = loadFormation();
		
		showFormation(currentFormation);
		
		return mMainScene;
	}
	
	public void clearScene(){
	
		clearPlayers();
		clearLines();
		clearBall();

	}
	
	public void clearPlayers(){
		for(final PlayerSprite p: players){
			mEngine.runOnUpdateThread(new Runnable() {
	    		@Override
	    		public void run() {
	    			BaseBoard.this.removeSprite(p);
	    		}
	    	});
		}
		this.players.clear();
	}
	public void clearBall(){
		
		mEngine.runOnUpdateThread(new Runnable() {
    		@Override
    		public void run() {
    			BaseBoard.this.removeSprite(mBall);
    		}
    	});
		this.mBall = null;
	}
	public void clearLines(){
		for(final Line line: lines){
			mEngine.runOnUpdateThread(new Runnable(){	
				@Override
				public void run(){
					BaseBoard.this.removeSprite(line);
				}
			});
		}
		this.lines.clear();

	}

	public void removeSprite(GLShape sprite){
		
		this.mMainScene.unregisterTouchArea(sprite);
		if(sprite instanceof PlayerSprite || sprite instanceof BallSprite){
			this.mMainScene.getLayer(PLAYER_LAYER).removeEntity(sprite);
			players.remove(sprite);
		}
		else if(sprite instanceof Line){
			this.mMainScene.getLayer(LINE_LAYER).removeEntity(sprite);
			lines.remove(sprite);
		}
	}
	
	public void createBall(BallSprite mBall){
		
		this.mBall = mBall;
		this.mMainScene.getLayer(PLAYER_LAYER).addEntity(mBall);
		this.mMainScene.registerTouchArea(mBall);
	}
	
	public void createPlayerMenu(){
		
		mPlayerMenu = new MenuScene(this.mCamera);
		mPlayerMenu.buildAnimations();
		mPlayerMenu.setBackgroundEnabled(false);
	}
	public void loadPlayerMenuItems(){
		createPlayerMenu();
		final IMenuItem deleteMenuItem = new ColorMenuItemDecorator(new TextMenuItem(PMENU_DELETE, this.mFont, "DELETE"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		deleteMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuItems.add(PMENU_DELETE, deleteMenuItem); 
		mPlayerMenu.addMenuItem(deleteMenuItem);

		
		final IMenuItem hideMenuItem = new ColorMenuItemDecorator(new TextMenuItem(PMENU_HIDE, this.mFont, "HIDE"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		deleteMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuItems.add(PMENU_DELETE, hideMenuItem);
		mPlayerMenu.addMenuItem(hideMenuItem);
		
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		
		if(!super.onOptionsItemSelected(item)){
		
			switch(item.getItemId()) {
			
				case R.id.reset:
					
					clearScene();
					loadFormation();
					showFormation(currentFormation);
					
					return true;
				
				case R.id.change_player_size:
					
					currentFormation = saveFormation();
					clearPlayers();
					clearBall();
					this.mRedPlayerTexture.clearTextureSources();
					this.mBluePlayerTexture.clearTextureSources();
					this.mBallTexture.clearTextureSources();
									
					this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					
					if(LARGE_PLAYERS){
						
						this.mRedPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mRedPlayerTexture, this, "32x32RED.png", 0, 0, 1, 1);
						this.mBluePlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBluePlayerTexture, this, "32x32BLUE.png", 0, 0, 1, 1);
						this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallTexture, this, BALL_PATH + "32.png", 0, 0, 1, 1);
	
						LARGE_PLAYERS = false;
					}
					else{
						
						this.mRedPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mRedPlayerTexture, this, "48x48RED.png", 0, 0, 1, 1);
						this.mBluePlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBluePlayerTexture, this, "48x48BLUE.png", 0, 0, 1, 1);
						this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallTexture, this, BALL_PATH + "48.png", 0, 0, 1, 1);
	
						LARGE_PLAYERS = true;
						
					}
					
					this.mEngine.getTextureManager().loadTextures(this.mRedPlayerTexture, this.mBluePlayerTexture, this.mBallTexture);
					
					showFormation(currentFormation);
					
					return true;
			}
		}
		else{
			return true;
		}
		return false;
	}
	
	
	
	// ===========================================================
	// Methods 
	// ===========================================================
	
	
	/*
	 * Capture player & ball positions, save them in XML format to internal storage
	 */
	
	public Formation saveFormation(){
		
		Formation fn = new Formation();
		ArrayList<PlayerInfo> playerList = new ArrayList<PlayerInfo>();
		PlayerSprite pSprite = null;
		PlayerInfo pInfo = null;
		BallSprite mBall = null;
		
		for(int i = 0; i < mMainScene.getLayer(PLAYER_LAYER).getEntityCount(); i++){
			if((IEntity) mMainScene.getLayer(PLAYER_LAYER).getEntity(i) instanceof PlayerSprite){
				pSprite = (PlayerSprite)mMainScene.getLayer(PLAYER_LAYER).getEntity(i);
				pInfo = pSprite.getPlayerInfo();
				pInfo.setCoords(pSprite.getX(), pSprite.getY());
				playerList.add(pInfo);
			}
			else if((IEntity) mMainScene.getLayer(PLAYER_LAYER).getEntity(i) instanceof BallSprite){
				mBall = (BallSprite)mMainScene.getLayer(PLAYER_LAYER).getEntity(i);
			}
		}
		fn.setBall(mBall.getX(), mBall.getY());
		fn.setPlayers(playerList);
		fn.setName("testing");
		//XMLWriter.writeFormation(this, fn, SPORT_NAME.toLowerCase());
		
		return fn;
		
	}
	
	public Formation loadFormation(){
		
		ArrayList<Formation> formsList = (ArrayList<Formation>) XMLAccess.loadFormations(this, resID);
		
		for(Formation fn:formsList){
			if(fn.getName().equalsIgnoreCase(DEFAULT_NAME)){
				this.currentFormation = fn;
				break;
			}
		}
		return this.currentFormation;
		
	}
	
	public void showFormation(Formation fn){
		
		TiledTextureRegion tex = null;
		createBall(new BallSprite(fn.getBall().getX(), fn.getBall().getY(), this.mBallTextureRegion));
		for(PlayerInfo p:fn.getPlayers()){
			
			if(p.getTeamColor().equalsIgnoreCase("blue")){
				tex = this.mBluePlayerTextureRegion;
			}
			else if(p.getTeamColor().equalsIgnoreCase("red")){
				tex = this.mRedPlayerTextureRegion;
			}		
			createPlayer(p, tex);

		}
	}
	
	private void createPlayer(final PlayerInfo p, TiledTextureRegion tex){
		
		
				
		final PlayerSprite newPlayer = new PlayerSprite(p, tex){
			
				@Override
				public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY){
					
					switch(pMenuItem.getID()) {
	
					    case PMENU_DELETE:
					    	
					    	mEngine.runOnUpdateThread(new Runnable() {
					    	
					    		@Override

					    		public void run() {
					    			removeSprite(selectedPlayer);
							   
					    		}

					    	});
					    	
					        /* Remove the menu and reset it. */
					        mMainScene.clearChildScene();
					        mPlayerMenu.reset();
					        mPlayerMenu = null;
					        selectedPlayer = null;
					        return true;
					   
					    case PMENU_HIDE:
					    	selectedPlayer = null;
					    	mMainScene.clearChildScene();
					    	mPlayerMenu.reset();
					    	mPlayerMenu = null;
					    	return true;
					    default:
					        return false;
					}
							
				}
			};
		
		this.mMainScene.getLayer(PLAYER_LAYER).addEntity(newPlayer);
		this.mMainScene.registerTouchArea(newPlayer);		
		this.players.add(newPlayer);
	}
	
	
	
	//////////TO DO
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

		
		PlayerSprite sprite = null; 
		
		float moveX, moveY;// endX, endY;
		moveX = 0;
		moveY = 0;
	//	endX = 0;
	//	endY = 0;
		Line arrowLineMain = null;
	//	float xDiff, yDiff;
		float diff;
	//	float angleRad, angleDeg;
		
		if((pTouchArea) instanceof BallSprite){
			
			switch(pSceneTouchEvent.getAction()) {
			
				case TouchEvent.ACTION_DOWN:
					
					mBall.setScale(2.0f);
					mBall.setmGrabbed(true);
					break;
					
				case TouchEvent.ACTION_MOVE:
					
					if(mBall.ismGrabbed()) {
						mBall.setPosition(pSceneTouchEvent.getX() - 48 / 2, pSceneTouchEvent.getY() - 48 / 2);
					}
					break;
					
				case TouchEvent.ACTION_UP:
					
					if(mBall.ismGrabbed()) {
						mBall.setmGrabbed(false);
						mBall.setScale(1.0f);
					}
					break;
			}
		}
		else if((pTouchArea) instanceof PlayerSprite){
			
			mHoldDetector.onTouchEvent(pSceneTouchEvent);
			sprite = (PlayerSprite) pTouchArea;
			selectedPlayer = sprite;			

			switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					
					sprite.setScale(2.0f);	
					sprite.setmGrabbed(true);
					
					sprite.setPosition(pSceneTouchEvent.getX()-24, pSceneTouchEvent.getY()-24);
					
					sprite.setStartX(sprite.getX());
					sprite.setStartY(sprite.getY());

					//System.out.println(this.getTextureRegion().getHeight());
					
					//arrowLineWingLeft = new Line(startX, startY, startX-30, startY-30, 8);
					//arrowLineWingRight = new Line(startX, startY, startX+30, startY-30, 8);
			
					
					break;
				case TouchEvent.ACTION_MOVE:
					
					if(sprite.ismGrabbed()) {
	
						sprite.setPosition(pSceneTouchEvent.getX()-24, pSceneTouchEvent.getY()-24);
						moveX = sprite.getX();
						moveY = sprite.getY();
						
						if(LINE_ENABLED){
						
							diff = MathUtils.distance(sprite.getStartX(), sprite.getStartY(), moveX, moveY);
							
							if(diff > 50){
								
								arrowLineMain = LineFactory.createLine(sprite.getStartX()+24, sprite.getStartY()+24, moveX+24, moveY+24, 8);
								mMainScene.getLayer(LINE_LAYER).addEntity(arrowLineMain);
								lines.add(arrowLineMain);
								sprite.setStartX(moveX);
								sprite.setStartY(moveY);
							}
						}
						
					}
					
					break;
				case TouchEvent.ACTION_UP:
					
					if(sprite.ismGrabbed()) {
						
						sprite.setmGrabbed(false);

						sprite.setScale(1.0f);
						sprite.setPosition(pSceneTouchEvent.getX()-24, pSceneTouchEvent.getY()-24);
	
						//endX = sprite.getX()+24;
						//endY = sprite.getY()+24;
						
						//diff = MathUtils.distance(startX, startY, endX, endY);
						//xDiff = Math.calculateDifference(startX, endX) / diff;
						//yDiff = Math.calculateDifference(startY, endY) / diff;	
						//angleRad = (float) java.lang.Math.atan2(-xDiff, yDiff);
						//angleDeg = MathUtils.radToDeg(angleRad);
						
						//System.out.println("Distance between start and end: " + diff);
						//System.out.println("pValueX: " + xDiff);
						//System.out.println("pValueY: " + yDiff);
						//System.out.println("Angle in radians: " + angleRad);
						//System.out.println("Angle in degrees: " + angleDeg);
						
							
						
					}
					break;
				}
		
		}
		
		return true;
	}
}

