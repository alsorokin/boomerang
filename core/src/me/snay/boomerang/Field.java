package me.snay.boomerang;

public class Field {
    private float width;
    private float height;
    private float halfHeight; // these are needed to use as shorthand and
    private float halfWidth;  // for faster calculations

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public float getHalfHeight() {
        return halfHeight;
    }

    public float getHalfWidth() {
        return halfWidth;
    }

    public Field(float width, float height) {
        this.width = width;
        this.halfWidth = width / 2;
        this.height = height;
        this.halfHeight = height / 2;
    }

}
