package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Iterator;
import java.util.Random;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;


public class MyGdxGame extends ApplicationAdapter {

	//height and width eventually wanna make menu
	public int screenWidth = 1920;
	public int screenHeight = 1080;

	//controls
	boolean toggleStats = false;

	//stats
	private double speedMultiplier = 1;
	private int lives = 1;
	private String statusOfGame = "running";
	private boolean objectConsumed = false;

	String speedString = String.valueOf(speedMultiplier);
	String livesString = String.valueOf(lives);


	//camera
	private OrthographicCamera camera;

	//sprites
	public SpriteBatch batch;
	Texture mainCharacterSprite;
	Texture finishLineSprite;
	Texture damageFloorSprite;
	Texture heartSprite;
	Texture sharpObjectSprite;

	//hit boxes
	private Rectangle mainCharacter;
	private Rectangle finishLine;
	private Rectangle heart;

	//need array for sharpObjects
	private Array<Rectangle> sharpObjects;
	private long lastDropTime;

	private Random random = new Random();
	int randomXFinishLine = random.nextInt(screenWidth - 32);
	int randomYFinishLine = random.nextInt(screenHeight - 32);

	int randomXHeart = random.nextInt(screenWidth - 32);
	int randomYHeart = random.nextInt(screenHeight - 32);

	//spawn sharp objects fxn
	private void spawnSharpObject(){
		Rectangle sharpObject = new Rectangle();
		sharpObject.x = MathUtils.random(0, screenWidth-64);
		sharpObject.y = screenHeight-64;
		sharpObject.width = 32;
		sharpObject.height = 32;
		sharpObjects.add(sharpObject);
		lastDropTime = TimeUtils.nanoTime();
	}

	private void generateDamageFloor(){
		Rectangle damageFloor = new Rectangle();
		damageFloor.x = MathUtils.random(0, screenWidth-64);
		damageFloor.y = MathUtils.random(0, screenHeight-64);
		damageFloor.width = 32;
		damageFloor.height = 32;
	}



	BitmapFont font;

	@Override
	public void create () {

		sharpObjectSprite = new Texture("sharpObject.png");
		heartSprite = new Texture("heart.png");
		mainCharacterSprite = new Texture("salmonSquare.png");
		finishLineSprite = new Texture("finishLineMain.png");

		font = new BitmapFont(Gdx.files.internal("test.fnt"),Gdx.files.internal("test.png"),false);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);
		batch = new SpriteBatch();

		//rectangle/hitbox for main character
		mainCharacter = new Rectangle();
		mainCharacter.x = screenWidth/4 - screenHeight/2;
		mainCharacter.y = 20;
		mainCharacter.width = 16;
		mainCharacter.height = 16;

		finishLine = new Rectangle();
		finishLine.x = randomXFinishLine;
		finishLine.y = randomYFinishLine;
		finishLine.width = 64;
		finishLine.height = 64;

		heart = new Rectangle();
		heart.x = randomXHeart - 50;
		heart.y = randomYHeart - 50;
		heart.width = 32;
		heart.height = 32;

		sharpObjects = new Array<Rectangle>();


	}

	@Override
	public void render () {

		ScreenUtils.clear(0, 0, 1, 0);

		//camera
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(finishLineSprite, finishLine.x, finishLine.y);
		batch.draw(mainCharacterSprite, mainCharacter.x, mainCharacter.y);

		//batch.draw(damageFloorSprite, damageFloor.x, damageFloor.y);

		//drawing of sharpObjects
		for(Rectangle sharpObject: sharpObjects){
			batch.draw(sharpObjectSprite, sharpObject.x, sharpObject.y);
		}

			font.getData().setScale(1);
			font.draw(batch, "speed:" + speedString, screenWidth/16, screenHeight-64);
			font.draw(batch, "lives:" + livesString, screenWidth/16, screenHeight-100);
			font.draw(batch, "status of game:" + statusOfGame, screenWidth/16, screenHeight-128);


		if(lives==0){
			font.getData().setScale(2);
			font.draw(batch, "GAME OVER", screenWidth/2,screenHeight/2);
		}


		if(mainCharacter.overlaps(heart)){
			objectConsumed = true;
			lives += 1;
			livesString = String.valueOf(lives);
			heart.x = -100;
			heart.y = -100;
		}

		if(objectConsumed == false){
			batch.draw(heartSprite, heart.x, heart.y);
		}

		batch.end();

		//spawn check for sharpObjects
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000){
			spawnSharpObject();
		}

		//sharpObjects falling for loop
		for(Iterator<Rectangle> iter = sharpObjects.iterator(); iter.hasNext();){
			Rectangle sharpObject = iter.next();
			sharpObject.y -= (100 * Gdx.graphics.getDeltaTime());
			if(sharpObject.y + 64 < 0){
				iter.remove();
			}
			if(sharpObject.overlaps(mainCharacter)){
				iter.remove();
				lives -= 1;
				livesString = String.valueOf(lives);

			}
		}




		//input
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {mainCharacter.x -= 200 * Gdx.graphics.getDeltaTime() * speedMultiplier;}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {mainCharacter.x += 200 * Gdx.graphics.getDeltaTime() * speedMultiplier;}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {mainCharacter.y += 200 * Gdx.graphics.getDeltaTime() * speedMultiplier;}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {mainCharacter.y -= 200 * Gdx.graphics.getDeltaTime() * speedMultiplier;}



		//speedMultipler (shift)
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
			speedMultiplier = 3;
			speedString = String.valueOf(speedMultiplier);}
		else{
			speedMultiplier = 1;
			speedString = String.valueOf(speedMultiplier);}

		//constraints of the width and height
		if(mainCharacter.x < 0) {
			mainCharacter.x = 0;
		}
		if(mainCharacter.y < 0) {
			mainCharacter.y = 0;
		}
		if(mainCharacter.y > screenHeight-32) {
			mainCharacter.y = screenHeight-32;
		}
		if(mainCharacter.x > screenWidth - 32){
			mainCharacter.x = screenWidth - 32;
		}

		if(mainCharacter.overlaps(finishLine)){
			statusOfGame = "finished";
		}

	}

	@Override
	public void dispose () {
		batch.dispose();
		mainCharacterSprite.dispose();
		finishLineSprite.dispose();
		heartSprite.dispose();
		damageFloorSprite.dispose();
		font.dispose();
		sharpObjectSprite.dispose();
	}
}
