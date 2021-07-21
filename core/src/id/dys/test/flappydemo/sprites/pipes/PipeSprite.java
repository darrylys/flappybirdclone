package id.dys.test.flappydemo.sprites.pipes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PipeSprite {

    private final Texture pipeUp, pipeDown;

    public PipeSprite() {
        pipeUp = new Texture(Gdx.files.internal("pipe_green_up.png"));
        pipeDown = new Texture(Gdx.files.internal("pipe_green.png"));
    }

    public void drawUpPipe(SpriteBatch batch, float x, float y) {
        batch.draw(pipeUp, x, y);
    }

    public void drawDownPipe(SpriteBatch batch, float x, float y) {
        batch.draw(pipeDown, x, y);
    }
/*
    public void draw(SpriteBatch batch, float x, float downHeight, float pipeGap) {
        batch.draw(pipeDown, x, downHeight - pipeDown.getHeight());
        batch.draw(pipeUp, x, downHeight + pipeGap);
    }
*/
    public void dispose() {
        pipeUp.dispose();
        pipeDown.dispose();
    }

}
