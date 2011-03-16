package com.sportsboards2d.boards;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.animator.SlideMenuAnimator;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.HoldDetector;
import org.anddev.andengine.input.touch.detector.HoldDetector.IHoldDetectorListener;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sportsboards2d.db.Configuration;
import com.sportsboards2d.db.parsing.XMLAccess;
import com.sportsboards2d.sprites.ButtonSprite;
import com.sportsboards2d.sprites.PlayerSprite;
import com.sportsboards2d.util.Constants;

/**
 * Coded by Nathan King
 */

/**
 * Copyright 2011 5807400 Manitoba Inc. All rights reserved.
 */

public abstract class Interface extends BaseGameActivity implements IOnMenuItemClickListener, IOnAreaTouchListener{
	
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final int CAMERA_WIDTH = 1024;
	protected static final int CAMERA_HEIGHT = 600;
	
	protected static final int BACKGROUND_LAYER = 0;
	protected static final int BALL_LAYER = 1;
	protected static final int PLAYER_LAYER = 2;
	protected static final int LINE_LAYER = 3;
	protected static final int MENU_BORDER_LAYER = 4;
	protected static final int MENU_LAYER = 5;
	protected static final int BUTTON_LAYER = 6;

	
	protected static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 1.0f, 1.0f);

	// ===========================================================
	// Fields
	// ===========================================================
	
	protected Camera mCamera;
	protected HoldDetector mHoldDetector;
	
	protected boolean LARGE_PLAYERS = true;
	protected boolean LINE_ENABLED = false;
	
	//private Texture mMenuBorderTexture;
	//private TextureRegion mMenuBorderTextureRegion;
	
	private Texture mPlayButtonTexture;
	private Texture mRecordButtonTexture;
	private Texture mStopButtonTexture;
	private Texture mRewindButtonTexture;
	private Texture mPauseButtonTexture;
	private TiledTextureRegion mPlayButtonTextureRegion;
	private TiledTextureRegion mRecordButtonTextureRegion;
	private TiledTextureRegion mStopButtonTextureRegion;
	private TiledTextureRegion mRewindButtonTextureRegion;
	private TiledTextureRegion mPauseButtonTextureRegion;
	
	private Texture mMenuFontTexture;
	private Font mMenuFont;
	
	protected Scene mMainScene;
	protected MenuScene mMainMenu;
	private MenuScene mSettingsMenu;
	private MenuScene mLineSettingsMenu;
	private MenuScene mGeneralSettingsMenu;
	private MenuScene mPlayerSettingsMenu;
	private MenuScene mPlayBackMenu;
	
	
	protected MenuScene mPlayerContextMenu;
	
	protected PlayerSprite selectedPlayer;
	
	private List<IMenuItem> menuItems = new ArrayList<IMenuItem>();
	protected List<ButtonSprite> buttons = new ArrayList<ButtonSprite>();

	protected PhysicsWorld mPhysicsWorld;
	private Vector2 mTempVector;
	
	@Override 
	public void onLoadResources(){
		
		onLoadConfig();
		
		FontFactory.setAssetBasePath("font/");
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mPlayButtonTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mStopButtonTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPauseButtonTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mRecordButtonTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mRewindButtonTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.mPlayButtonTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mPlayButtonTexture, this, "play_button.png", 0, 0, 1, 1);
		this.mStopButtonTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mStopButtonTexture, this, "stop_button.jpg", 0, 0, 1, 1);
		this.mPauseButtonTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mPauseButtonTexture, this, "pause_button.jpg", 0, 0, 1, 1);
		this.mRecordButtonTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mRecordButtonTexture, this, "record_button.jpg", 0, 0, 1, 1);
		this.mRewindButtonTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mRewindButtonTexture, this, "rewind_button.jpg", 0, 0, 1, 1);
		
		this.mEngine.getTextureManager().loadTextures(this.mPlayButtonTexture, this.mStopButtonTexture, this.mPauseButtonTexture, this.mRecordButtonTexture, this.mRewindButtonTexture);
		
//		this.mMenuBorderTexture = new Texture(512 ,512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		this.mMenuBorderTextureRegion = TextureRegionFactory.createFromAsset(this.mMenuBorderTexture, this, "menu_border.png", 0, 0);

		this.mMenuFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuFont = FontFactory.createFromAsset(this.mMenuFontTexture, this, "VeraBd.ttf", 36, true, Color.WHITE);

		this.mEngine.getTextureManager().loadTexture(this.mMenuFontTexture);
		this.mEngine.getFontManager().loadFont(this.mMenuFont);
//		this.mEngine.getTextureManager().loadTexture(this.mMenuBorderTexture);
	
	}

	@Override
	public Scene onLoadScene() {
		
		this.mMainScene = new Scene(7);
		createMainMenu();
		createSettingsMenu();
		//createLineSettingsMenu();
		//createPlayerSettingsMenu();
		//createGeneralSettingsMenu();
		createPlayerContextMenu();
		
		this.mHoldDetector = new HoldDetector(new IHoldDetectorListener(){

			@Override
			public void onHold(HoldDetector arg0, long arg1, float arg2, float arg3){}

			@Override
			public void onHoldFinished(final HoldDetector pHoldDetector, long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY){
				
				if(pHoldX >= 900){
					Interface.this.menuItems.get(Constants.PMENU_DELETE).setPosition(pHoldX-128, pHoldY);
					Interface.this.menuItems.get(Constants.PMENU_HIDE).setPosition(pHoldX-128, pHoldY-48);
				}
				else{
					Interface.this.menuItems.get(Constants.PMENU_DELETE).setPosition(pHoldX+24, pHoldY);
					Interface.this.menuItems.get(Constants.PMENU_HIDE).setPosition(pHoldX+24, pHoldY-48);
				}
				Interface.this.mPlayerContextMenu.setOnMenuItemClickListener(selectedPlayer);
				Interface.this.mMainScene.setChildScene(Interface.this.mPlayerContextMenu, false, true, true);
				
			}
			
		});
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0.0f), false);

		final Shape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
		final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		this.mMainScene.getChild(BACKGROUND_LAYER).attachChild(ground);
		this.mMainScene.getChild(BACKGROUND_LAYER).attachChild(roof);
		this.mMainScene.getChild(BACKGROUND_LAYER).attachChild(left);
		this.mMainScene.getChild(BACKGROUND_LAYER).attachChild(right);

		
		
		this.mMainScene.registerUpdateHandler(this.mPhysicsWorld);
		
		ButtonSprite button = new ButtonSprite(325, 0, this.mPlayButtonTextureRegion);
		button.setVisible(false);
		buttons.add(button);
		this.mMainScene.getChild(PLAYER_LAYER).attachChild(button);

		button = new ButtonSprite(400, 0, this.mRecordButtonTextureRegion);
		button.setVisible(false);
		buttons.add(button);
		this.mMainScene.getChild(PLAYER_LAYER).attachChild(button);

		button = new ButtonSprite(475, 0, this.mStopButtonTextureRegion);
		button.setVisible(false);
		buttons.add(button);
		this.mMainScene.getChild(PLAYER_LAYER).attachChild(button);

		button = new ButtonSprite(600, 0, this.mRewindButtonTextureRegion);
		button.setVisible(false);
		buttons.add(button);
		this.mMainScene.getChild(PLAYER_LAYER).attachChild(button);

		button = new ButtonSprite(675, 0, this.mPauseButtonTextureRegion);
		button.setVisible(false);
		buttons.add(button);
		this.mMainScene.getChild(PLAYER_LAYER).attachChild(button);
		
		this.mHoldDetector.setTriggerHoldMinimumMilliseconds(300);
		this.mMainScene.registerUpdateHandler(this.mHoldDetector);
		
		
		
		return this.mMainScene;
	}
	
	public void onLoadConfig(){
		
		Configuration config = XMLAccess.loadConfig(this, "config");
		
		LINE_ENABLED = config.isLineEnabled();
		LARGE_PLAYERS = config.isLargePlayers();
	}
	
	public void createMainMenu(){
	
		this.mMainMenu = new MenuScene(this.mCamera);
		
		final IMenuItem reset = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_RESET, this.mMenuFont, "Reset"), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		reset.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(reset);
		
		final IMenuItem lineEnable = new ColorMenuItemDecorator(new TextMenuItem(Constants.SETTINGS_LINE_ENABLE, this.mMenuFont, "Toggle Line Draw"), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		lineEnable.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(lineEnable);
		
		final IMenuItem clearLines = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_CLEARLINES, this.mMenuFont, "Clear Lines"), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		clearLines.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(clearLines);
		
		final IMenuItem save = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_SAVE, this.mMenuFont, "Save Formation"), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		save.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(save);
		
		final IMenuItem delete = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_DELETE, this.mMenuFont, "Delete Formation"), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		delete.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(delete);
		
		final IMenuItem load = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_LOAD, this.mMenuFont, "Load Formation"), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		load.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(load);
/////////////////////		
		final IMenuItem settings = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_SETTINGS, this.mMenuFont, "Playback Mode"), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		settings.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(settings);
		
		this.mMainMenu.buildAnimations();
		this.mMainMenu.setBackgroundEnabled(false);
		this.mMainMenu.setOnMenuItemClickListener(this);
	}
	
	public void createSettingsMenu(){
		
		this.mSettingsMenu = new MenuScene(this.mCamera);
		
		final IMenuItem genSettings = new ColorMenuItemDecorator(new TextMenuItem(Constants.SETTINGS_MENU_GENERAL, this.mMenuFont, "System Settings"), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		genSettings.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mSettingsMenu.addMenuItem(genSettings);
		
		final IMenuItem lineSettings = new ColorMenuItemDecorator(new TextMenuItem(Constants.SETTINGS_MENU_LINE, this.mMenuFont, "Line Settings"), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		lineSettings.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mSettingsMenu.addMenuItem(lineSettings);
		
		final IMenuItem playerSettings = new ColorMenuItemDecorator(new TextMenuItem(Constants.SETTINGS_MENU_PLAYER, this.mMenuFont, "Player Settings"), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		playerSettings.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mSettingsMenu.addMenuItem(playerSettings);
		
		this.mSettingsMenu.setMenuAnimator(new SlideMenuAnimator());
		this.mSettingsMenu.buildAnimations();
		this.mSettingsMenu.setBackgroundEnabled(false);
		this.mSettingsMenu.setOnMenuItemClickListener(this);
	}
	
	public void createLineSettingsMenu(){
		
		this.mLineSettingsMenu = new MenuScene(this.mCamera);
		
		final IMenuItem lineColor = new ColorMenuItemDecorator(new TextMenuItem(Constants.SETTINGS_LINE_COLOR, this.mMenuFont, "Line Color"), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		lineColor.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mLineSettingsMenu.addMenuItem(lineColor);
		
		this.mLineSettingsMenu.setMenuAnimator(new SlideMenuAnimator());
		this.mLineSettingsMenu.buildAnimations();
		this.mLineSettingsMenu.setBackgroundEnabled(false);
		this.mLineSettingsMenu.setOnMenuItemClickListener(this);

	}
	
	public void createPlayerSettingsMenu(){
		this.mPlayerSettingsMenu = new MenuScene(this.mCamera);
		
		final IMenuItem playerSize = new ColorMenuItemDecorator(new TextMenuItem(Constants.SETTINGS_PLAYER_SIZE, this.mMenuFont, "Change Player Size"), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		playerSize.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mPlayerSettingsMenu.addMenuItem(playerSize);
		
		this.mPlayerSettingsMenu.setMenuAnimator(new SlideMenuAnimator());
		this.mPlayerSettingsMenu.buildAnimations();
		this.mPlayerSettingsMenu.setBackgroundEnabled(false);
		this.mPlayerSettingsMenu.setOnMenuItemClickListener(this);
		
	}
	
	public void createGeneralSettingsMenu(){
		
	}
	
	
	
	public void createPlayerContextMenu(){
		
		this.mPlayerContextMenu = new MenuScene(this.mCamera);
		this.mPlayerContextMenu.buildAnimations();
		this.mPlayerContextMenu.setBackgroundEnabled(false);
		
		final IMenuItem deleteMenuItem = new ColorMenuItemDecorator(new TextMenuItem(Constants.PMENU_DELETE, this.mMenuFont, "EXIT"), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		deleteMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mPlayerContextMenu.addMenuItem(deleteMenuItem);
		menuItems.add(deleteMenuItem);
		
		
		final IMenuItem hideMenuItem = new ColorMenuItemDecorator(new TextMenuItem(Constants.PMENU_HIDE, this.mMenuFont, "HIDE"), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		deleteMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mPlayerContextMenu.addMenuItem(hideMenuItem);
		menuItems.add(hideMenuItem);
		
	}
	
	@Override
	public boolean onKeyDown(int key, KeyEvent event){
		
		if(key == KeyEvent.KEYCODE_MENU){
			
			if(this.mMainScene.hasChildScene()) {
				/* Remove the menu and reset it. */
				this.mMainMenu.back();
				this.mMainScene.getChild(MENU_BORDER_LAYER).detachChildren();
			} else {
				/* Attach the menu. */
				
//				Sprite sprite = new Sprite(300, 165, this.mMenuBorderTextureRegion);
				
				//this.mMainScene.getChild(MENU_BORDER_LAYER).attachChild(sprite);
				this.mMainScene.setChildScene(this.mMainMenu, false, true, true);
			}
			return true;
		}
		
		else if(key == KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to exit?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                Interface.this.finish();
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		}
		else {
			return super.onKeyDown(key, event);
		}
	}
	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
				
		switch(pMenuItem.getID()) {
		
			case Constants.MAIN_MENU_SETTINGS:
				//attach settings menu
				
				this.mMainMenu.back();
				for(ButtonSprite b:buttons){
					b.setVisible(true);
					this.mMainScene.registerTouchArea(b);
				}
				//this.mMainMenu.setChildSceneModal(mPlayBackMenu);
				return true;
				
			case Constants.SETTINGS_MENU_GENERAL:
				this.mSettingsMenu.back();
				//attach general settings menu
				return true;
				
			case Constants.SETTINGS_MENU_LINE:
				//this.mMainMenu.clearChildScene();
				this.mSettingsMenu.setChildSceneModal(mLineSettingsMenu);
				//attach line settings menu
				return true;
				
				case Constants.SETTINGS_LINE_ENABLE:
					if(LINE_ENABLED){
						LINE_ENABLED = false;
					}
					else{
						LINE_ENABLED = true;
					}
					//this.mSettingsMenu.back();
					this.mMainMenu.back();
					return true;
					
				case Constants.SETTINGS_LINE_COLOR:
					
					return true;
			
				case Constants.SETTINGS_LINE_TYPE:
					
					return true;
					
			case Constants.SETTINGS_MENU_PLAYER:
				
				this.mSettingsMenu.setChildSceneModal(mPlayerSettingsMenu);
	
				return true;
				
			default:
				return false;
		}
	}
	

}