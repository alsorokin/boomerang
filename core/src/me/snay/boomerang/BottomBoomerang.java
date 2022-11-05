package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;

public class BottomBoomerang extends Boomerang {
    private static final float STARTING_POINT_Y = (float) (0F - Math.PI / 2D);
    private static final float STARTING_POINT_X = (float) Math.PI;

    public BottomBoomerang(Field field) {
        super(field, new Texture("boomerang-blue.png"));
        setX(this.getWidth() / 2);
        setY(this.getHeight() / 2);
        setTimeTravelledX(STARTING_POINT_X);
        setTimeTravelledY(STARTING_POINT_Y);
    }

    @Override
    public void move(float delta) {
        super.move(delta);

        if (!isTossed) return; // move by Y axis only if is tossed

        if (getTimeTravelledY() >= STARTING_POINT_Y + PI2) {
            setTimeTravelledY(STARTING_POINT_Y);
            isTossed = false;
        }
        setTimeTravelledY(getTimeTravelledY() + delta);
        rotation -= 7F;

        setY(calculateY());
    }

    public void resetY() {
        setTimeTravelledY(STARTING_POINT_Y);
        setY(calculateY());
        isTossed = false;
    }

    @Override
    public int getHitScore() {
        return 1 + Math.abs((int) Math.floor(getTimeTravelledY() - STARTING_POINT_Y));
    }
}
