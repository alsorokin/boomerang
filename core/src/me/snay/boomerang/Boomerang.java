package me.snay.boomerang;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

enum BoomerangOrientation {
    BOTTOM,
    TOP
}

public class Boomerang {
    private float x;
    private float y;
    private float timeTravelledX;
    private float timeTravelledY;
    private float size;
    private BoomerangOrientation orientation;
    private Texture texture;
    private Field field;
    private boolean isTossed;
    private float rotation;

    private static final float BOTTOM_STARTING_POINT_Y = (float)(0 - Math.PI / 2);
    private static final float TOP_STARTING_POINT_Y = (float)(Math.PI/2);
    private static final float BOTTOM_STARTING_POINT_X = 0;
    private static final float TOP_STARTING_POINT_X = (float)Math.PI;
    private static final float PI2 = (float)(Math.PI * 2);

    public Texture getTexture() {
        return texture;
    }

    public float getTextureX() {
        return this.x - (this.size / 2);
    }

    public float getTextureY() {
        return this.y - (this.size / 2);
    }

    public float getSize() {
        return size;
    }

    public float getRotation() {
        return rotation;
    }

    public void toss() {
        isTossed = true;
    }

    public Boomerang(Field field, BoomerangOrientation orientation) {
        this.orientation = orientation;
        this.texture = new Texture("boomerang.png");
        this.size = texture.getWidth() / 2;
        this.field = field;
        this.isTossed = false;
        this.rotation = 0F;

        switch (orientation) {
            case BOTTOM:
                this.x = this.size / 2;
                this.y = this.size / 2;
                this.timeTravelledX = BOTTOM_STARTING_POINT_X;
                this.timeTravelledY = BOTTOM_STARTING_POINT_Y;
                break;
            case TOP:
                this.x = field.getWidth() - (this.size / 2);
                this.y = field.getHeight() - (this.size / 2);
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
        x = (float)Math.cos(timeTravelledX) * 222F + field.getHalfWidth();

        if (!isTossed) return; // Move by Y axis only if is tossed

        timeTravelledY += Gdx.graphics.getDeltaTime();

        if (orientation == BoomerangOrientation.BOTTOM
                && timeTravelledY >= BOTTOM_STARTING_POINT_Y + PI2) {
            timeTravelledY = BOTTOM_STARTING_POINT_Y;
            isTossed = false;
            rotation = 0F;
        } else if (orientation == BoomerangOrientation.TOP
                && timeTravelledY >= TOP_STARTING_POINT_Y + PI2) {
            timeTravelledY = TOP_STARTING_POINT_Y;
            isTossed = false;
            rotation = 0F;
        }
        y = (float)Math.sin(timeTravelledY) * 380F + field.getHalfHeight();

        rotation -= 7F;
    }
}
