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

	private static final int BACKGROUND_LAYER = 0;
	private static final int PLAYER_LAYER = 1;
	private static final int PMENU_LAYER = 2;
	
	private List<IMenuItem> menuItems = new ArrayList<IMenuItem>();
	private static final int PMENU_DELETE = 0;
	private static final int PMENU_HIDE = 1;

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
	
	private PlayerSprite selectedPlayer;
	
	
	//private Camera mCamera;
	
	private Scene mMainScene;
	private boolean LARGE_PLAYERS = true;
	
	private MenuScene mPlayerMenu;
	
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final Engine engine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
		
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
		this.mPlayerMenuFont = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
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
		this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "48x48RED.png", 0, 0);
		this.mBluePlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mBluePlayerTexture, this, "48x48BLUE.png", 0, 0);
		
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture, this.mBluePlayerTexture, this.mRedPlayerTexture, this.mBallTexture);
	}
	@Override
	public Scene onLoadScene(){
		
		super.onLoadScene();
		this.mMainScene = new Scene(3);
		
		this.mMainScene.setOnSceneTouchListener(this);
		this.mMainScene.setTouchAreaBindingEnabled(true);
		
		createPlayerMenu();

		mMainScene.getLayer(BACKGROUND_LAYER).addEntity(new Sprite(0, 0, this.mBackGroundTextureRegion));
		currentFormation = loadFormation();
		showFormation(currentFormation);
		
		return mMainScene;
	}
	
	public void clearScene(){
		
		this.mMainScene.clearTouchAreas();
		this.mMainScene.clearUpdateHandlers();
		
		for(PlayerSprite p: players){
			this.mMainScene.getLayer(PLAYER_LAYER).removeEntity(p);	
		}
		
		this.mMainScene.getLayer(PLAYER_LAYER).removeEntity(this.ball);
		
		for(Line line: lines){
			this.mMainScene.getLayer(PLAYER_LAYER).removeEntity(line);
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
				
				clearScene();
				showFormation(currentFormation);
				
				return true;
			
			case R.id.change_player_size:
				
				clearScene();
				this.mRedPlayerTexture.clearTextureSources();
				this.mBluePlayerTexture.clearTextureSources();
				this.mBallTexture.clearTextureSources();
				
				this.mEngine.getTextureManager().unloadTextures(this.mRedPlayerTexture, this.mBluePlayerTexture, this.mBallTexture);
				
				this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
				this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
				
				if(LARGE_PLAYERS){
					
					this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "32x32RED.png", 0, 0);
					this.mBluePlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mBluePlayerTexture, this, "32x32BLUE.png", 0, 0);
					this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, BALL_PATH + "32.png", 0, 0);

					LARGE_PLAYERS = false;
				}
				else{
					
					this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "48x48RED.png", 0, 0);
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
		
		for(int i = 0; i < mMainScene.getLayer(PLAYER_LAYER).getEntityCount(); i++){
			if((IEntity) mMainScene.getLayer(PLAYER_LAYER).getEntity(i) instanceof PlayerSprite){
				pSprite = (PlayerSprite)mMainScene.getLayer(PLAYER_LAYER).getEntity(i);
				pInfo = pSprite.getPlayerInfo();
				pInfo.setCoords(pSprite.getX(), pSprite.getY());
				playerList.add(pInfo);
			}
			else if((IEntity) mMainScene.getLayer(PLAYER_LAYER).getEntity(i) instanceof BallSprite){
				ball = (BallSprite)mMainScene.getLayer(PLAYER_LAYER).getEntity(i);
			}
		}
		fn.setBall(ball.getX(), ball.getY());
		fn.setPlayers(playerList);
		fn.setName("testing");
		XMLWriter.writeFormation(this, fn, SPORT_NAME.toLowerCase());
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
		
		
				
		final PlayerSprite newPlayer = new PlayerSprite(p, tex){
			
			float startX, startY, endX, endY;
			float moveX, moveY;
			Line arrowLineMain = null;
			//Line arrowLineWingLeft = null;
			//Line arrowLineWingRight = null;
			float xDiff, yDiff;
			float diff;
			float angleRad, angleDeg;
			

			@Override
			
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) throws NullPointerException{
				selectedPlayer = this;
				//mHold.
				mHold.onTouchEvent(pSceneTouchEvent);
			
				
				
				switch(pSceneTouchEvent.getAction()) {
					case TouchEvent.ACTION_DOWN:
						
						this.setScale(2.0f);	
						this.mGrabbed = true;
						this.setPosition(pSceneTouchEvent.getX()-24, pSceneTouchEvent.getY()-24);
						
						startX = this.getX();
						startY = this.getY();
						
						System.out.println(this.getTextureRegion().getHeight());
						
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
								
								mMainScene.getLayer(PLAYER_LAYER).addEntity(arrowLineMain);
								lines.add(arrowLineMain);
								startX = moveX;
								startY = moveY;
							}
							
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
							
								
							this.setPosition(pSceneTouchEvent.getX()-24, pSceneTouchEvent.getY()-24);

												
						}
						break;
					}
					return true;
				}
				@Override
				public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY){
					
					
					
					switch(pMenuItem.getID()) {
	
					    case PMENU_DELETE:
					    	
					    	mMainScene.getLayer(PLAYER_LAYER).removeEntity(selectedPlayer);
					    	//players.remove(selectedPlayer);
					        /* Remove the menu and reset it. */
					        mMainScene.clearChildScene();
					        mPlayerMenu.reset();
					        return true;
					   
					    case PMENU_HIDE:
					    	
					    	mMainScene.clearChildScene();
					    	mPlayerMenu.reset();
					    	
					    default:
					        return false;
					}
							
				}
			};
		
		
		newPlayer.setHoldDetector(new HoldDetector(new IHoldDetectorListener() {
			
			@Override
			public void onHold(HoldDetector arg0, long arg1, float arg2, float arg3) {
				
			}

			@Override
			public void onHoldFinished(final HoldDetector pHoldDetector, long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY){
				loadPlayerMenuItems();
				menuItems.get(PMENU_DELETE).setPosition(pHoldX, pHoldY);
				menuItems.get(PMENU_HIDE).setPosition(pHoldX, pHoldY-48);
				//System.out.println(pHoldX);
				
				menuItems.clear();
				mMainScene.setChildScene(mPlayerMenu, false, true, true);
				
			}
		}));
		
		mPlayerMenu.setOnMenuItemClickListener(newPlayer);
		this.mMainScene.getLayer(PLAYER_LAYER).addEntity(newPlayer);
		this.mMainScene.registerTouchArea(newPlayer);
		this.mMainScene.registerUpdateHandler(newPlayer.getHoldDetector());
		players.add(newPlayer);
	}
	
	public void addBall(BallSprite ball){
		
		this.ball = ball;
		this.mMainScene.getLayer(PLAYER_LAYER).addEntity(ball);
		this.mMainScene.registerTouchArea(ball);
	}
	
	public void createPlayerMenu(){
		
		mPlayerMenu = new MenuScene(this.mCamera);
		mPlayerMenu.buildAnimations();
		mPlayerMenu.setBackgroundEnabled(false);
	}
	public void loadPlayerMenuItems(){
		
		final IMenuItem deleteMenuItem = new ColorMenuItemDecorator(new TextMenuItem(PMENU_DELETE, this.mFont, "DELETE"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		deleteMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuItems.add(PMENU_DELETE, deleteMenuItem); 
		mPlayerMenu.addMenuItem(deleteMenuItem);

		
		final IMenuItem hideMenuItem = new ColorMenuItemDecorator(new TextMenuItem(PMENU_HIDE, this.mFont, "HIDE"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		deleteMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuItems.add(PMENU_DELETE, hideMenuItem);
		mPlayerMenu.addMenuItem(hideMenuItem);
		
	}
}

