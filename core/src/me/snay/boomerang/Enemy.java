package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Enemy extends CircularObject {
    private Random random = new Random();
    private Field field;
    private Vector2 impulse;
    private float topBorder;
    private float bottomBorder;
    private float leftBorder;
    private float rightBorder;
    private float impulseRange = 50F;

    private float getHorizontalThreshold() {
        return rightBorder - leftBorder;
    }

    private float getVerticalThreshold() {
        return topBorder - bottomBorder;
    }

    public Enemy(Field field) {
        super(new Texture("enemy.png"), 0.75F);
        this.field = field;
        this.topBorder = field.getHeight() * 0.9F;
        this.bottomBorder = field.getHeight() * 0.1F;
        this.leftBorder = getWidth() / 2;
        this.rightBorder = field.getWidth() - (getWidth() / 2);

        randomizeImpulse();
        relocate();
    }

    public void randomizeImpulse() {
        float randomImpulseX = (impulseRange / 2) - (random.nextFloat() * impulseRange);
        float randomImpulseY = (impulseRange / 2) - (random.nextFloat() * impulseRange);
        this.impulse = new Vector2(randomImpulseX, randomImpulseY);
    }

    public void relocate() {
        float x = (random.nextFloat() * getHorizontalThreshold()) + leftBorder;
        float y = (random.nextFloat() * getVerticalThreshold()) + bottomBorder;
        setPosition(x, y);
    }

    public void move(float delta) {
        setPosition(getX() + (impulse.x * delta), getY() + (impulse.y * delta));
        if (getX() < leftBorder) {
            impulse.x = -impulse.x;
            setX(leftBorder);
        } else if (getX() > rightBorder) {
            impulse.x = -impulse.x;
            setX(rightBorder);
        } else if (getY() < bottomBorder) {
            impulse.y = -impulse.y;
            setY(bottomBorder);
        } else if (getY() > topBorder) {
            impulse.y = -impulse.y;
            setY(topBorder);
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, getTextureX(), getTextureY(), getWidth(), getHeight());
    }
}
