package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

enum BoomerangOrientation {
    BOTTOM,
    TOP
}

public class Boomerang extends GameObject {
    private float timeTravelledX;
    private float timeTravelledY;
    private BoomerangOrientation orientation;
    private Field field;
    private boolean isTossed;
    private float rotation;
    private Circle hitbox;

    private static final float BOTTOM_STARTING_POINT_Y = (float)(0F - Math.PI / 2D);
    private static final float TOP_STARTING_POINT_Y = (float)(Math.PI / 2D);
    private static final float BOTTOM_STARTING_POINT_X = (float)Math.PI;
    private static final float TOP_STARTING_POINT_X = 0F;
    private static final float PI2 = (float)(Math.PI * 2D);

    public float getRotation() {
        return rotation;
    }

    public Circle getHitbox() {
        return hitbox;
    }

    public void toss() {
        isTossed = true;
    }

    public boolean isTossed() {
        return isTossed;
    }

    @Override
    public void setX(float value) {
        super.setX(value);
        hitbox.setX(value);
    }

    @Override
    public void setY(float value) {
        super.setY(value);
        hitbox.setY(value);
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
                new Texture("boomerang-red.png")
        );
        this.orientation = orientation;
        this.setScale(0.7F);
        this.field = field;
        this.isTossed = false;
        this.rotation = 0F;
        this.hitbox = new Circle(0, 0, this.getSize() / 2); // will be aligned automatically by setX() and setY()

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
        setX((float) Math.cos(timeTravelledX) * 222F + field.getHalfWidth());

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
        setY((float) Math.sin(timeTravelledY) * 380F + field.getHalfHeight());

        rotation -= 7F;
    }

    public int getHitScore() {
        switch (orientation) {
            case BOTTOM:
                return 1 + Math.abs((int)Math.floor(timeTravelledY - BOTTOM_STARTING_POINT_Y));
            case TOP:
                return 1 + Math.abs((int)Math.floor(timeTravelledY - TOP_STARTING_POINT_Y));
            default:
                return 0;
        }
    }
}
