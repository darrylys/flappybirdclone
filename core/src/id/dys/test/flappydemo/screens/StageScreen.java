package id.dys.test.flappydemo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

import id.dys.test.flappydemo.FlappyDemo;
import id.dys.test.flappydemo.GameConstants;
import id.dys.test.flappydemo.sounds.SoundWrapper;
import id.dys.test.flappydemo.sprites.bird.BirdSprite;
import id.dys.test.flappydemo.sprites.bird.BirdSpriteWrapper;
import id.dys.test.flappydemo.sprites.digits.DigitSpriteWriter;
import id.dys.test.flappydemo.sprites.pipes.PipeSprite;

public class StageScreen implements Screen {

    private final FlappyDemo game;

    private final Texture bgDay;
    private final Texture bgBase;

    private final SoundWrapper soundWrapper;
    private final BirdSpriteWrapper birdSpriteWrapper;
    private final BirdSprite birdSprite;
    private final PipeSprite pipeSprite;
    private final DigitSpriteWriter digitSpriteWriter;

    private final OrthographicCamera camera;

    private final Rectangle birdRect;
    private final Array<Rectangle> pipeUpRects;
    private final Array<Rectangle> pipeDownRects;
    private long lastPipeSpawn;

    private int birdHitsPipeCount = 0;
    private float ySpeed = 0.0f;

    public StageScreen(FlappyDemo game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.SCREEN_W, GameConstants.SCREEN_H);

        bgDay = new Texture(Gdx.files.internal("background_day.png"));
        bgBase = new Texture(Gdx.files.internal("base.png"));
        soundWrapper = new SoundWrapper();
        birdSpriteWrapper = new BirdSpriteWrapper();
        birdSprite = birdSpriteWrapper.getRandom();
        pipeSprite = new PipeSprite();
        digitSpriteWriter = new DigitSpriteWriter();

        birdRect = createBirdRect();
        pipeUpRects = new Array<>();
        pipeDownRects = new Array<>();
        spawnPipe();
    }

    private void spawnUpPipe(float startUpPipeY) {
        Rectangle r = new Rectangle();
        r.x = GameConstants.SCREEN_W;
        r.y = startUpPipeY;
        r.width = GameConstants.PIPE_W;
        r.height = GameConstants.PIPE_H;
        pipeUpRects.add(r);
    }

    private float spawnDownPipe() {
        Rectangle r = new Rectangle();
        r.x = GameConstants.SCREEN_W;
        int pipeHeight = MathUtils.random(100, 200) + bgBase.getHeight();
        r.y = pipeHeight - GameConstants.PIPE_H;
        r.width = GameConstants.PIPE_W;
        r.height = GameConstants.PIPE_H;
        pipeDownRects.add(r);
        return pipeHeight;
    }

    private void spawnPipe() {
        float pipeDownHeight = spawnDownPipe();
        int pipeGap = MathUtils.random(100, 200);
        spawnUpPipe(pipeDownHeight + pipeGap);
        lastPipeSpawn = TimeUtils.nanoTime();
    }

    private Rectangle createBirdRect() {
        Rectangle r = new Rectangle();
        r.x = GameConstants.SCREEN_W / 2 - GameConstants.BIRD_W / 2;
        r.y = GameConstants.SCREEN_H / 2 - GameConstants.BIRD_H / 2;
        r.width = GameConstants.BIRD_W;
        r.height = GameConstants.BIRD_H;
        return r;
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
        birdSprite.draw(game.getBatch(), birdRect.x, birdRect.y);
        for (Rectangle r : pipeUpRects) {
            pipeSprite.drawUpPipe(game.getBatch(), r.x, r.y);
        }
        for (Rectangle r : pipeDownRects) {
            pipeSprite.drawDownPipe(game.getBatch(), r.x, r.y);
        }
        game.getBatch().draw(bgBase, 0, 0);
//        game.getFont().draw(game.getBatch(),
//                "Bird Hits: " + birdHitsPipeCount, 0, GameConstants.SCREEN_H - game.getFont().getCapHeight());
        digitSpriteWriter.drawCenteredAtHeight(game.getBatch(), "" + birdHitsPipeCount, GameConstants.SCORE_Y);

        game.getBatch().end();
    }

    private void updateGameWorld(float delta) {
        if (Gdx.input.isTouched()) {
            ySpeed = GameConstants.BIRD_INC_SPEED_Y;
        } else {
            ySpeed -= GameConstants.BIRD_GRAVITY;
        }

        birdRect.y += ySpeed * delta;

        if (birdRect.y < bgBase.getHeight()) {
            birdRect.y = bgBase.getHeight();
            ySpeed = 0.0f;
        }

        if (birdRect.y > GameConstants.SCREEN_H - GameConstants.BIRD_H) {
            birdRect.y = GameConstants.SCREEN_H - GameConstants.BIRD_H;
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

        movePipe(pipeDownRects, delta);
        movePipe(pipeUpRects, delta);

        checkBirdOverlapsWithPipe(pipeDownRects);
        checkBirdOverlapsWithPipe(pipeUpRects);

        removePipeIfRequired(pipeDownRects);
        removePipeIfRequired(pipeUpRects);

    }

    private void movePipe(Array<Rectangle> pipes, float delta) {
        for (Rectangle r : pipes) {
            r.x -= GameConstants.PIPE_SPEED_X * delta;
        }
    }

    private void checkBirdOverlapsWithPipe(Array<Rectangle> pipes) {
        for (Iterator<Rectangle> iter = pipes.iterator(); iter.hasNext();) {
            Rectangle r = iter.next();
            if (birdRect.overlaps(r)) {
                birdHitsPipeCount ++;
                iter.remove();
            }
        }
    }

    private void removePipeIfRequired(Array<Rectangle> pipes) {
        for (Iterator<Rectangle> iter = pipes.iterator(); iter.hasNext();) {
            Rectangle r = iter.next();
            if (r.x + GameConstants.PIPE_W < 0) {
                iter.remove();
            }
        }
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
        bgBase.dispose();
        bgDay.dispose();
        soundWrapper.dispose();
        birdSpriteWrapper.dispose();
        pipeSprite.dispose();
        digitSpriteWriter.dispose();
    }
}
