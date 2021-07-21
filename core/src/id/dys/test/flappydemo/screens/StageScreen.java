package id.dys.test.flappydemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import id.dys.test.flappydemo.FlappyDemo;
import id.dys.test.flappydemo.GameConstants;
import id.dys.test.flappydemo.sounds.SoundWrapper;
import id.dys.test.flappydemo.sprites.universe.TheWorld;

public class StageScreen implements Screen {

    private final FlappyDemo game;
    private final TheWorld theWorld;

    private final SoundWrapper soundWrapper;

    private final OrthographicCamera camera;

    private long lastPipeSpawn;

    private int birdHitsPipeCount = 0;
    private float ySpeed = 0.0f;

    public StageScreen(FlappyDemo game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.SCREEN_W, GameConstants.SCREEN_H);
        this.theWorld = new TheWorld(game);

        soundWrapper = new SoundWrapper();
        spawnPipe();
    }

    private void spawnPipe() {
        int pipeDownHeight = MathUtils.random(100, 200) + theWorld.getBaseHeight();
        int pipeGap = MathUtils.random(100, 200);
        theWorld.spawnPipe(pipeDownHeight, pipeGap);
        lastPipeSpawn = TimeUtils.nanoTime();
    }

    @Override
    public void show() {

    }

    private void drawCurrentWorld() {
        ScreenUtils.clear(0,0,0, 1); // all values have range [0,1]
        camera.update();

        game.getBatch().setProjectionMatrix(camera.combined); // ritual code, just copy paste.
        game.getBatch().begin();

        theWorld.draw(birdHitsPipeCount);

        game.getBatch().end();
    }

    private void updateGameWorld(float delta) {
        if (Gdx.input.isTouched()) {
            ySpeed = GameConstants.BIRD_INC_SPEED_Y;
        } else {
            ySpeed -= GameConstants.BIRD_GRAVITY;
        }

        theWorld.incBirdYPositionBy(ySpeed * delta);

        if (theWorld.doesBirdHitGround()) {
            theWorld.setBirdYPositionToFloor();
            ySpeed = 0.0f;
        }

        if (theWorld.doesBirdHitCeiling()) {
            theWorld.setBirdYPositionToCeiling();
            ySpeed = 0.0f;
        }

        if (TimeUtils.nanoTime() - lastPipeSpawn > GameConstants.SPAWN_TIME_GAP_IN_NANOS) {
            spawnPipe();
        }
    }

    @Override
    public void render(float delta) {
        drawCurrentWorld();

        updateGameWorld(delta);

        theWorld.movePipesByDx(delta, -GameConstants.PIPE_SPEED_X);

        birdHitsPipeCount += theWorld.countBirdPipeOverlaps();

        theWorld.removePipeIfExitingScreen();

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
        soundWrapper.dispose();
        theWorld.dispose();
    }
}
