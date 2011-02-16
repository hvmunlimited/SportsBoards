package com.sportsboards.boards;

import java.util.ArrayList;
import java.util.List;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchException;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sportsboards.R;
import com.sportsboards.db.Formation;
import com.sportsboards.db.PlayerInfo;
import com.sportsboards.db.parsing.XMLAccess;
import com.sportsboards.db.parsing.XMLWriter;
import com.sportsboards.sprites.BallSprite;
import com.sportsboards.sprites.PlayerSprite;

/**
 * Coded by Nathan King
 */

public abstract class BaseBoard extends Interface{
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected int NUM_PLAYERS;
	protected String SPORT_NAME;
	protected int resID;
	protected final String DEFAULT_NAME = "DEFAULT";
	private boolean LARGE_PLAYERS = true;
	
	private Scene mMainScene;
		
	protected Texture mBackgroundTexture;
	protected Texture mBallTexture;
	private Texture mRedPlayerTexture;
	private Texture mBluePlayerTexture;
	
	protected TextureRegion mBackGroundTextureRegion;
	protected TextureRegion mBallTextureRegion;
	private TextureRegion mRedPlayerTextureRegion;
	private TextureRegion mBluePlayerTextureRegion;
	
	private List<PlayerSprite> mRedTeam = new ArrayList<PlayerSprite>();
	private List<PlayerSprite> mBlueTeam = new ArrayList<PlayerSprite>();
	private BallSprite ball;
	
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
		
		mMainScene.setOnAreaTouchTraversalFrontToBack();
	
		return mMainScene;
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		 MenuInflater inflater = getMenuInflater();
		 inflater.inflate(R.layout.settings, pMenu);
		 return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		
		Formation fn = null;
		
		switch(item.getItemId()) {
			case R.id.reset:
				for(PlayerSprite p: mRedTeam){
					this.mMainScene.getTopLayer().removeEntity(p);	
				}
				for(PlayerSprite p: mBlueTeam){
					this.mMainScene.getTopLayer().removeEntity(p);
				}
				
				this.mMainScene.getTopLayer().removeEntity(this.ball);
				
				this.ball = null;
				this.mBlueTeam.clear();
				this.mRedTeam.clear();
				fn = loadFormation();
				showFormation(fn);
				break;
			/*case R.id.save:
				
				saveFormation();
				return false;
				
			case R.id.load:
				return false;
			case R.id.enable:
				return false;
			case R.id.disable:
				return false;*/
			case R.id.change_player_size:
				
				if(LARGE_PLAYERS){
					
					this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "32x32RED.png", 0, 0);
					this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mBluePlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mBluePlayerTexture, this, "32x32BLUE.png", 0, 0);
					this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, "Basketball_Ball_32.png", 0, 0);

					LARGE_PLAYERS = false;
				}
				else{
					
					this.mRedPlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mBluePlayerTexture = new Texture(64, 64, TextureOptions.BILINEAR);
					this.mRedPlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mRedPlayerTexture, this, "48x48RED.png", 0, 0);
					this.mBluePlayerTextureRegion = TextureRegionFactory.createFromAsset(this.mBluePlayerTexture, this, "48x48BLUE.png", 0, 0);
					this.mBallTextureRegion = TextureRegionFactory.createFromAsset(this.mBallTexture, this, "Basketball_Ball_48.png", 0, 0);

					LARGE_PLAYERS = true;
					
				}
				this.mEngine.getTextureManager().loadTextures(this.mRedPlayerTexture, this.mBluePlayerTexture, this.mBallTexture);
				
				for(PlayerSprite p: mRedTeam){
					this.mMainScene.getTopLayer().removeEntity(p);	
				}
				for(PlayerSprite p: mBlueTeam){
					this.mMainScene.getTopLayer().removeEntity(p);
				}
				
				this.mMainScene.getTopLayer().removeEntity(this.ball);
				
				this.ball = null;
				this.mBlueTeam.clear();
				this.mRedTeam.clear();
				fn = loadFormation();
				showFormation(fn);
				break;
				
			default:
				return false;
		}
		return true;
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
	
	/*
	 * Load formation from storage
	 */
	
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
	
	/*
	 * Display players and sprites
	 */
	
	public void showFormation(Formation fn){
		addBall(new BallSprite(fn.getBall().getX(), fn.getBall().getY(), this.mBallTextureRegion));
		for(PlayerInfo p:fn.getPlayers()){
			
			if(p.getTeamColor().equalsIgnoreCase("blue")){
				addPlayer(new PlayerSprite(p, p.getX(), p.getY(), 
							this.mBluePlayerTextureRegion), mBlueTeam);
			}
			else if(p.getTeamColor().equalsIgnoreCase("red")){
				addPlayer(new PlayerSprite(p, p.getX(), p.getY(),
							this.mRedPlayerTextureRegion), mRedTeam);
			}			
		}
	}
	/*
	 * Add a player sprite to the scene
	 */
	public void addPlayer(PlayerSprite p, List<PlayerSprite> list){
		mMainScene.getTopLayer().addEntity(p);
		mMainScene.registerTouchArea(p);
		list.add(p);
	}
	/*
	 * Add a ball sprite to the scene
	 */
	public void addBall(BallSprite ball){
		this.ball = ball;
		mMainScene.getTopLayer().addEntity(ball);
		mMainScene.registerTouchArea(ball);
	}
	
	
}

