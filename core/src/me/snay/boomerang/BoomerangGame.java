package me.snay.boomerang;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BoomerangGame extends ApplicationAdapter {
    final float FIELD_HEIGHT = 800;
    final float FIELD_WIDTH = 480;

    private SpriteBatch batch;
    private Boomerang player1;
    private Boomerang player2;
    private Boomerang player1Tracer;
    private Boomerang player2Tracer;
    private Score player1Score;
    private Score player2Score;
    private Bonus[] bonuses = new Bonus[10];
    private Field field;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture background;
    private TouchRegister[] touched = new TouchRegister[20];
    private ShapeRenderer shapeRenderer;
    private Boomerang tracer;
    private Boomerang boomerang;
    private float traceRadius;

    private class TouchRegister {
        public boolean isTouched = false;
        public boolean isTop = false;
        public long timestamp;
    }

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, FIELD_WIDTH, FIELD_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        field = new Field(FIELD_WIDTH, FIELD_HEIGHT);
        background = new Texture("background.png");
        player1 = new Boomerang(field, BoomerangOrientation.BOTTOM);
        player1Tracer = new Boomerang(field, BoomerangOrientation.BOTTOM);
        player2 = new Boomerang(field, BoomerangOrientation.TOP);
        player2Tracer = new Boomerang(field, BoomerangOrientation.TOP);
        player1Score = new Score(20, 44, 3);
        player2Score = new Score(FIELD_WIDTH - 77, FIELD_HEIGHT - 68, 3);
        bonuses[0] = new Bonus(BonusType.PIGEON, field);

        viewport = new FitViewport(FIELD_WIDTH, FIELD_HEIGHT, camera);

        for (int i = 0; i < 20; i++) {
            touched[i] = new TouchRegister();
        }
    }

    @Override
    public void render() {
        // Logical stuff
        player1.move(Gdx.graphics.getDeltaTime());
        player2.move(Gdx.graphics.getDeltaTime());
        checkCollisions();

        // Graphical stuff
        //Gdx.gl.glClearColor(0F, 0F, 0F, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0F, 0F, field.getWidth(), field.getHeight());
        batch.draw(
                player1.getTexture(),
                player1.getTextureX(),
                player1.getTextureY(),
                player2.getSize() / 2,
                player2.getSize() / 2,
                player1.getSize(),
                player1.getSize(),
                1,
                1,
                player1.getRotation(),
                0,
                0,
                player1.getTexture().getWidth(),
                player1.getTexture().getHeight(),
                false,
                false
        );
        batch.draw(
                player2.getTexture(),
                player2.getTextureX(),
                player2.getTextureY(),
                player2.getSize() / 2,
                player2.getSize() / 2,
                player2.getSize(),
                player2.getSize(),
                1,
                1,
                player2.getRotation(),
                0,
                0,
                player2.getTexture().getWidth(),
                player2.getTexture().getHeight(),
                false,
                false
        );
        for (Bonus bonus : bonuses) {
            if (bonus != null) {
                bonus.draw(batch);
            }
        }
        player1Score.draw(batch);
        player2Score.draw(batch);
        batch.end();

        // Input stuff
        for (int i = 0; i < 20; i++) {
            if (Gdx.input.isTouched(i)) {
                if (!touched[i].isTouched) {
                    touched[i].isTouched = true;
                    touched[i].timestamp = TimeUtils.millis();
                    Vector2 newPoints = new Vector2(Gdx.input.getX(i), Gdx.input.getY(i));
                    newPoints = viewport.unproject(newPoints);
                    if (newPoints.y > field.getHalfHeight()) {
                        touched[i].isTop = true;
                    } else {
                        touched[i].isTop = false;
                    }
                }
                if (TimeUtils.millis() > touched[i].timestamp + 500) {
                    if (!touched[i].isTop) {
                        boomerang = player1;
                        tracer = player1Tracer;
                        shapeRenderer.setColor(Color.YELLOW);
                    } else {
                        boomerang = player2;
                        tracer = player2Tracer;
                        shapeRenderer.setColor(Color.RED);
                    }
                    tracer.setPosition(boomerang.getX(), boomerang.getY());
                    tracer.setTTX(boomerang.getTTX());
                    tracer.setTTY(boomerang.getTTY());

                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    traceRadius = 5F;
                    tracer.toss();
                    for (int j = 0; j < 60; j++) {
                        tracer.move(0.05F);
                        if (traceRadius > 0 && tracer.isTossed()) {
                            shapeRenderer.circle(tracer.getX(), tracer.getY(), traceRadius);
                        }
                        traceRadius -= 0.1F;
                    }
                    shapeRenderer.end();
                }
            } else if (touched[i].isTouched) {
                touched[i].isTouched = false;
                if (touched[i].isTop) {
                    player2.toss();
                } else {
                    player1.toss();
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    protected void checkCollisions() {
        for (Bonus bonus : bonuses) {
            if (bonus == null) continue;
            if (bonus.getHitbox().overlaps(player1.getHitbox())) {
                bonus.relocate();
                player1Score.increase(player1.getHitScore());
            } else if (bonus.getHitbox().overlaps(player2.getHitbox())) {
                bonus.relocate();
                player2Score.increase(player2.getHitScore());
            }
        }
    }

    @Override
    public void dispose() {
        player1.dispose();
        player2.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        //field.dispose();
        background.dispose();
        for (Bonus bonus : bonuses) {
            if (bonus != null) bonus.dispose();
        }
    }
}
