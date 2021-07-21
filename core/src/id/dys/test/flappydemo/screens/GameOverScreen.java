package id.dys.test.flappydemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import id.dys.test.flappydemo.FlappyDemo;
import id.dys.test.flappydemo.GameConstants;
import id.dys.test.flappydemo.sprites.universe.TheWorld;

public class GameOverScreen implements Screen {

    private final FlappyDemo game;
    private final TheWorld theWorld;
    private final int playerScore;
    private final OrthographicCamera camera;

    private final Texture gameOver;

    public GameOverScreen(FlappyDemo game, TheWorld.MovingObjects objects, int playerScore) {
        this.game = game;
        this.theWorld = new TheWorld(game, objects);
        this.playerScore = playerScore;
        this.gameOver = new Texture(Gdx.files.internal("gameover.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.SCREEN_W, GameConstants.SCREEN_H);
    }

    @Override
    public void show() {

    }

    private void drawCurrent() {
        ScreenUtils.clear(0,0,0, 1); // all values have range [0,1]
        camera.update();

        game.getBatch().setProjectionMatrix(camera.combined); // ritual code, just copy paste.
        game.getBatch().begin();

        theWorld.draw(playerScore);
        game.getBatch().draw(gameOver,
                GameConstants.SCREEN_W / 2 - gameOver.getWidth() /  2.0f,
                GameConstants.SCREEN_H / 2 - gameOver.getHeight() / 2.0f - 50.0f);

        game.getBatch().end();
    }

    private void monitorInput() {
        if (Gdx.input.isTouched()) {
            game.setScreen(new StageScreen(game));
            dispose();
        }
    }

    @Override
    public void render(float delta) {
        drawCurrent();
        monitorInput();
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
        gameOver.dispose();
        theWorld.dispose();
    }
}
