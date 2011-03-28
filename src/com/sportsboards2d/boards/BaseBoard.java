package com.sportsboards2d.boards;

import java.util.ArrayList;
import java.util.List;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.PathModifier;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchException;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sportsboards2d.activities.DeleteForm;
import com.sportsboards2d.activities.LoadForm;
import com.sportsboards2d.activities.SaveForm;
import com.sportsboards2d.db.objects.Configuration;
import com.sportsboards2d.db.objects.Coordinates;
import com.sportsboards2d.db.objects.Formation;
import com.sportsboards2d.db.objects.PlayerInfo;
import com.sportsboards2d.db.parsing.XMLAccess;
import com.sportsboards2d.sprites.BallSprite;
import com.sportsboards2d.sprites.ButtonSprite;
import com.sportsboards2d.sprites.LineFactory;
import com.sportsboards2d.sprites.PlayerSprite;
import com.sportsboards2d.util.Constants;
import com.sportsboards2d.util.PausablePathModifier;
import com.sportsboards2d.util.SpritePath;

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
	protected String BALL_PATH_SMALL;
	protected String BALL_PATH_LARGE;
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
	protected TiledTextureRegion mRedPlayerTextureRegion;
	private TiledTextureRegion mBluePlayerTextureRegion;
	
	private int currentFormation;
	private  List<Formation> formsList;
	public static String[] formNames;
	private List<PlayerSprite> players = new ArrayList<PlayerSprite>();
	private List<Line>lines = new ArrayList<Line>();
	protected BallSprite mBall;
	
	private List<SpritePath> pathList = new ArrayList<SpritePath>();
	private List<PausablePathModifier> pauseList = new ArrayList<PausablePathModifier>();
	//private List<Shape> undoList = new ArrayList<Shape>();
	protected List<Coordinates> path = new ArrayList<Coordinates>();
	
	private Line left, right;
	
	
	private boolean recording = false;


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
			this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallTexture, this, BALL_PATH_SMALL, 0, 0, 1, 1);
		}
		else{
			this.mRedPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mRedPlayerTexture, this, "32x32RED.png", 0, 0, 1, 1);
			this.mBluePlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBluePlayerTexture, this, "32x32BLUE.png", 0, 0, 1, 1);
			this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallTexture, this, BALL_PATH_SMALL, 0, 0, 1, 1);
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
		if(formsList.size()>0){
			showFormation();
		}
		return mMainScene;
	}
	protected void clearScene(){
		clearPlayers();
		clearLines();
		clearBall();
	}
	
	private void clearPlayers(){
		for(final PlayerSprite p: players){
			mEngine.runOnUpdateThread(new Runnable() {
	    		@Override
	    		public void run() {
	    			Body body = BaseBoard.this.mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(p);
	    			BaseBoard.this.mPhysicsWorld.destroyBody(body);
	    			BaseBoard.this.mMainScene.getChild(PLAYER_LAYER).detachChild(p);
	    			BaseBoard.this.players.remove(p);
	    			BaseBoard.this.mMainScene.unregisterTouchArea(p);
	    			//BaseBoard.this.mMainScene.
	    		}
	    	});
		}
		this.players.clear();
	}
	private void clearBall(){
		
		mEngine.runOnUpdateThread(new Runnable() {
    		@Override
    		public void run() {
    			BaseBoard.this.mMainScene.getChild(PLAYER_LAYER).detachChild(mBall);
    			BaseBoard.this.mMainScene.unregisterTouchArea(mBall);
    		}
    	});
	}
	private void clearLines(){
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
			
			case Constants.MAIN_MENU_PLAYBACK:
				this.playBackEnabled = true;
				super.onMenuItemClicked(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
				return true;
			case Constants.SETTINGS_LINE_ENABLE:
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
					this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallTexture, this, BALL_PATH_SMALL, 0, 0, 1, 1);

					LARGE_PLAYERS = false;
				}
				else{
					this.mRedPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mRedPlayerTexture, this, "48x48RED.png", 0, 0, 1, 1);
					this.mBluePlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBluePlayerTexture, this, "48x48BLUE.png", 0, 0, 1, 1);
					this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallTexture, this, BALL_PATH_SMALL, 0, 0, 1, 1);

					LARGE_PLAYERS = true;

				}
				this.mMainMenu.back();
				return true;
				
			default:
				return false;
		}
	}

	/*
	 * Capture player & ball positions, save them in XML format to internal storage
	 */
	
	private Formation captureFormation(){
		
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
	
	private void loadFormation(String name){
		
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
	
	
	
	private void showFormation(){
		
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

	protected void createPlayer(PlayerInfo p, TiledTextureRegion tex){
		
		ChangeableText playerText;
		
		final Body body;
		final PlayerSprite newPlayer = new PlayerSprite(p, tex){
			
				@Override
				public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY){
					
					switch(pMenuItem.getID()) {
	
					    case Constants.PMENU_HIDE:
					    	
					    	mEngine.runOnUpdateThread(new Runnable() {
					    	
					    		@Override

					    		public void run() {
					    			final Body body = BaseBoard.this.mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(selectedPlayer);
					    			BaseBoard.this.mPhysicsWorld.destroyBody(body);
					    			BaseBoard.this.mMainScene.getChild(PLAYER_LAYER).detachChild(selectedPlayer);
					    			BaseBoard.this.players.remove(selectedPlayer);
					    			BaseBoard.this.mMainScene.unregisterTouchArea(selectedPlayer);
							   
					    		}

					    	});
					        /* Remove the menu and reset it. */
					        mMainScene.clearChildScene();

					        return true;
					   
					    case Constants.PMENU_EXIT:
					    	mMainScene.clearChildScene();

					    	return true;
					    default:
					    	mMainScene.clearChildScene();
					        return false;
					}	
				}
			};
			
		if(p.getInitials().length()>1){
			playerText = new ChangeableText(+15, -30, this.mPlayerInfoFont, p.getInitials());
			newPlayer.addDisplayInfo(playerText);
		}
		
		if(p.getType().length()>0){
			playerText = new ChangeableText(+50, +10, this.mPlayerInfoFont, p.getType());
			newPlayer.addDisplayInfo(playerText);
		}
		
		playerText = new ChangeableText(+20, +50, this.mPlayerInfoFont, "0");
		newPlayer.addDisplayInfo(playerText);
		
		//this enables/disables the player info popup when touching a player
		newPlayer.setupDisplay();
		
		//enable physics
		
		body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, newPlayer, BodyType.DynamicBody, FIXTURE_DEF);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(newPlayer, body, true, true));

		this.mMainScene.getChild(PLAYER_LAYER).attachChild(newPlayer);
		this.mMainScene.registerTouchArea(newPlayer);		
		this.players.add(newPlayer);
		selectedPlayer = newPlayer;
	}
	
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

		PlayerSprite sprite = null; 
			
		float[]Xs = null;
		float[]Ys = null;
		
		float moveX, moveY, angleDeg, diff;
		moveX = 0;
		moveY = 0;

		Line arrowLineMain = null;
		
		if((pTouchArea) instanceof BallSprite){
			
			switch(pSceneTouchEvent.getAction()) {
			
				case TouchEvent.ACTION_DOWN:
					
					this.mBall.setScale(2.0f);
					this.mBall.setmGrabbed(true);
					
					return true;
					
				case TouchEvent.ACTION_MOVE:
					
					if(this.mBall.ismGrabbed()) {
						this.mBall.setPosition(pSceneTouchEvent.getX() - 48 / 2, pSceneTouchEvent.getY() - 48 / 2);
					}
					return true;
					
				case TouchEvent.ACTION_UP:
					
					if(this.mBall.ismGrabbed()) {
						this.mBall.setmGrabbed(false);
						this.mBall.setScale(1.0f);
						
						
						
					}
		
					return true;
			}
		}
		else if((pTouchArea) instanceof PlayerSprite){
			
			mHoldDetector.onTouchEvent(pSceneTouchEvent);
			sprite = (PlayerSprite) pTouchArea;
			selectedPlayer = sprite;
			Body body = this.mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(sprite);
			
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
					
					sprite.displayInfo(true);
					path.clear();
					sprite.setStartX(sprite.getX()+24);
					sprite.setStartY(sprite.getY()+24);
				
					left = LineFactory.createLine(0, 0, 0, 0, 8);
					right = LineFactory.createLine(0, 0, 0, 0, 8);
					
					this.mMainScene.getChild(LINE_LAYER).attachChild(left);
					this.mMainScene.getChild(LINE_LAYER).attachChild(right);
				
					return true;
					
				case TouchEvent.ACTION_MOVE:
					
					if(sprite.ismGrabbed()) {
	
						sprite.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						body.setTransform(new Vector2(pSceneTouchEvent.getX()/32,  pSceneTouchEvent.getY()/32), 0);
						
						moveX = sprite.getX();
						moveY = sprite.getY();
						
						path.add(new Coordinates(sprite.getX(), sprite.getY()));

						
						if(LINE_ENABLED){
						
							diff = MathUtils.distance(sprite.getStartX(), sprite.getStartY(), moveX, moveY);
							
							float relativeX = 2 * (MathUtils.bringToBounds(0, sprite.getWidth(), pTouchAreaLocalX) / sprite.getWidth() - 0.5f);
							float relativeY = 2 * (MathUtils.bringToBounds(0, sprite.getHeight(), pTouchAreaLocalY) / sprite.getHeight() - 0.5f);

							angleDeg = MathUtils.radToDeg((float)Math.atan2(-relativeX, relativeY));
							
							if(diff > 30){
								left.setPosition(moveX, moveY, moveX - 20, moveY - 20);
								right.setPosition(moveX, moveY, moveX + 20, moveY - 20);
								
								left.setRotation(angleDeg);
								right.setRotation(angleDeg);
								
								arrowLineMain = LineFactory.createLine(sprite.getStartX(), sprite.getStartY(), moveX, moveY, 8);
								this.mMainScene.getChild(LINE_LAYER).attachChild(arrowLineMain);
								this.lines.add(arrowLineMain);
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
						sprite.displayInfo(false);
						
						if(recording){
							if(path.size()>1){

								Xs = new float[path.size()];
								Ys = new float[path.size()];

								System.out.println(path.size());

								for(int i = path.size()-1; i >= 0; i--){
									Xs[i] = path.get(i).getX();
									Ys[i] = path.get(i).getY();
								}
								final PausablePathModifier.Path path1 = new PausablePathModifier.Path(Xs, Ys);
								pathList.add(new SpritePath((AnimatedSprite)selectedPlayer, path1));
								body.setTransform(new Vector2(Xs[0]/32, Ys[0]/32), 0);
							}
						}

					}
					return true;
				}
		
		}
		else if((pTouchArea) instanceof ButtonSprite){
			int buttonPushed = buttons.indexOf(pTouchArea);
			
			switch(buttonPushed){
			
				case Constants.PLAY_BUTTON:
					for(SpritePath path:pathList){
						PausablePathModifier path1 = new PausablePathModifier(1.0f, path.getPath());
						path.getSprite().registerEntityModifier(path1);
						pauseList.add(path1);
					}
					
					System.out.println("play button hit");
					return true;
					
				case Constants.STOP_BUTTON:
					recording = false;
					for(SpritePath path:pathList){
						path.getSprite().clearEntityModifiers();
					}
					return true;
			
				case Constants.PAUSE_BUTTON:
					System.out.println("pause button hit");
					
					return true;
					
				case Constants.RECORD_BUTTON:
					recording = true;
					return true;
					
				case Constants.REWIND_BUTTON:

					return true;
			}
			
			
		}
		
		return false;
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
					XMLAccess.writeFormations(this, formsList, SPORT_NAME);
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
					XMLAccess.writeFormations(this, formsList, SPORT_NAME);
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
	public void setCollisionFilters(){
		
		for(int i = 0; i < this.mMainScene.getChild(PLAYER_LAYER).getChildCount(); i++){
			
			final PlayerSprite player = (PlayerSprite) this.mMainScene.getChild(PLAYER_LAYER).getChild(i);
			
			for(final ButtonSprite button:buttons){
				
			
				
				this.mMainScene.registerUpdateHandler(new IUpdateHandler() {
	
					@Override
					public void reset() { }
	
					@Override
					public void onUpdate(final float pSecondsElapsed) {
						if(player.collidesWith(button)) {
							//centerRectangle.setColor(1, 0, 0);
						} else {
							//centerRectangle.setColor(0, 1, 0);
						}
					}
				});
			}
		}
		
	}
}

