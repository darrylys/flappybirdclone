package id.dys.test.flappydemo.sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundWrapper {

    public enum Name {
        die, hit, point, swoosh, wing
    }

    private final Sound die, hit, point, swoosh, wing;

    public SoundWrapper() {
        die = Gdx.audio.newSound(Gdx.files.internal("die.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
        point = Gdx.audio.newSound(Gdx.files.internal("point.wav"));
        swoosh = Gdx.audio.newSound(Gdx.files.internal("swoosh.wav"));
        wing = Gdx.audio.newSound(Gdx.files.internal("wing.wav"));
    }

    public long play(Name name) {
        switch (name) {
            case die: return die.play();
            case hit: return hit.play();
            case wing: return wing.play();
            case point: return point.play();
            case swoosh: return swoosh.play();
            default: throw new IllegalArgumentException("Unsupported name: " + name);
        }
    }

    public void dispose() {
        die.dispose();
        hit.dispose();
        point.dispose();
        swoosh.dispose();
        wing.dispose();
    }

}
