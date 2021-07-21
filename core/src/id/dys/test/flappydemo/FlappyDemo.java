package id.dys.test.flappydemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import id.dys.test.flappydemo.screens.StageScreen;
import id.dys.test.flappydemo.screens.StartScreen;

public class FlappyDemo extends Game {
	private SpriteBatch batch;
	private BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		setScreen(new StartScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	@Override
	public void dispose () {
		font.dispose();
		batch.dispose();
	}
}
