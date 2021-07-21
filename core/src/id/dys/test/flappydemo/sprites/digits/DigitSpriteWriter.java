package id.dys.test.flappydemo.sprites.digits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import id.dys.test.flappydemo.GameConstants;

public class DigitSpriteWriter {

    private final Texture[] digits = new Texture[10];
    private static final float MARGIN_RIGHT = 5.0f;

    public DigitSpriteWriter() {
        for (int i = 0; i < digits.length; ++i) {
            digits[i] = new Texture(Gdx.files.internal("d" + i + ".png"));
        }
    }

    private float getWidthOfStr(String strDigits) {
        float width = 0;
        for (int i = 0; i < strDigits.length(); ++i) {
            int digit = strDigits.charAt(i) - '0';
            width += digits[digit].getWidth();

            if (i < strDigits.length() - 1) {
                width += MARGIN_RIGHT;
            }
        }
        return width;
    }

    public void drawCenteredAtHeight(SpriteBatch batch, String strDigits, float y) {
        float width = getWidthOfStr(strDigits);

        float x = GameConstants.SCREEN_W / 2.0f - width / 2.0f;

        for (int i = 0; i < strDigits.length(); ++i) {
            int d = strDigits.charAt(i) - '0';

            batch.draw(digits[d], x, y);

            x += digits[d].getWidth() + MARGIN_RIGHT;
        }

    }

    public void dispose() {
        for (Texture t : digits) {
            t.dispose();
        }
    }

}
