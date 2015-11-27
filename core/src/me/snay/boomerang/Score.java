package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Score {
    private Texture font;
    private float x;
    private float y;
    private int digits[];
    private TextureRegion glyphs[];
    private int score;

    private static final int GLYPH_WIDTH = 19;
    private static final int GLYPH_HEIGHT = 24;

    public Score(float x, float y, int digitCount) {
        font = new Texture("score.png");
        this.x = x;
        this.y = y;
        this.digits = new int[digitCount];
        glyphs = new TextureRegion[10];
        for (int i = 0; i < 10; i++) {
            glyphs[i] = new TextureRegion(font, GLYPH_WIDTH * i, 0, GLYPH_WIDTH, GLYPH_HEIGHT);
        }
    }

    public void set(int score) {
        this.score = score;
        for (int i = 0; i < digits.length; i++) {
            digits[i] = getNthDigit(score, i + 1);
        }
    }

    public void increase() {
        increase(1);
    }

    public void increase(int value) {
        set(score + value);
    }

    public void decrease() {
        decrease(1);
    }

    public void decrease(int value) {
        set(score - value);
    }

    public void draw(SpriteBatch batch) {
        for (int i = 0; i < digits.length; i++) {
            batch.draw(glyphs[digits[i]], x + ((digits.length - 1 - i) * GLYPH_WIDTH), y);
        }
    }

    private int getNthDigit(int number, int n) {
        return (int) ((number / Math.pow(10, n - 1)) % 10);
    }
}
