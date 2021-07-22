package id.dys.test.flappydemo.sprites.bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class BirdSprite {

    private static final long FLAP_GAP_NANOS = 350000000;
    private static final int UP = 0, MID = 1, DOWN = 2;
    private int ci, li;
    private long lastFlapWingsNanos = 0;

    private final Texture[] img;

    public BirdSprite(String birdName) {
        img = new Texture[3];
        img[UP] = new Texture(Gdx.files.internal(birdName + "_upflap.png"));
        img[MID] = new Texture(Gdx.files.internal(birdName + "_midflap.png"));
        img[DOWN] = new Texture(Gdx.files.internal(birdName + "_downflap.png"));

        ci = MID;
        li = UP;
    }

    private int getAndUpdateNextIdx() {
        if (ci == UP) {
            ci = MID;
            li = UP;
        } else if (ci == DOWN) {
            ci = MID;
            li = DOWN;
        } else {
            if (li == UP) {
                ci = DOWN;
            } else {
                ci = UP;
            }
            li = MID;
        }
        return ci;
    }

    public void draw(SpriteBatch batch, float x, float y) {
        int flapIdx = ci;
        long currentNanos = TimeUtils.nanoTime();
        if (currentNanos - lastFlapWingsNanos >= FLAP_GAP_NANOS) {
            flapIdx = getAndUpdateNextIdx();
            lastFlapWingsNanos = currentNanos;
        }

        batch.draw(img[flapIdx], x, y);
    }

    public void dispose() {
        for (Texture texture : img) {
            texture.dispose();
        }
    }

}
