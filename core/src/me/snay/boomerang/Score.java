package me.snay.boomerang;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Score {
    private Texture font;
    private TextureRegion glyphs[];
    private Texture delimiter;
    private float x;
    private float y;
    private int digits[];
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
        delimiter = new Texture("horizontal-delimiter.png");
    }

    public void set(int score) {
        // TODO: Game over!
        if (score < 0) {
            score = 0;
        }
        this.score = score;
        for (int i = 0; i < digits.length; i++) {
            digits[i] = getNthDigit(i + 1);
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
            batch.draw(glyphs[digits[i]],
                    x + (i * GLYPH_WIDTH),
                    y,
                    glyphs[digits[i]].getRegionWidth() / 2,
                    glyphs[digits[i]].getRegionHeight() / 2,
                    glyphs[digits[i]].getRegionWidth(),
                    glyphs[digits[i]].getRegionHeight(),
                    1,
                    1,
                    180F);
            batch.draw(glyphs[digits[i]],
                    x + ((digits.length - 1 - i) * GLYPH_WIDTH),
                    y + GLYPH_HEIGHT + delimiter.getHeight() + 4);
            batch.draw(delimiter, x, y + GLYPH_HEIGHT + 2);
        }
    }

    private int getNthDigit(int n) {
        return (int)((score / Math.pow(10, n - 1)) % 10);
    }
}
