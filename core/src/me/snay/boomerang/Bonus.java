package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Bonus extends CircularObject {
    private Random random = new Random();
    private Field field;
    private GameObject preview;

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
