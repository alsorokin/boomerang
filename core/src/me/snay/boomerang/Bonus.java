package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

import java.util.Random;

public class Bonus extends GameObject {
    private Circle hitbox;
    private Random random = new Random();
    private Field field;
    private GameObject preview;

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

    @Override
    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    public Bonus(BonusType type, Field field) {
        super(new Texture("bonus.png"));
        switch (type) {
            case PIGEON:
                this.preview = new GameObject(new Texture("bonus-preview.png"));
                this.preview.setPosition(
                        random.nextFloat() * field.getWidth(),
                        random.nextFloat() * field.getHeight()
                );
                this.setScale(0.5F);
                this.preview.setScale(0.25F);
        }

        this.field = field;
        this.hitbox = new Circle(0, 0, this.getSize() / 2);
        relocate();
    }

    public void relocate() {
        setPosition(preview.getX(), preview.getY());
        preview.setPosition(
                random.nextFloat() * field.getWidth(),
                random.nextFloat() * field.getHeight()
        );
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                texture,
                getTextureX(),
                getTextureY(),
                getSize(),
                getSize()
        );
        batch.draw(
                preview.getTexture(),
                preview.getTextureX(),
                preview.getTextureY(),
                preview.getSize(),
                preview.getSize()
        );
    }
}
