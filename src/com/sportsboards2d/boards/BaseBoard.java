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
import android.content.SharedPreferences;
import android.graphics.Color;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sportsboards2d.R;
import com.sportsboards2d.activities.CreatePlayer;
import com.sportsboards2d.activities.DeleteForm;
import com.sportsboards2d.activities.LoadForm;
import com.sportsboards2d.activities.SaveForm;
import com.sportsboards2d.activities.SelectPlayer;
import com.sportsboards2d.activities.SettingsViewer;
import com.sportsboards2d.activities.ViewPlayer;
import com.sportsboards2d.db.objects.Coordinates;
import com.sportsboards2d.db.objects.FormationObject;
import com.sportsboards2d.db.objects.Player;
import com.sportsboards2d.db.objects.PlayerEntry;
import com.sportsboards2d.db.objects.PlayerInfo;
import com.sportsboards2d.db.objects.PlayerObject;
import com.sportsboards2d.db.parsing.XMLAccess;
import com.sportsboards2d.sprites.BallSprite;
import com.sportsboards2d.sprites.ButtonSprite;
import com.sportsboards2d.sprites.LineFactory;
import com.sportsboards2d.sprites.PlayerSprite;
import com.sportsboards2d.util.Constants;
import com.sportsboards2d.util.PausablePathModifier;
import com.sportsboards2d.util.SpritePath;
import com.sportsboards2d.util.StringPrinting;

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
	public static int playerIDCounter;
	protected String SPORT_NAME;
	protected String BALL_PATH_SMALL;
	protected String BALL_PATH_LARGE;
	protected String DEFAULT_NAME;
		
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
	private  List<FormationObject> formsList;
	public static String[] formNames;
	public static String[] playerNames;
	private List<PlayerSprite> playerSprites = new ArrayList<PlayerSprite>();
	private List<PlayerInfo> players = new ArrayList<PlayerInfo>();
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
		DEFAULT_NAME = getString(R.string.default_string);
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
		this.mPlayerInfoFont = FontFactory.createFromAsset(this.mPlayerInfoFontTexture, this, "VeraBd.ttf", 18, true, Color.BLACK);
		//this.mPlayerInfoFont = new Font(this.mPlayerInfoFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, true, Color.BLACK);
		this.mEngine.getTextureManager().loadTexture(this.mPlayerInfoFontTexture);
		this.mEngine.getFontManager().loadFont(this.mPlayerInfoFont);
		//load player and ball textures
		this.mBackgroundTexture = new Texture(1024, 1024, TextureOptions.DEFAULT);
		this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		this.mBallTexture = new Texture(64, 64, TextureOptions.BILINEAR);
		
		if(config.largePlayers){
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
		
		List<FormationObject> fEntries;

		this.mMainScene = super.onLoadScene();	
		this.mMainScene.setOnAreaTouchListener(this);
		this.mMainScene.setTouchAreaBindingEnabled(true);
		this.mMainScene.getChild(BACKGROUND_LAYER).attachChild(new Sprite(0, 0, this.mBackGroundTextureRegion));
		
		players = XMLAccess.loadPlayers(this, SPORT_NAME.toLowerCase());
		fEntries = XMLAccess.loadFormations(this, SPORT_NAME.toLowerCase());
		formsList = matchPlayers(fEntries);
		loadFormation(config.lastLoaded);
		this.mBall = new BallSprite(0, 0, this.mBallTextureRegion);
		if(formsList!=null){
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
		for(final PlayerSprite p: playerSprites){
			mEngine.runOnUpdateThread(new Runnable() {
	    		@Override
	    		public void run() {
	    			Body body = BaseBoard.this.mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(p);
	    			BaseBoard.this.mPhysicsWorld.destroyBody(body);
	    			BaseBoard.this.mMainScene.getChild(PLAYER_LAYER).detachChild(p);
	    			BaseBoard.this.playerSprites.remove(p);
	    			BaseBoard.this.mMainScene.unregisterTouchArea(p);
	    			//BaseBoard.this.mMainScene.
	    		}
	    	});
		}
		this.playerSprites.clear();
	}
	private void clearBall(){
		
		mEngine.runOnUpdateThread(new Runnable() {
    		@Override
    		public void run() {
    			BaseBoard.this.mMainScene.getChild(BALL_LAYER).detachChild(mBall);
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


	/*
	 * Capture player & ball positions, save them in XML format to internal storage
	 */
	
	private List<PlayerObject> captureFormation(){
		
		List<PlayerObject> playerList = new ArrayList<PlayerObject>();
		PlayerSprite pSprite = null;
		Player newPlayer = null;
		PlayerInfo pInfo = null;
		float xPlayer = 0.0f, yPlayer = 0.0f;
		String team = "";
		
		
		for(int i = 0; i < mMainScene.getChild(PLAYER_LAYER).getChildCount(); i++){
			if((IEntity) mMainScene.getChild(PLAYER_LAYER).getChild(i) instanceof PlayerSprite){
				pSprite = (PlayerSprite)mMainScene.getChild(PLAYER_LAYER).getChild(i);
				team = pSprite.getPlayer().getpTeam();
				xPlayer = pSprite.getX();
				yPlayer = pSprite.getY();
				newPlayer = pSprite.getPlayer();
				//System.out.println(pSprite.getPlayer().getpID());
				pInfo = new PlayerInfo(newPlayer.getpID(), newPlayer.getpInfo().getjNum(), newPlayer.getpInfo().getType(),
						newPlayer.getpInfo().getPName());
				newPlayer = new Player(newPlayer.getpID(), team, new Coordinates(xPlayer, yPlayer), pInfo);
				playerList.add(newPlayer);
			}
		}
		return playerList;
	}
	
	private void loadFormation(String name){
		
		if(formsList != null){
			for(int i = 0; i < formsList.size(); i++){
				if(name.equalsIgnoreCase(formsList.get(i).getfName())){
					currentFormation = i;
					//System.out.println(formsList.get(i).getPlayers().size());
					break;
				}
			}
		}
	}

	private void showFormation(){
		
		TiledTextureRegion tex = null;
		Body body = null;
		PlayerSprite newPlayer = null;
		FormationObject fn = formsList.get(currentFormation);
		
		for(PlayerObject p:fn.getPlayers()){
			
			if(((Player) p).getpTeam().equalsIgnoreCase("blue")){
				tex = this.mBluePlayerTextureRegion;
			}
			else if(((Player) p).getpTeam().equalsIgnoreCase("red")){
				tex = this.mRedPlayerTextureRegion;
			}
			newPlayer = createPlayer((Player) p, tex);
			body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, newPlayer, BodyType.DynamicBody, FIXTURE_DEF);
			this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(newPlayer, body, true, true));

			this.mMainScene.getChild(PLAYER_LAYER).attachChild(newPlayer);
			this.mMainScene.registerTouchArea(newPlayer);		
			this.playerSprites.add(newPlayer);
		}
		
		//System.out.println(config.playerInfoDisplayToggle);
		
		if(config.playerInfoDisplayToggle == true && config.playerInfoDisplayWhenMode == 1){
			for(PlayerSprite p1:playerSprites){
				p1.displayInfo(true);
			}
		}
		else if(config.playerInfoDisplayToggle == false){
			for(PlayerSprite p1:playerSprites){
				p1.displayInfo(false);
			}
		}
		
		this.mBall.setPosition(fn.getBall().getX(), fn.getBall().getY());
		this.mMainScene.getChild(BALL_LAYER).attachChild(this.mBall);

		this.mMainScene.registerTouchArea(this.mBall);
	}

	protected PlayerSprite createPlayer(final Player p, TiledTextureRegion tex){
		
		ChangeableText playerText;
		
		final PlayerSprite newPlayer = new PlayerSprite(p, p.getX(), p.getY(), tex){
			
				@Override
				public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY){
					
					switch(pMenuItem.getID()) {
					
						case Constants.PMENU_VIEW:
							System.out.println("here");
							Intent intent = new Intent(BaseBoard.this, ViewPlayer.class);
							//p.getpInfo().writeToParcel(data, 0);
							
		
							intent.putExtra("player", p.getpInfo());
							BaseBoard.this.startActivity(intent);
							BaseBoard.this.mMainScene.clearChildScene();

							return true;
	
						case Constants.PMENU_SWAP:
							String display;
							
							Intent intent1 = new Intent(BaseBoard.this, SelectPlayer.class);
							playerNames = new String[BaseBoard.this.players.size()];
							for(int i = 0; i < players.size(); i++){
								display = "";
								display += players.get(i).getFirstName().charAt(0);
						        display += ". ";						         
						        display += players.get(i).getLastName();
						        display += "\t\t\t\t\t#";
						        display += players.get(i).getjNum();
						        display += "\t\t\t" + players.get(i).getType();
								playerNames[i] = display;
							}
							//intent1.putExtra("size", players.size());
							//for(PlayerInfo p:players){
								//intent1.putExtra("player", p);
							//}
							BaseBoard.this.startActivityForResult(intent1, 6);
							
							BaseBoard.this.mMainScene.clearChildScene();
							
							return true;
							
	/*				    case Constants.PMENU_HIDE:
					    	
					    	mEngine.runOnUpdateThread(new Runnable() {
					    	
					    		@Override

					    		public void run() {
					    			final Body body = BaseBoard.this.mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(selectedPlayer);
					    			BaseBoard.this.mPhysicsWorld.destroyBody(body);
					    			BaseBoard.this.mMainScene.getChild(PLAYER_LAYER).detachChild(selectedPlayer);
					    			BaseBoard.this.playerSprites.remove(selectedPlayer);
					    			BaseBoard.this.mMainScene.unregisterTouchArea(selectedPlayer);
							   
					    		}

					    	});
					        // Remove the menu and reset it. 
					        mMainScene.clearChildScene();

					        return true;*/
					   
					    case Constants.PMENU_EXIT:
					    	mMainScene.clearChildScene();

					    	return true;
					    default:
					    	mMainScene.clearChildScene();
					        return false;
					}	
				}
			};
			
		
		playerText = new ChangeableText(+10, -20, this.mPlayerInfoFont, p.getpInfo().getInitials(), 30);
		newPlayer.addDisplayInfo(playerText);
		
		playerText = new ChangeableText(-25, +10, this.mPlayerInfoFont, p.getpInfo().getType(), 30);
		newPlayer.addDisplayInfo(playerText);
		
		playerText = new ChangeableText(+10, +50, this.mPlayerInfoFont, String.valueOf(p.getpInfo().getjNum()), 30);
		newPlayer.addDisplayInfo(playerText);
		
		
		return newPlayer;
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
			
			if(sprite.getPlayer().getpTeam().equalsIgnoreCase("red")){
				LineFactory.setColor(config.rTeamLineColor);
			}
			else if(sprite.getPlayer().getpTeam().equalsIgnoreCase("blue")){
				LineFactory.setColor(config.bTeamLineColor);
			}
			
			switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					
					sprite.setScale(2.0f);	
					sprite.setmGrabbed(true);
					if(config.playerInfoDisplayToggle && config.playerInfoDisplayWhenMode == 0){
						sprite.displayInfo(true);
					}
					path.clear();
					sprite.setStartX(sprite.getX()+24);
					sprite.setStartY(sprite.getY()+24);
				
					left = LineFactory.createLine(0, 0, 0, 0, 8);
					right = LineFactory.createLine(0, 0, 0, 0, 8);
					
					this.mMainScene.getChild(LINE_LAYER).attachChild(left);
					this.mMainScene.getChild(LINE_LAYER).attachChild(right);
				
					lines.add(left);
					lines.add(right);
					
					return true;
					
				case TouchEvent.ACTION_MOVE:
					
					if(sprite.ismGrabbed()) {
	
						sprite.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						body.setTransform(new Vector2(pSceneTouchEvent.getX()/32,  pSceneTouchEvent.getY()/32), 0);
						
						moveX = sprite.getX();
						moveY = sprite.getY();
						
						path.add(new Coordinates(sprite.getX()-24, sprite.getY()-24));

						
						if(config.lineEnabled){
						
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
						if(config.playerInfoDisplayToggle && config.playerInfoDisplayWhenMode == 0){
							sprite.displayInfo(false);
						}
						
						if(recording){
							if(path.size()>1){

								Xs = new float[path.size()];
								Ys = new float[path.size()];

								for(int i = path.size()-1; i >= 0; i--){
									Xs[i] = path.get(i).getX();
									Ys[i] = path.get(i).getY();
								}
								Ys[0] = Ys[0] + 24;
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
					
					return true;
					
				case Constants.STOP_BUTTON:
					recording = false;
					for(SpritePath path:pathList){
						path.getSprite().clearEntityModifiers();
					}
					return true;
			
				case Constants.PAUSE_BUTTON:
					
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
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
				
		switch(pMenuItem.getID()) {
			case Constants.MAIN_MENU_SETTINGS:
				this.startActivityForResult(new Intent(this, SettingsViewer.class), 4);
				this.mMainMenu.back();
				return true;
			case Constants.MAIN_MENU_PLAYBACK:
				this.playBackEnabled = true;
				return super.onMenuItemClicked(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
			case Constants.MAIN_MENU_RESET:
				
				clearScene();
				
				loadFormation(formsList.get(currentFormation).getfName());
				
				showFormation();
				this.mMainMenu.back();

				return true;

			case Constants.MAIN_MENU_CLEARLINES:
				
				this.clearLines();
				this.mMainMenu.back();

				return true;
				
			case Constants.MAIN_MENU_FORMATIONS:
				
				return super.onMenuItemClicked(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
				
			case Constants.FORMATIONS_SUBMENU_SAVE:
				formNames = new String[this.formsList.size()];
				for(int i = 0; i < formsList.size(); i++){
					formNames[i] = formsList.get(i).getfName();
				}
				this.startActivityForResult(new Intent(this, SaveForm.class), 1);
				
				this.mMainMenu.back();
				return true;
			
			case Constants.FORMATIONS_SUBMENU_DELETE:
				
				formNames = new String[this.formsList.size()];
				for(int i = 0; i < formsList.size(); i++){
					formNames[i] = formsList.get(i).getfName();
				}
				
				this.startActivityForResult(new Intent(this, DeleteForm.class), 3);
				this.mMainMenu.back();
				return true;
				
			//get a list of formations	
			case Constants.FORMATIONS_SUBMENU_LOAD:
				
				formNames = new String[this.formsList.size()];
				for(int i = 0; i < formsList.size(); i++){
					formNames[i] = formsList.get(i).getfName();
				}
				
				this.startActivityForResult(new Intent(this, LoadForm.class), 2);
				this.mMainMenu.back();
				return true;
				
			case Constants.MAIN_MENU_PLAYERS:
				
				return super.onMenuItemClicked(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
				
			case Constants.PLAYERS_SUBMENU_CREATE:
				
				this.startActivityForResult(new Intent(this, CreatePlayer.class), 5);
				this.mMainMenu.back();
				return true;
				
			case Constants.PLAYERS_SUBMENU_VIEW:
				String display;
				playerNames = new String[BaseBoard.this.players.size()];
				for(int i = 0; i < players.size(); i++){
					display = "";
					display += players.get(i).getFirstName().charAt(0);
			        display += ". ";						         
			        display += players.get(i).getLastName();
			        display += "\t\t\t\t\t#";
			        display += players.get(i).getjNum();
			        display += "\t\t\t" + players.get(i).getType();
					playerNames[i] = display;
				}
				this.startActivity(new Intent(this, SelectPlayer.class));
				this.mMainMenu.back();
				return true;
				
			default:
				return false;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int receiveCode, Intent intent){
		
		List<PlayerObject> playerList = new ArrayList<PlayerObject>();
		System.out.println("Receive code: " +receiveCode);	
		//boolean exists = false;
		FormationObject fn = null;
		//int index = 0;
		
		//save form activity

		if(requestCode == 1){
			if(receiveCode == 1){

				playerList = captureFormation();
				
				if(intent == null){
					fn = new FormationObject(formsList.get(receiveCode).getfName(), new Coordinates(this.mBall.getX(), this.mBall.getY()), playerList);
					formsList.set(receiveCode, fn);
				}
				else{
					fn = new FormationObject(intent.getType(), new Coordinates(this.mBall.getX(), this.mBall.getY()), playerList);
					formsList.add(fn);
				}
				
				
				StringPrinting.printAllFormation(formsList);
				XMLAccess.writeFormations(this, formsList, SPORT_NAME.toLowerCase());
			}
		}
		//load form activity
		else if(requestCode == 2){
			if(receiveCode != -1){
				fn = formsList.get(receiveCode);
				clearScene();
				loadFormation(fn.getfName());
				showFormation();
			}
		}
		//delete form activity
		else if(requestCode == 3){
			
			if(receiveCode != -1){

				if(formsList.get(receiveCode).getfName().equalsIgnoreCase(DEFAULT_NAME)){
					
				}
				else{
					formsList.remove(receiveCode);
					XMLAccess.writeFormations(this, formsList, SPORT_NAME.toLowerCase());
				}
			}
		}
		//check results of settings
		else if(requestCode == 4){
			
			int menuTextColor = config.menuTextColor;
			
			updateConfig();
			PlayerSprite.displayMode = config.playerInfoDisplayWhatMode;
			if(config.playerInfoDisplayToggle == true && config.playerInfoDisplayWhenMode == 1){
				for(PlayerSprite p:playerSprites){
					p.displayInfo(true);
				}
			}
			else if(config.playerInfoDisplayToggle == false){
				for(PlayerSprite p:playerSprites){
					p.displayInfo(false);
				}
			}
			if(menuTextColor != config.menuTextColor){
				
				createMainMenu();
				createFormationsSubMenu();
				createPlayersSubMenu();
				createPlayerContextMenu();
				
			}
		}
		//create a new player
		else if(requestCode == 5 && receiveCode == 5){
			
			PlayerInfo newPlayer = (PlayerInfo)intent.getParcelableExtra(getString(R.string.players_create));
			
			StringPrinting.printPlayerInfo(newPlayer);
			
			players.add(newPlayer);
			
			XMLAccess.writePlayers(this, players, SPORT_NAME.toLowerCase());
			playerIDCounter++;
		}
		//player swap from player context menu
		else if(requestCode == 6 && receiveCode > 0){
			selectedPlayer.swap(players.get(receiveCode));
		}
		this.mMainScene.getChild(BALL_LAYER).getChild(0).setVisible(true);

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
	private List<FormationObject> matchPlayers(List<FormationObject> formEntries){
		
		List<FormationObject> forms = new ArrayList<FormationObject>();
		Player newPlayer = null;
		List<PlayerObject> matchList = new ArrayList<PlayerObject>();
		FormationObject fn = null;
		int pID;
		
		for(FormationObject fEntry:formEntries){
			matchList = new ArrayList<PlayerObject>();
			for(PlayerInfo pInfo:players){
				
				pID = pInfo.getpID();
				
				for(PlayerObject pEntry:fEntry.getPlayers()){
					
					if(pID == pEntry.getpID()){
						newPlayer = new Player(pID, ((PlayerEntry)pEntry).getpTeam(), ((PlayerEntry)pEntry).getCoords(), pInfo);
						matchList.add(newPlayer);
					}
				}
					
			}
			fn = new FormationObject(fEntry.getfName(), fEntry.getBall(), matchList);
			forms.add(fn);
		}
		
		return forms;
	}
	public void changePlayerSize(){
		this.mRedPlayerTexture.clearTextureSources();
		this.mBluePlayerTexture.clearTextureSources();
		this.mBallTexture.clearTextureSources();
		
		if(config.largePlayers){
			
			this.mRedPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mRedPlayerTexture, this, "32x32RED.png", 0, 0, 1, 1);
			this.mBluePlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBluePlayerTexture, this, "32x32BLUE.png", 0, 0, 1, 1);
			this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallTexture, this, BALL_PATH_SMALL, 0, 0, 1, 1);

			config.largePlayers = false;
		}
		else{
			this.mRedPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mRedPlayerTexture, this, "48x48RED.png", 0, 0, 1, 1);
			this.mBluePlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBluePlayerTexture, this, "48x48BLUE.png", 0, 0, 1, 1);
			this.mBallTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallTexture, this, BALL_PATH_SMALL, 0, 0, 1, 1);

			config.largePlayers = true;

		}
		this.mMainMenu.back();
	}
	
	@Override
	public void finish(){
		
		SharedPreferences settings = getSharedPreferences(getString(R.string.settings), 0);

		SharedPreferences.Editor editor = settings.edit();
		
		editor.putString("last loaded", formsList.get(currentFormation).getfName());
		editor.commit();
		super.finish();
	}

}

