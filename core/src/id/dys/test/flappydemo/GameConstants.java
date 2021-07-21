package id.dys.test.flappydemo;

public final class GameConstants {

    public static final float SCREEN_W = 288, SCREEN_H = 512 + 112;
    public static final float BIRD_W = 20, BIRD_H = 20;
    public static final float PIPE_W = 52, PIPE_H = 320;
    public static final float SCORE_Y = 500;

    public static final float BIRD_INC_SPEED_Y = 150.0f, BIRD_GRAVITY = 7.0f;
    public static final float PIPE_SPEED_X = 50;

    public static final long SPAWN_TIME_GAP_IN_NANOS = 5000000000L;

    private GameConstants() {

    }

}
