package me.snay.boomerang;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

enum BoomerangOrientation {
    STRAIGHT,
    GAY
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
    private static final float TOP_STARTING_POINT_Y = (float)(0 - Math.PI/2);
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
        texture = new Texture("boomerang.png");
        this.size = texture.getWidth() / 2;
        this.field = field;
        this.isTossed = false;

        switch (orientation) {
            case STRAIGHT:
                this.x = this.size / 2;
                this.y = this.size / 2;
                this.timeTravelledX = BOTTOM_STARTING_POINT_X;
                this.timeTravelledY = BOTTOM_STARTING_POINT_Y;
                break;
            case GAY:
                this.x = field.getWidth() - (this.size / 2);
                this.y = field.getHeight() - (this.size / 2);
                this.timeTravelledX = TOP_STARTING_POINT_X;
                this.timeTravelledY = TOP_STARTING_POINT_Y;
                break;
        }
    }

    public void Move() {
        this.timeTravelledX += Gdx.graphics.getDeltaTime();
        if (this.timeTravelledX >= PI2) {
            this.timeTravelledX -= PI2;
        }
        this.x = (float)Math.cos(timeTravelledX) * 222F + (this.field.getWidth() / 2);

        if (!this.isTossed) return; // Move by Y axis only if is tossed

        this.timeTravelledY += Gdx.graphics.getDeltaTime();

        if (this.orientation == BoomerangOrientation.STRAIGHT
                && this.timeTravelledY >= BOTTOM_STARTING_POINT_Y + PI2) {
            this.timeTravelledY = BOTTOM_STARTING_POINT_Y;
            this.isTossed = false;
        } else if (this.orientation == BoomerangOrientation.GAY
                && this.timeTravelledY >= TOP_STARTING_POINT_Y + PI2) {
            this.timeTravelledY = TOP_STARTING_POINT_Y;
            this.isTossed = false;
        }
        this.y = (float)Math.sin(timeTravelledY) * 380F + this.field.getHeight() / 2;

        this.rotation -= 7F;
    }
}
