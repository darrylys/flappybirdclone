package id.dys.test.flappydemo.sprites.bird;

import java.util.Random;

public class BirdSpriteWrapper {

    private final BirdSprite[] sprites;
    private final Random random = new Random();

    public BirdSpriteWrapper() {
        sprites = new BirdSprite[3];
        sprites[0] = new BirdSprite("bluebird");
        sprites[1] = new BirdSprite("redbird");
        sprites[2] = new BirdSprite("yellowbird");
    }

    public BirdSprite get(int i) {
        return sprites[i];
    }

    public int getRandomIndex() {
        return random.nextInt(sprites.length);
    }

    public void dispose() {
        for (BirdSprite bs : sprites) {
            bs.dispose();
        }
    }

}
