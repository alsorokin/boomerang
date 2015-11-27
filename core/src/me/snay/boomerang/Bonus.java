package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

import java.util.Random;

public class Bonus extends GameObject{
    private Circle hitbox;
    private Random random = new Random();
    private Field field;

    public Circle getHitbox() {
        return hitbox;
    }

    @Override
    public void setX(float value) {
        super.setX(value);
        this.hitbox.setX(value);
    }

    @Override
    public void setY(float value) {
        super.setY(value);
        this.hitbox.setY(value);
    }

    public Bonus(BonusType type, float x, float y, Field field) {
        switch (type) {
            case PIGEON:
                this.texture = new Texture("bonus.png");
        }
        this.setScale(0.5F);
        this.hitbox = new Circle(x, y, this.getSize() / 2);
        setX(x);
        setY(y);
        this.field = field;
    }

    public void relocate() {
        setX(random.nextFloat() * field.getWidth());
        setY(random.nextFloat() * field.getHeight());
    }
}
