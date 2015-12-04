package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Enemy extends CircularObject{
    private Random random = new Random();
    private Field field;

    public Enemy(Field field) {
        super(new Texture("enemy.png"), 0.75F);
        this.field = field;
        relocate();
    }

    public void relocate() {
        setPosition(
                random.nextFloat() * field.getWidth(),
                // Leave the top and bottom 10% of the game field enemy-free
                (random.nextFloat() * (field.getHeight() * 0.8F)) + field.getHeight() * 0.1F
        );
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, getTextureX(), getTextureY(), getWidth(), getHeight());
    }
}
