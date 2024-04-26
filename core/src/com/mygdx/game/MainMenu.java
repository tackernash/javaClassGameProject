package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenu implements Screen{
    MyGdxGame game;

    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;

    public MainMenu(){
        this.game = game;
        playButtonActive = new Texture("playActive.png");
        playButtonInactive = new Texture("playInactive.png");
        exitButtonActive = new Texture("exitActive.png");
        exitButtonInactive = new Texture("exitInactive.png");


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 1, 0);

        game.batch.begin();

        game.batch.draw(exitButtonActive, game.screenWidth/2, game.screenHeight/2);

        game.batch.end();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
