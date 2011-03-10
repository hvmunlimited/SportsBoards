package com.sportsboards2d.boards;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.animator.SlideMenuAnimator;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.input.touch.detector.HoldDetector;
import org.anddev.andengine.input.touch.detector.HoldDetector.IHoldDetectorListener;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.sportsboards2d.db.Configuration;
import com.sportsboards2d.db.parsing.XMLAccess;
import com.sportsboards2d.sprites.PlayerSprite;
import com.sportsboards2d.util.Constants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.KeyEvent;

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
	protected static final int MENU_LAYER = 4;

	
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected Camera mCamera;
	protected HoldDetector mHoldDetector;
	
	protected boolean LARGE_PLAYERS = true;
	protected boolean LINE_ENABLED = false;
	
	private Texture mMenuFontTexture;
	private Font mMenuFont;
	
	protected Scene mMainScene;
	protected MenuScene mMainMenu;
	private MenuScene mSettingsMenu;
	private MenuScene mLineSettingsMenu;
	//private MenuScene mGeneralSettingsMenu;
	private MenuScene mPlayerSettingsMenu;
	
	protected MenuScene mPlayerContextMenu;
	
	protected PlayerSprite selectedPlayer;
	
	private List<IMenuItem> menuItems = new ArrayList<IMenuItem>();

	
	@Override 
	public void onLoadResources(){
		
		onLoadConfig();
		
		FontFactory.setAssetBasePath("font/");
		TextureRegionFactory.setAssetBasePath("gfx/");

		this.mMenuFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuFont = FontFactory.createFromAsset(this.mMenuFontTexture, this, "UnrealTournament.ttf", 36, true, Color.RED);

		this.mEngine.getTextureManager().loadTexture(this.mMenuFontTexture);
		this.mEngine.getFontManager().loadFont(this.mMenuFont);
	
	}

	@Override
	public Scene onLoadScene() {
		
		this.mMainScene = new Scene(4);
		createMainMenu();
		createSettingsMenu();
		createLineSettingsMenu();
		createPlayerSettingsMenu();
		createGeneralSettingsMenu();
		
		createPlayerContextMenu();
		
		this.mHoldDetector = new HoldDetector(new IHoldDetectorListener(){

			@Override
			public void onHold(HoldDetector arg0, long arg1, float arg2, float arg3){}

			@Override
			public void onHoldFinished(final HoldDetector pHoldDetector, long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY){
				
				Interface.this.menuItems.get(Constants.PMENU_DELETE).setPosition(pHoldX+24, pHoldY);
				Interface.this.menuItems.get(Constants.PMENU_HIDE).setPosition(pHoldX+24, pHoldY-48);
				Interface.this.mPlayerContextMenu.setOnMenuItemClickListener(selectedPlayer);
				Interface.this.mMainScene.setChildScene(Interface.this.mPlayerContextMenu, false, true, true);
				
			}
			
		});
		this.mHoldDetector.setTriggerHoldMinimumMilliseconds(400);
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
		
		final IMenuItem reset = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_RESET, this.mMenuFont, "Reset"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		reset.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(reset);
		
		final IMenuItem clearLines = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_CLEARLINES, this.mMenuFont, "Clear Lines"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		clearLines.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(clearLines);
		
		final IMenuItem save = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_SAVE, this.mMenuFont, "Save Formation"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		save.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(save);
		
		final IMenuItem load = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_LOAD, this.mMenuFont, "Load Formation"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		load.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMainMenu.addMenuItem(load);
		
		final IMenuItem settings = new ColorMenuItemDecorator(new TextMenuItem(Constants.MAIN_MENU_SETTINGS, this.mMenuFont, "Settings"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
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
		
		final IMenuItem lineEnable = new ColorMenuItemDecorator(new TextMenuItem(Constants.SETTINGS_LINE_ENABLE, this.mMenuFont, "Enable/Disable Draw"), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		lineEnable.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mLineSettingsMenu.addMenuItem(lineEnable);
		
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
		
		final IMenuItem deleteMenuItem = new ColorMenuItemDecorator(new TextMenuItem(Constants.PMENU_DELETE, this.mMenuFont, "DELETE"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
		deleteMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mPlayerContextMenu.addMenuItem(deleteMenuItem);
		menuItems.add(deleteMenuItem);
		
		
		final IMenuItem hideMenuItem = new ColorMenuItemDecorator(new TextMenuItem(Constants.PMENU_HIDE, this.mMenuFont, "HIDE"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
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
			} else {
				/* Attach the menu. */
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
				this.mMainMenu.setChildSceneModal(mSettingsMenu);
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
					this.mSettingsMenu.back();
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