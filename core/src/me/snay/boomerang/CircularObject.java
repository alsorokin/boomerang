package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

public class CircularObject extends GameObject{
    protected Circle hitbox;

    public CircularObject(Texture texture) {
        super(texture);
        this.hitbox = new Circle(0, 0, this.getSize() / 2);
    }

    public CircularObject(Texture texture, float scale) {
        this(texture);
        this.setScale(scale);
    }

    public Circle getHitbox() {
        return hitbox;
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        hitbox.setX(super.getX());
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        hitbox.setY(super.getY());
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        hitbox.setPosition(x, y);
    }

    @Override
    public void setScale(float scale) {
        super.setScale(scale);
        hitbox.setRadius((this.texture.getWidth() / 2) * scale);
    }
}
