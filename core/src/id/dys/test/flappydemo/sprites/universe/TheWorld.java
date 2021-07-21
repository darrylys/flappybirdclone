package id.dys.test.flappydemo.sprites.universe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import id.dys.test.flappydemo.FlappyDemo;
import id.dys.test.flappydemo.GameConstants;
import id.dys.test.flappydemo.sprites.bird.BirdSprite;
import id.dys.test.flappydemo.sprites.bird.BirdSpriteWrapper;
import id.dys.test.flappydemo.sprites.digits.DigitSpriteWriter;
import id.dys.test.flappydemo.sprites.pipes.PipeSprite;

public class TheWorld {

    private final FlappyDemo game;

    private final Texture bgDay;
    private final Texture bgBase;

    private final BirdSpriteWrapper birdSpriteWrapper;
    private final BirdSprite birdSprite; // don't dispose, handled by birdSpriteWrapper.dispose()
    private final PipeSprite pipeSprite;
    private final DigitSpriteWriter digitSpriteWriter;

    private final Rectangle birdRect;
    private final Array<Rectangle> pipeUpRects;
    private final Array<Rectangle> pipeDownRects;

    public TheWorld(FlappyDemo game) {
        this(game, new MovingObjects());
    }

    public TheWorld(FlappyDemo game, MovingObjects obj) {
        this.game = game;
        bgDay = new Texture(Gdx.files.internal("background_day.png"));
        bgBase = new Texture(Gdx.files.internal("base.png"));

        birdSpriteWrapper = new BirdSpriteWrapper();
        birdSprite = birdSpriteWrapper.getRandom();
        pipeSprite = new PipeSprite();
        digitSpriteWriter = new DigitSpriteWriter();

        if (obj.birdRect == null) {
            this.birdRect = createBirdRect();
        } else {
            this.birdRect = obj.birdRect;
        }
        this.pipeUpRects = obj.pipeUpRects;
        this.pipeDownRects = obj.pipeDownRects;
    }

    private Rectangle createBirdRect() {
        Rectangle r = new Rectangle();
        r.x = GameConstants.SCREEN_W / 2 - GameConstants.BIRD_W / 2;
        r.y = GameConstants.SCREEN_H / 2 - GameConstants.BIRD_H / 2;
        r.width = GameConstants.BIRD_W;
        r.height = GameConstants.BIRD_H;
        return r;
    }

    private void spawnUpPipe(int startUpPipeY) {
        Rectangle r = new Rectangle();
        r.x = GameConstants.SCREEN_W;
        r.y = startUpPipeY;
        r.width = GameConstants.PIPE_W;
        r.height = GameConstants.PIPE_H;
        pipeUpRects.add(r);
    }

    private void spawnDownPipe(int pipeHeight) {
        Rectangle r = new Rectangle();
        r.x = GameConstants.SCREEN_W;
        r.y = pipeHeight - GameConstants.PIPE_H;
        r.width = GameConstants.PIPE_W;
        r.height = GameConstants.PIPE_H;
        pipeDownRects.add(r);
    }

    public void spawnPipe(int pipeDownHeight, int pipeGap) {
        spawnDownPipe(pipeDownHeight);
        spawnUpPipe(pipeDownHeight + pipeGap);
    }

    public int getBaseHeight() {
        return bgBase.getHeight();
    }

    public void incBirdYPositionBy(float dy) {
        birdRect.y += dy;
    }

    public boolean doesBirdHitGround() {
        return birdRect.y < bgBase.getHeight();
    }

    public void setBirdYPositionToFloor() {
        birdRect.y = bgBase.getHeight();
    }

    public void setBirdYPositionToCeiling() {
        birdRect.y = GameConstants.SCREEN_H - GameConstants.BIRD_H;
    }

    public boolean doesBirdHitCeiling() {
        return birdRect.y > GameConstants.SCREEN_H - GameConstants.BIRD_H;
    }

    public void draw(int score) {

        game.getBatch().draw(bgDay, 0, bgBase.getHeight());
        birdSprite.draw(game.getBatch(), birdRect.x, birdRect.y);
        for (Rectangle r : pipeUpRects) {
            pipeSprite.drawUpPipe(game.getBatch(), r.x, r.y);
        }
        for (Rectangle r : pipeDownRects) {
            pipeSprite.drawDownPipe(game.getBatch(), r.x, r.y);
        }
        game.getBatch().draw(bgBase, 0, 0);
        digitSpriteWriter.drawCenteredAtHeight(game.getBatch(), "" + score, GameConstants.SCORE_Y);

    }

    public void movePipesByDx(float timeDelta, float dx) {
        movePipe(pipeDownRects, timeDelta, dx);
        movePipe(pipeUpRects, timeDelta, dx);
    }

    private void movePipe(Array<Rectangle> pipes, float delta, float dx) {
        for (Rectangle r : pipes) {
            r.x += dx * delta;
        }
    }

    public int countBirdPipeOverlaps() {
        return checkBirdOverlapsWithPipe(pipeDownRects) + checkBirdOverlapsWithPipe(pipeUpRects);
    }

    private int checkBirdOverlapsWithPipe(Array<Rectangle> pipes) {
        int count = 0;
        for (Iterator<Rectangle> iter = pipes.iterator(); iter.hasNext();) {
            Rectangle r = iter.next();
            if (birdRect.overlaps(r)) {
                count++;
                iter.remove();
            }
        }
        return count;
    }

    public void removePipeIfExitingScreen() {
        removePipeIfExitingScreen(pipeDownRects);
        removePipeIfExitingScreen(pipeUpRects);
    }

    private void removePipeIfExitingScreen(Array<Rectangle> pipes) {
        for (Iterator<Rectangle> iter = pipes.iterator(); iter.hasNext();) {
            Rectangle r = iter.next();
            if (r.x + GameConstants.PIPE_W < 0) {
                iter.remove();
            }
        }
    }

    public MovingObjects get() {
        return new MovingObjects(birdRect, pipeUpRects, pipeDownRects);
    }

    public void dispose() {
        bgBase.dispose();
        bgDay.dispose();
        birdSpriteWrapper.dispose();
        pipeSprite.dispose();
        digitSpriteWriter.dispose();
    }

    public static class MovingObjects {
        private final Rectangle birdRect;
        private final Array<Rectangle> pipeUpRects;
        private final Array<Rectangle> pipeDownRects;

        public MovingObjects() {
            this(null, new Array<Rectangle>(), new Array<Rectangle>());
        }

        public MovingObjects(Rectangle birdRect, Array<Rectangle> pipeUpRects, Array<Rectangle> pipeDownRects) {
            this.birdRect = birdRect;
            this.pipeUpRects = pipeUpRects;
            this.pipeDownRects = pipeDownRects;
        }
    }

}
