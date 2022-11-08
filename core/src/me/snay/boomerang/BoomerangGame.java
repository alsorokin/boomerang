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
    // Constants
    private static final float FIELD_HEIGHT = 800;
    private static final float FIELD_WIDTH = 480;

    // Properties
    private static boolean drawHitboxes = false;

    // Game objects etc.
    private SpriteBatch batch;
    private BottomBoomerang player1;
    private TopBoomerang player2;
    private BottomBoomerang tracer1;
    private TopBoomerang tracer2;
    private Score score1;
    private Score score2;
    private final Bonus[] bonuses = new Bonus[10];
    private final Enemy[] enemies = new Enemy[20];
    private Field field;
    private Viewport viewport;
    private Texture background;
    private final TouchRegister[] touched = new TouchRegister[20];
    private ShapeRenderer shapeRenderer;

    // AI
    private Brain antiBrain;

    private class TouchRegister {
        public boolean isTouched = false;
        public boolean isTop = false;
        public long timestamp;
    }

    @Override
    public void create() {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, FIELD_WIDTH, FIELD_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        field = new Field(FIELD_WIDTH, FIELD_HEIGHT);
        background = new Texture("background.png");
        player1 = new BottomBoomerang(field);
        tracer1 = new BottomBoomerang(field);
        player2 = new TopBoomerang(field);
        tracer2 = new TopBoomerang(field);
        score1 = new Score(20, 44, 3);
        score2 = new Score(FIELD_WIDTH - 77, FIELD_HEIGHT - 68, 3);
        for (int i = 0; i < 1; i++) {
            bonuses[i] = new Bonus(BonusType.PIGEON, field);
        }
        for (int i = 0; i < 1; i++) {
            enemies[i] = new Enemy(field);
        }

        viewport = new FitViewport(FIELD_WIDTH, FIELD_HEIGHT, camera);

        for (int i = 0; i < 20; i++) {
            touched[i] = new TouchRegister();
        }

        antiBrain = new Brain(player2, field, bonuses, enemies);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        // Logical stuff
        player1.move(delta);
        player2.move(delta);
        for (Enemy enemy : enemies) {
            if (enemy != null) {
                enemy.move(delta);
            }
        }
        checkCollisions();

        // AI stuff
        if (antiBrain != null)
        {
            antiBrain.think(delta);
        }

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
        for (Enemy enemy : enemies) {
            if (enemy != null) {
                enemy.draw(batch);
            }
        }
        score1.draw(batch);
        score2.draw(batch);
        batch.end();

        if (drawHitboxes) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GOLD);
            shapeRenderer.circle(player1.getHitbox().x, player1.getHitbox().y, player1.getHitbox().radius);
            shapeRenderer.circle(player2.getHitbox().x, player2.getHitbox().y, player1.getHitbox().radius);
            shapeRenderer.setColor(Color.BLUE);
            for (Bonus bonus : bonuses) {
                if (bonus != null) {
                    shapeRenderer.circle(bonus.hitbox.x, bonus.hitbox.y, bonus.hitbox.radius);
                }
            }
            shapeRenderer.setColor(Color.RED);
            for (Enemy enemy : enemies) {
                if (enemy != null) {
                    shapeRenderer.circle(enemy.hitbox.x, enemy.hitbox.y, enemy.hitbox.radius);
                }
            }
            shapeRenderer.end();
        }

        // Input and trajectory predictions
        for (int i = 0; i < 6; i++) {
            if (Gdx.input.isTouched(i)) {
                if (!touched[i].isTouched) {
                    touched[i].isTouched = true;
                    touched[i].timestamp = TimeUtils.millis();
                    Vector2 newPoints = new Vector2(Gdx.input.getX(i), Gdx.input.getY(i));
                    newPoints = viewport.unproject(newPoints);
                    touched[i].isTop = newPoints.y > field.getHalfHeight();
                }
                if (TimeUtils.millis() > touched[i].timestamp + 500) {
                    Boomerang tracer;
                    Boomerang boomerang;
                    if (!touched[i].isTop) {
                        boomerang = player1;
                        tracer = tracer1;
                        shapeRenderer.setColor(Color.YELLOW);
                    } else {
                        boomerang = player2;
                        tracer = tracer2;
                        shapeRenderer.setColor(Color.RED);
                    }
                    tracer.setPosition(boomerang.getX(), boomerang.getY());
                    tracer.setTimeTravelledX(boomerang.getTimeTravelledX());
                    tracer.setTimeTravelledY(boomerang.getTimeTravelledY());

                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    float traceRadius = 5F;
                    tracer.toss();
                    tracerLoop:
                    for (int j = 0; j < (75 / boomerang.getVelocity()); j++) {
                        tracer.move(0.04F);
                        for (Enemy enemy : enemies) {
                            if (enemy != null && enemy.getHitbox().overlaps(tracer.hitbox)) {
                                Vector2 bottomLeft = new Vector2(
                                        tracer.getX() - 15,
                                        tracer.getY() - 15
                                );
                                Vector2 topLeft = new Vector2(
                                        tracer.getX() - 15,
                                        tracer.getY() + 15
                                );
                                Vector2 topRight = new Vector2(
                                        tracer.getX() + 15,
                                        tracer.getY() + 15
                                );
                                Vector2 bottomRight = new Vector2(
                                        tracer.getX() + 15,
                                        tracer.getY() - 15
                                );
                                shapeRenderer.setColor(Color.BLACK);
                                shapeRenderer.rectLine(bottomLeft, topRight, 3);
                                shapeRenderer.rectLine(topLeft, bottomRight, 3);
                                break tracerLoop;
                            }
                        }
                        if (traceRadius > 0 && tracer.isTossed()) {
                            shapeRenderer.circle(tracer.getX(), tracer.getY(), traceRadius);
                        }
                        traceRadius -= 0.07F * boomerang.getVelocity();
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
                score1.increase(player1.getHitScore());
            } else if (bonus.getHitbox().overlaps(player2.getHitbox())) {
                bonus.relocate();
                score2.increase(player2.getHitScore());
            }
        }
        for (Enemy enemy : enemies) {
            if (enemy == null) continue;
            if (enemy.getHitbox().overlaps(player1.getHitbox())) {
                enemy.randomizeImpulse();
                player1.resetY();
                score1.decrease(5);
            } else if (enemy.getHitbox().overlaps(player2.getHitbox())) {
                enemy.randomizeImpulse();
                player2.resetY();
                score2.decrease(5);
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
