package id.dys.test.flappydemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import id.dys.test.flappydemo.FlappyDemo;
import id.dys.test.flappydemo.GameConstants;

public class StartScreen implements Screen {

    private final FlappyDemo game;
    private final Texture bgDay, bgBase, message;
    private final OrthographicCamera camera;

    public StartScreen(FlappyDemo game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.SCREEN_W, GameConstants.SCREEN_H);

        bgDay = new Texture(Gdx.files.internal("background_day.png"));
        bgBase = new Texture(Gdx.files.internal("base.png"));
        message = new Texture(Gdx.files.internal("message.png"));
    }

    @Override
    public void show() {

    }

    private void drawCurrentWorld() {
        ScreenUtils.clear(0,0,0, 1); // all values have range [0,1]
        camera.update();

        game.getBatch().setProjectionMatrix(camera.combined); // ritual code, just copy paste.
        game.getBatch().begin();

        game.getBatch().draw(bgDay, 0, bgBase.getHeight());
        game.getBatch().draw(message,
                GameConstants.SCREEN_W / 2 - message.getWidth() /  2.0f,
                GameConstants.SCREEN_H / 2 - message.getHeight() / 2.0f);
        game.getBatch().draw(bgBase, 0, 0);

        game.getBatch().end();
    }

    private void handleInput(float delta) {
        if (Gdx.input.isTouched()) {
            game.setScreen(new StageScreen(game));
            dispose();
        }
    }

    @Override
    public void render(float delta) {
        drawCurrentWorld();

        handleInput(delta);
    }

    @Override
    public void resize(int width, int height) {

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
