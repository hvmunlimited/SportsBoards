package com.sportsboards2d.boards;

import java.util.ArrayList;
import java.util.List;

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
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchException;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.MathUtils;

import android.content.Intent;
import android.graphics.Color;

import com.sportsboards2d.db.Configuration;
import com.sportsboards2d.db.Formation;
import com.sportsboards2d.db.PlayerInfo;
import com.sportsboards2d.db.parsing.XMLAccess;
import com.sportsboards2d.sprites.BallSprite;
import com.sportsboards2d.sprites.LineFactory;
import com.sportsboards2d.sprites.PlayerSprite;
import com.sportsboards2d.util.Constants;

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
	
	
	
	private Texture mPlayerInfoFontTexture;
	
	private Font mPlayerInfoFont;
	
	protected TextureRegion mBackGroundTextureRegion;
	protected TiledTextureRegion mBallTextureRegion;
	private TiledTextureRegion mRedPlayerTextureRegion;
	private TiledTextureRegion mBluePlayerTextureRegion;
	
	private int currentFormation;
	private  List<Formation> formsList;
	public static String[] formNames;
	private List<PlayerSprite> players = new ArrayList<PlayerSprite>();
	private List<Line>lines = new ArrayList<Line>();
	private BallSprite mBall;
	
	//private List<Shape> undoList = new ArrayList<Shape>();
	
	
	protected Scene mMainScene;
	//Eprivate MenuScene mPlayerMenu;
	
	
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

		super.onLoadResources();
		//load menu textures
		
		this.mPlayerInfoFontTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlayerInfoFont = FontFactory.createFromAsset(this.mPlayerInfoFontTexture, this, "VeraBd.ttf", 24, true, Color.BLACK);
		//this.mPlayerInfoFont = new Font(this.mPlayerInfoFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, true, Color.BLACK);

		this.mEngine.getTextureManager().loadTexture(this.mPlayerInfoFontTexture);
		this.mEngine.getFontManager().loadFont(this.mPlayerInfoFont);
		//load player and ball textures
		
		this.mBackgroundTexture = new Texture(1024, 1024, TextureOptions.DEFAULT);
		this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mBallTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		
		if(LARGE_PLAYERS){
			
			
			this.mRedPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mRedPlayerTexture, this, "48x48RED.png", 0, 0, 1, 1);
			this.mBluePlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBluePlayerTexture, this, "48x48BLUE.png", 0, 0, 1, 1);
			this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallTexture, this, BALL_PATH + "48.png", 0, 0, 1, 1);
			
		}
		else{
			
			this.mRedPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mRedPlayerTexture, this, "32x32RED.png", 0, 0, 1, 1);
			this.mBluePlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBluePlayerTexture, this, "32x32BLUE.png", 0, 0, 1, 1);
			this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallTexture, this, BALL_PATH + "32.png", 0, 0, 1, 1);

		}
	
		this.mEngine.getTextureManager().loadTextures(this.mBluePlayerTexture, this.mRedPlayerTexture, this.mBallTexture);
	}
	@Override
	public Scene onLoadScene(){
		
		this.mMainScene = super.onLoadScene();
				
		this.mMainScene.setOnAreaTouchListener(this);
		this.mMainScene.setTouchAreaBindingEnabled(true);
		
		this.mMainScene.getChild(BACKGROUND_LAYER).attachChild(new Sprite(0, 0, this.mBackGroundTextureRegion));
		
		loadFormation(DEFAULT_NAME);
		
		this.mBall = new BallSprite(0, 0, this.mBallTextureRegion);

		showFormation();
		
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
	    			BaseBoard.this.mMainScene.getChild(PLAYER_LAYER).detachChild(p);
	    			BaseBoard.this.players.remove(p);
	    			BaseBoard.this.mMainScene.unregisterTouchArea(p);
	    		}
	    	});
		}
		this.players.clear();
	}
	public void clearBall(){
		
		mEngine.runOnUpdateThread(new Runnable() {
    		@Override
    		public void run() {
    			BaseBoard.this.mMainScene.getChild(PLAYER_LAYER).detachChild(mBall);
    			BaseBoard.this.mMainScene.unregisterTouchArea(mBall);
    		}
    	});
	}
	public void clearLines(){
		for(final Line line: lines){
			mEngine.runOnUpdateThread(new Runnable(){	
				@Override
				public void run(){
					BaseBoard.this.mMainScene.getChild(LINE_LAYER).detachChild(line);
					BaseBoard.this.lines.remove(line);
				}
			});
		}
		this.lines.clear();

	}

	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
			
			case Constants.MAIN_MENU_SETTINGS:
				super.onMenuItemClicked(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
				return true;
			case Constants.SETTINGS_MENU_LINE:
				super.onMenuItemClicked(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
				return true;		
			case Constants.SETTINGS_LINE_ENABLE:
				super.onMenuItemClicked(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
				return true;	
			case Constants.SETTINGS_LINE_COLOR:
				super.onMenuItemClicked(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
				return true;
			case Constants.SETTINGS_LINE_TYPE:
				super.onMenuItemClicked(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
				return true;
			case Constants.SETTINGS_MENU_PLAYER:
				super.onMenuItemClicked(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
				return true;
			
			case Constants.MAIN_MENU_RESET:
				
				clearScene();
				
				loadFormation(formsList.get(currentFormation).getName());
				
				showFormation();
				this.mMainMenu.back();

				return true;

			case Constants.MAIN_MENU_CLEARLINES:
				
				this.clearLines();
				this.mMainMenu.back();

				return true;
				
			case Constants.MAIN_MENU_SAVE:
				
				this.startActivityForResult(new Intent(this, SaveForm.class), 1);
				this.mMainMenu.back();
				return true;
			
			case Constants.MAIN_MENU_DELETE:
				
				formNames = new String[this.formsList.size()];
				for(int i = 0; i < formsList.size(); i++){
					formNames[i] = formsList.get(i).getName();
				}
				
				this.startActivityForResult(new Intent(this, DeleteForm.class), 3);
				
				this.mMainMenu.back();
				return true;
				
			//get a list of formations	
			case Constants.MAIN_MENU_LOAD:
				
				formNames = new String[this.formsList.size()];
				for(int i = 0; i < formsList.size(); i++){
					formNames[i] = formsList.get(i).getName();
				}
				
				this.startActivityForResult(new Intent(this, LoadForm.class), 2);
				
				this.mMainMenu.back();
				return true;
				
			case Constants.SETTINGS_PLAYER_SIZE:

				this.mRedPlayerTexture.clearTextureSources();
				this.mBluePlayerTexture.clearTextureSources();
				this.mBallTexture.clearTextureSources();
				
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
				this.mMainMenu.back();
				return true;
				
			default:
				return false;
		}
	}
	
	
	
	// ===========================================================
	// Methods 
	// ===========================================================
	
	public void updateFormation(){
		
	}
	
	/*
	 * Capture player & ball positions, save them in XML format to internal storage
	 */
	
	public Formation captureFormation(){
		
		Formation fn = new Formation();
		ArrayList<PlayerInfo> playerList = new ArrayList<PlayerInfo>();
		PlayerSprite pSprite = null;
		float x, y;
		PlayerInfo pInfo = null;
		
		for(int i = 0; i < mMainScene.getChild(PLAYER_LAYER).getChildCount(); i++){
			pInfo = new PlayerInfo();
			if((IEntity) mMainScene.getChild(PLAYER_LAYER).getChild(i) instanceof PlayerSprite){
				pSprite = (PlayerSprite)mMainScene.getChild(PLAYER_LAYER).getChild(i);
				pInfo.setPlayerName(pSprite.getPlayerInfo().getPlayerName());
				pInfo.setTeamColor(pSprite.getPlayerInfo().getTeamColor());
				pInfo.setType(pSprite.getPlayerInfo().getType());
				x = pSprite.getX();
				y = pSprite.getY();
				pInfo.setCoords(x, y);
				playerList.add(pInfo);
			}
		}
		fn.setBall(this.mBall.getX(), this.mBall.getY());
		fn.setPlayers(playerList);
		return fn;
	}
	
	public void loadFormation(String name){
		
		if(formsList == null){
			System.out.println(SPORT_NAME);
			formsList = XMLAccess.loadFormations(this, SPORT_NAME.toLowerCase(), resID);
		}
		System.out.println(formsList.size());

		for(int i = 0; i < formsList.size(); i++){
			if(name.equalsIgnoreCase(formsList.get(i).getName())){
				currentFormation = i;
				break;
			}
		}
	}
	
	
	
	public void showFormation(){
		
		TiledTextureRegion tex = null;
		
		Formation fn = formsList.get(currentFormation);
		
		for(PlayerInfo p:fn.getPlayers()){
			
			if(p.getTeamColor().equalsIgnoreCase("blue")){
				tex = this.mBluePlayerTextureRegion;
			}
			else if(p.getTeamColor().equalsIgnoreCase("red")){
				tex = this.mRedPlayerTextureRegion;
			}
			createPlayer(p, tex);

		}
		
		this.mBall.setPosition(fn.getBall().getX(), fn.getBall().getY());
		this.mMainScene.getChild(PLAYER_LAYER).attachChild(this.mBall);

		this.mMainScene.registerTouchArea(this.mBall);
	}

	private void createPlayer(PlayerInfo p, TiledTextureRegion tex){
		
		ChangeableText playerText;
		
		final PlayerSprite newPlayer = new PlayerSprite(p, tex){
			
				@Override
				public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY){
					
					switch(pMenuItem.getID()) {
	
					    case Constants.PMENU_HIDE:
					    	
					    	mEngine.runOnUpdateThread(new Runnable() {
					    	
					    		@Override

					    		public void run() {
					    			selectedPlayer.setVisible(false);
					    			BaseBoard.this.mMainScene.getChild(PLAYER_LAYER).detachChild(selectedPlayer);
					    			BaseBoard.this.players.remove(selectedPlayer);
					    			BaseBoard.this.mMainScene.unregisterTouchArea(selectedPlayer);
							   
					    		}

					    	});
					        /* Remove the menu and reset it. */
					        mMainScene.clearChildScene();

					        return true;
					   
					    case Constants.PMENU_DELETE:
					    	mMainScene.clearChildScene();

					    	return true;
					    default:
					    	mMainScene.clearChildScene();
					        return false;
					}	
				}
			};
		
		playerText = new ChangeableText(+15, -30, this.mPlayerInfoFont, p.getInitials());
		newPlayer.addDisplayInfo(playerText);
		playerText = new ChangeableText(+50, +10, this.mPlayerInfoFont, p.getType());
		newPlayer.addDisplayInfo(playerText);
		playerText = new ChangeableText(+20, +50, this.mPlayerInfoFont, "0");
		newPlayer.addDisplayInfo(playerText);

		//newPlayer.setupDisplay();
		
		this.mMainScene.getChild(PLAYER_LAYER).attachChild(newPlayer);
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
					
					this.mBall.setScale(2.0f);
					this.mBall.setmGrabbed(true);
					break;
					
				case TouchEvent.ACTION_MOVE:
					
					if(this.mBall.ismGrabbed()) {
						this.mBall.setPosition(pSceneTouchEvent.getX() - 48 / 2, pSceneTouchEvent.getY() - 48 / 2);
					}
					break;
					
				case TouchEvent.ACTION_UP:
					
					if(this.mBall.ismGrabbed()) {
						this.mBall.setmGrabbed(false);
						this.mBall.setScale(1.0f);
					}
					break;
			}
		}
		else if((pTouchArea) instanceof PlayerSprite){
			
			mHoldDetector.onTouchEvent(pSceneTouchEvent);
			sprite = (PlayerSprite) pTouchArea;
			selectedPlayer = sprite;
			
			if(sprite.getPlayerInfo().getTeamColor().equalsIgnoreCase("red")){
				LineFactory.setColor(LineFactory.RED);
			}
			else if(sprite.getPlayerInfo().getTeamColor().equalsIgnoreCase("blue")){
				LineFactory.setColor(LineFactory.BLUE);
			}

			switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					
					sprite.setScale(2.0f);	
					sprite.setmGrabbed(true);
					
					sprite.setPosition(pSceneTouchEvent.getX()-24, pSceneTouchEvent.getY()-24);
					sprite.displayInfo(true);

					sprite.setStartX(sprite.getX());
					sprite.setStartY(sprite.getY());
					
					
					break;
				case TouchEvent.ACTION_MOVE:
					
					if(sprite.ismGrabbed()) {
	
						sprite.setPosition(pSceneTouchEvent.getX()-24, pSceneTouchEvent.getY()-24);
						moveX = sprite.getX();
						moveY = sprite.getY();
						
						if(LINE_ENABLED){
						
							diff = MathUtils.distance(sprite.getStartX(), sprite.getStartY(), moveX, moveY);
							
							if(diff > 30){
								
								arrowLineMain = LineFactory.createLine(sprite.getStartX()+24, sprite.getStartY()+24, moveX+24, moveY+24, 8);
								mMainScene.getChild(LINE_LAYER).attachChild(arrowLineMain);
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
						
						sprite.displayInfo(false);

						
						
						
						
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
	@Override
	protected void onActivityResult(int requestCode, int receiveCode, Intent intent){
		
		System.out.println(receiveCode);	
		//boolean exists = false;
		Formation fn = null;
		//int index = 0;
		
		if(receiveCode != -1){
			
			//save form activity

			if(requestCode == 1){
				if(receiveCode == 1){
					
					
					fn = captureFormation();
					fn.setName(intent.getType());
					formsList.add(fn);
					XMLAccess.writeFormations(this, formsList, SPORT_NAME.toLowerCase());
				}
			}
			//load form activity
			else if(requestCode == 2){
				fn = formsList.get(receiveCode);
				clearScene();
				loadFormation(fn.getName());
				showFormation();
			}
			
			else if(requestCode == 3){
				
				if(formsList.get(receiveCode).getName().equals(DEFAULT_NAME)){
					
					//do nothing
					
				}
				else{
					formsList.remove(receiveCode);
					XMLAccess.writeFormations(this, formsList, SPORT_NAME.toLowerCase());
				}
				
				
			}
		}

	}
	@Override
	public void finish(){
		
		Configuration config = new Configuration(LINE_ENABLED, true, SPORT_NAME, LARGE_PLAYERS);


		XMLAccess.writeConfig(this, config, "config");
		
		super.finish();
	}
}

