package me.snay.boomerang;

import com.badlogic.gdx.Gdx;
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

    private static final float BOTTOM_STARTING_POINT_Y = (float)(0 - Math.PI / 2);
    private static final float TOP_STARTING_POINT_Y = (float)(Math.PI/2);
    private static final float BOTTOM_STARTING_POINT_X = 0;
    private static final float TOP_STARTING_POINT_X = (float)Math.PI;
    private static final float PI2 = (float)(Math.PI * 2);

    public float getRotation() {
        return rotation;
    }

    public Circle getHitbox() {
        return hitbox;
    }

    public void toss() {
        isTossed = true;
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

    public Boomerang(Field field, BoomerangOrientation orientation) {
        this.orientation = orientation;
        this.texture = new Texture("boomerang.png");
        this.size = texture.getWidth() / 2;
        this.field = field;
        this.isTossed = false;
        this.rotation = 0F;
        this.hitbox = new Circle(0, 0, this.size / 2); // will be aligned automatically by setX() and setY()

        switch (orientation) {
            case BOTTOM:
                setX(this.size / 2);
                setY(this.size / 2);
                this.timeTravelledX = BOTTOM_STARTING_POINT_X;
                this.timeTravelledY = BOTTOM_STARTING_POINT_Y;
                break;
            case TOP:
                setX(field.getWidth() - (this.size / 2));
                setY(field.getHeight() - (this.size / 2));
                this.timeTravelledX = TOP_STARTING_POINT_X;
                this.timeTravelledY = TOP_STARTING_POINT_Y;
                break;
        }
    }

    public void Move() {
        timeTravelledX += Gdx.graphics.getDeltaTime();
        if (timeTravelledX >= PI2) {
            timeTravelledX -= PI2;
        }
        setX((float) Math.cos(timeTravelledX) * 222F + field.getHalfWidth());

        if (!isTossed) return; // Move by Y axis only if is tossed

        timeTravelledY += Gdx.graphics.getDeltaTime();

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
}
