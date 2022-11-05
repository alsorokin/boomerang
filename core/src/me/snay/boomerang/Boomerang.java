package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;

public abstract class Boomerang extends CircularObject {
    private float timeTravelledX;
    private float timeTravelledY;
    protected Field field;
    protected boolean isTossed;
    protected float rotation;

    protected static final float PI2 = (float) (Math.PI * 2D);

    public float getRotation() {
        return rotation;
    }

    public void toss() {
        isTossed = true;
    }

    public boolean isTossed() {
        return isTossed;
    }

    public float getTimeTravelledX() {
        return timeTravelledX;
    }

    public void setTimeTravelledX(float value) {
        timeTravelledX = value;
    }

    public float getTimeTravelledY() {
        return timeTravelledY;
    }

    public void setTimeTravelledY(float value) {
        timeTravelledY = value;
    }

    public Boomerang(Field field, Texture texture) {
        super(texture, 0.7F);
        this.field = field;
        this.isTossed = false;
        this.rotation = 0F;
    }

    public void move(float delta) {
        timeTravelledX += delta;
        if (timeTravelledX >= PI2) {
            timeTravelledX -= PI2;
        }
        setX(calculateX());
    }

    protected float calculateX() {
        return (float) Math.cos(timeTravelledX) * 222F + field.getHalfWidth();
    }

    protected float calculateY() {
        return (float) Math.sin(timeTravelledY) * 380F + field.getHalfHeight();
    }

    public int getHitScore() {
        return 1;
    }
}
