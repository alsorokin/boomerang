package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;

public class GameObject {
    private float x;
    private float y;
    protected float width;
    protected float height;
    protected Texture texture;

    public GameObject(Texture texture) {
        this.texture = texture;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        if (this.texture != null) {
            this.texture.dispose();
        }
        this.texture = texture;
    }

    public float getTextureX() {
        return this.x - (this.getSize() / 2);
    }

    public float getTextureY() {
        return this.y - (this.getSize() / 2);
    }

    public float getSize() {
        return (width + height) / 2;
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

    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setScale(float scale) {
        width = texture.getWidth() * scale;
        height = texture.getHeight() * scale;
    }

    public void setSize(float size) {
        width = size;
        height = size;
    }

    public void dispose() {
        texture.dispose();
    }
}
