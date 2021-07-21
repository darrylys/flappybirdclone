package id.dys.test.flappydemo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import id.dys.test.flappydemo.FlappyDemo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Flappy bird clone";
		config.height = 512 + 112;
		config.width = 288;
		new LwjglApplication(new FlappyDemo(), config);
	}
}
