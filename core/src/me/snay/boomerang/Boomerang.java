package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;

enum BoomerangOrientation {
    BOTTOM,
    TOP
}

public class Boomerang extends CircularObject {
    private float timeTravelledX;
    private float timeTravelledY;
    private BoomerangOrientation orientation;
    private Field field;
    private boolean isTossed;
    private float rotation;

    private static final float BOTTOM_STARTING_POINT_Y = (float) (0F - Math.PI / 2D);
    private static final float TOP_STARTING_POINT_Y = (float) (Math.PI / 2D);
    private static final float BOTTOM_STARTING_POINT_X = (float) Math.PI;
    private static final float TOP_STARTING_POINT_X = 0F;
    private static final float PI2 = (float) (Math.PI * 2D);

    public float getRotation() {
        return rotation;
    }

    public void toss() {
        isTossed = true;
    }

    public boolean isTossed() {
        return isTossed;
    }

    public float getTTX() {
        return timeTravelledX;
    }

    public void setTTX(float value) {
        timeTravelledX = value;
    }

    public float getTTY() {
        return timeTravelledY;
    }

    public void setTTY(float value) {
        timeTravelledY = value;
    }

    public Boomerang(Field field, BoomerangOrientation orientation) {
        super(
                orientation == BoomerangOrientation.BOTTOM ?
                        new Texture("boomerang-yellow.png") :
                        new Texture("boomerang-red.png"),
                0.7F
        );
        this.orientation = orientation;
        this.field = field;
        this.isTossed = false;
        this.rotation = 0F;

        switch (orientation) {
            case BOTTOM:
                setX(this.getWidth() / 2);
                setY(this.getHeight() / 2);
                this.timeTravelledX = BOTTOM_STARTING_POINT_X;
                this.timeTravelledY = BOTTOM_STARTING_POINT_Y;
                break;
            case TOP:
                setX(field.getWidth() - (this.getWidth() / 2));
                setY(field.getHeight() - (this.getHeight() / 2));
                this.timeTravelledX = TOP_STARTING_POINT_X;
                this.timeTravelledY = TOP_STARTING_POINT_Y;
                break;
        }
    }

    public void move(float delta) {
        timeTravelledX += delta;
        if (timeTravelledX >= PI2) {
            timeTravelledX -= PI2;
        }
        setX(calculateX());

        if (!isTossed) return; // move by Y axis only if is tossed

        timeTravelledY += delta;

        if (orientation == BoomerangOrientation.BOTTOM
                && timeTravelledY >= BOTTOM_STARTING_POINT_Y + PI2) {
            timeTravelledY = BOTTOM_STARTING_POINT_Y;
            isTossed = false;
        } else if (orientation == BoomerangOrientation.TOP
                && timeTravelledY >= TOP_STARTING_POINT_Y + PI2) {
            timeTravelledY = TOP_STARTING_POINT_Y;
            isTossed = false;
        }
        setY(calculateY());

        rotation -= 7F;
    }

    private float calculateX() {
        return (float) Math.cos(timeTravelledX) * 222F + field.getHalfWidth();
    }

    private float calculateY() {
        return (float) Math.sin(timeTravelledY) * 380F + field.getHalfHeight();
    }

    public void resetY() {
        switch(orientation) {
            case TOP:
                setTTY(TOP_STARTING_POINT_Y);
                setY(calculateY());
                isTossed = false;
                break;
            case BOTTOM:
                setTTY(BOTTOM_STARTING_POINT_Y);
                setY(calculateY());
                isTossed = false;
                break;
        }
    }

    public int getHitScore() {
        switch (orientation) {
            case BOTTOM:
                return 1 + Math.abs((int) Math.floor(timeTravelledY - BOTTOM_STARTING_POINT_Y));
            case TOP:
                return 1 + Math.abs((int) Math.floor(timeTravelledY - TOP_STARTING_POINT_Y));
            default:
                return 0;
        }
    }
}
