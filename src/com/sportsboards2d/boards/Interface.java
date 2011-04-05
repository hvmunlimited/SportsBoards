package com.sportsboards2d.boards;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.detector.HoldDetector;
import org.anddev.andengine.input.touch.detector.HoldDetector.IHoldDetectorListener;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sportsboards2d.R;
import com.sportsboards2d.db.objects.Configuration;
import com.sportsboards2d.sprites.ButtonSprite;
import com.sportsboards2d.sprites.PlayerSprite;
import com.sportsboards2d.util.Colors;
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
	protected static final int BALL_LAYER = 2;
	protected static final int PLAYER_LAYER = 1;
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
	
	protected boolean playBackEnabled = false;
	
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
	
	protected Texture mMenuFontTexture;
	protected Font mMenuFont;
	
	protected Scene mMainScene;
	protected MenuScene mMainMenu;
	protected MenuScene mPlayerContextMenu;
	
	protected PlayerSprite selectedPlayer;
	
	private List<IMenuItem> menuItems = new ArrayList<IMenuItem>();
	protected List<ButtonSprite> buttons = new ArrayList<ButtonSprite>();

	protected PhysicsWorld mPhysicsWorld;
	
	protected Configuration config;
	
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
		
	}

	@Override
	public Scene onLoadScene() {
		
		this.mMainScene = new Scene(7);
		createMainMenu();
		createPlayerContextMenu();
		
		this.mHoldDetector = new HoldDetector(new IHoldDetectorListener(){

			@Override
			public void onHold(HoldDetector arg0, long arg1, float arg2, float arg3){}

			@Override
			public void onHoldFinished(final HoldDetector pHoldDetector, long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY){
				
				if(pHoldX >= 900){
					Interface.this.menuItems.get(Constants.PMENU_HIDE).setPosition(pHoldX-128, pHoldY);
					Interface.this.menuItems.get(Constants.PMENU_EXIT).setPosition(pHoldX-128, pHoldY-48);
				}
				else{
					Interface.this.menuItems.get(Constants.PMENU_HIDE).setPosition(pHoldX+24, pHoldY);
					Interface.this.menuItems.get(Constants.PMENU_EXIT).setPosition(pHoldX+24, pHoldY-48);
				}
				Interface.this.mPlayerContextMenu.setOnMenuItemClickListener(selectedPlayer);
				Interface.this.mMainScene.setChildScene(Interface.this.mPlayerContextMenu, false, true, true);
				
			}
			
		});
		
		
		this.mHoldDetector.setTriggerHoldMinimumMilliseconds(900);
		this.mMainScene.registerUpdateHandler(this.mHoldDetector);
		
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
		this.mMainScene.getChild(BUTTON_LAYER).attachChild(button);

		button = new ButtonSprite(400, 0, this.mRecordButtonTextureRegion);
		button.setVisible(false);
		buttons.add(button);
		this.mMainScene.getChild(BUTTON_LAYER).attachChild(button);

		button = new ButtonSprite(475, 0, this.mStopButtonTextureRegion);
		button.setVisible(false);
		buttons.add(button);
		this.mMainScene.getChild(BUTTON_LAYER).attachChild(button);

		button = new ButtonSprite(550, 0, this.mRewindButtonTextureRegion);
		button.setVisible(false);
		buttons.add(button);
		this.mMainScene.getChild(BUTTON_LAYER).attachChild(button);

		button = new ButtonSprite(625, 0, this.mPauseButtonTextureRegion);
		button.setVisible(false);
		buttons.add(button);
		this.mMainScene.getChild(BUTTON_LAYER).attachChild(button);

	
		return this.mMainScene;
	}
	
	private void onLoadConfig(){
	
		SharedPreferences settings = getSharedPreferences("settings", 0);
		String default_board = settings.getString("default_board", null);
		config = new Configuration();

		
		//No preferences saved, (meaning first boot of the app)
		if(default_board == null){
			System.out.println("no prefs");
			
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("default_board", "basketball");
			editor.putBoolean(getString(R.string.lineEnabled), false);
			editor.putInt(getString(R.string.rTeamLineColor), Colors.RED);
			editor.putInt(getString(R.string.bTeamLineColor), Colors.BLUE);
			editor.putBoolean(getString(R.string.player_info_display), false);
			editor.putInt(getString(R.string.display_when), 0);
			editor.putInt(getString(R.string.display_what), 0);
			editor.putInt(getString(R.string.menuTextColor), 0);
			
			
			editor.commit();
			updateConfig(settings);
		}
		else{
			updateConfig(settings);
		}	
	}
	protected void updateConfig(SharedPreferences settings){
		config.largePlayers = true;
		config.lineEnabled = settings.getBoolean("lineEnabled", false);
		config.rTeamLineColor = settings.getInt(getString(R.string.rTeamLineColor), Colors.RED);
		config.bTeamLineColor = settings.getInt(getString(R.string.bTeamLineColor), Colors.BLUE);
		config.playerInfoDisplayToggle = settings.getBoolean(getString(R.string.player_info_display), false);
		config.playerInfoDisplayWhenMode = settings.getInt(getString(R.string.display_when), 0);
		config.playerInfoDisplayWhatMode = settings.getInt(getString(R.string.display_what), 0);
		config.menuTextColor = settings.getInt(getString(R.string.menuTextColor), 0);
	}
	
	protected void updateMenuText(){
		
		int max = this.mMainMenu.getChild(0).getChildCount();
		IMenuItem item = null;

		switch(config.menuTextColor){
		
			case Colors.WHITE:
				
				for(int i = 0; i < max; i++){
					item = (IMenuItem)this.mMainMenu.getChild(0).getChild(i);
					item.setColor(1.0f, 1.0f, 1.0f);
					item.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				}
				break;
			case Colors.BLACK:
				for(int i = 0; i < max; i++){
					item = (IMenuItem)this.mMainMenu.getChild(0).getChild(i);
					item.setColor(0.0f, 0.0f, 0.0f);
					item.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				}
				break;	
			case Colors.RED:
				for(int i = 0; i < max; i++){
					this.mMainMenu.getChild(0).getChild(i).setColor(1.0f, 0.0f, 0.0f);
				}
				break;
			case Colors.GREEN:
				for(int i = 0; i < max; i++){
					this.mMainMenu.getChild(0).getChild(i).setColor(0.0f, 1.0f, 0.0f);
				}
				break;
			case Colors.BLUE:
				for(int i = 0; i < max; i++){
					this.mMainMenu.getChild(0).getChild(i).setColor(0.0f, 0.0f, 1.0f);
				}
				break;
			case Colors.ORANGE:
				for(int i = 0; i < max; i++){
					this.mMainMenu.getChild(0).getChild(i).setColor(1.0f, 0.35f, 0.0f);
				}
				break;
			case Colors.GREY:
				for(int i = 0; i < max; i++){
					this.mMainMenu.getChild(0).getChild(i).setColor(0.6f, 0.6f, 0.6f);
				}
				break;
			case Colors.YELLOW:
				for(int i = 0; i < max; i++){
					this.mMainMenu.getChild(0).getChild(i).setColor(1.0f, 1.0f, 0.0f);
				}
				break;
			
		}
	}
	
	/*
	 * 0.0f, 1,0f, 0.0f, 255.0f, 255.0f, 255.0f = white unselected, green on select
	 * 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f = black unselected, red on select
	 */
	
	protected void createMainMenu(){
	
		this.mMainMenu = new MenuScene(this.mCamera);
		
		final IMenuItem reset = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_RESET, this.mMenuFont, getString(R.string.reset)), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		reset.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(reset);
				
		final IMenuItem lineEnable = new ColorMenuItemDecorator(new TextMenuItem(Constants.SETTINGS_LINE_ENABLE, this.mMenuFont, getString(R.string.line_enable)), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		lineEnable.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//this.mMainMenu.addMenuItem(lineEnable);
		
		final IMenuItem clearLines = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_CLEARLINES, this.mMenuFont, getString(R.string.line_clear)), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		clearLines.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(clearLines);
		
		final IMenuItem save = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_SAVE, this.mMenuFont, getString(R.string.save_form)), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		save.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(save);
		
		final IMenuItem delete = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_DELETE, this.mMenuFont, getString(R.string.delete_form)), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		delete.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(delete);
		
		final IMenuItem load = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_LOAD, this.mMenuFont, getString(R.string.load_form)), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		load.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(load);

		final IMenuItem playback = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_PLAYBACK, this.mMenuFont, getString(R.string.playback)), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		playback.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(playback);
		
		final IMenuItem settings = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_SETTINGS, this.mMenuFont, getString(R.string.settings)), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		settings.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(settings);
		
		this.mMainMenu.buildAnimations();
		this.mMainMenu.setBackgroundEnabled(false);
		this.mMainMenu.setOnMenuItemClickListener(this);
	}
	
	
	
	private void createPlayerContextMenu(){
		
		this.mPlayerContextMenu = new MenuScene(this.mCamera);
		this.mPlayerContextMenu.buildAnimations();
		this.mPlayerContextMenu.setBackgroundEnabled(false);
		
		final IMenuItem deleteMenuItem = new ColorMenuItemDecorator(new TextMenuItem(Constants.PMENU_EXIT, this.mMenuFont, getString(R.string.exit_player_menu)), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
		deleteMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mPlayerContextMenu.addMenuItem(deleteMenuItem);
		menuItems.add(deleteMenuItem);
		
		
		final IMenuItem hideMenuItem = new ColorMenuItemDecorator(new TextMenuItem(Constants.PMENU_HIDE, this.mMenuFont, getString(R.string.hide_player)), 0.0f, 1.0f,0.0f, 255.0f, 255.0f, 255.0f);
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
			} 
			else {
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
		
			case Constants.MAIN_MENU_PLAYBACK:
				//attach settings menu
				this.mMainMenu.back();
				for(ButtonSprite b:buttons){
					if(b.isVisible()){
						b.setVisible(false);
						this.mMainScene.unregisterTouchArea(b);
					}
					else{
						b.setVisible(true);
						this.mMainScene.registerTouchArea(b);

					}
				}
				//this.mMainMenu.setChildSceneModal(mPlayBackMenu);
				return true;
				
			
			case Constants.SETTINGS_LINE_ENABLE:
				if(config.lineEnabled){
					config.lineEnabled = false;
				}
				else{
					config.lineEnabled = true;
				}
				//this.mSettingsMenu.back();
				this.mMainMenu.back();
				return true;

	
			default:
				return false;
		}
	}

}