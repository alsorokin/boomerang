package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;

public class GameObject {
    private float x;
    private float y;
    protected float size;
    protected Texture texture;

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

    public float getX() {
        return x;
    }

    public void setX(float value) {
        x = value;
    }

    public float getY() {
        return y;
    }

    public void setY(float value) {
        y = value;
    }
}
