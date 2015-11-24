package me.snay.boomerang;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BoomerangGame extends ApplicationAdapter {
    final float FIELD_HEIGHT = 800;
    final float FIELD_WIDTH = 480;

    SpriteBatch batch;
    Boomerang player1;
    Boomerang player2;
    private Field field;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture background;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, FIELD_WIDTH, FIELD_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        field = new Field(FIELD_WIDTH, FIELD_HEIGHT);
        player1 = new Boomerang(field, BoomerangOrientation.BOTTOM);
        player2 = new Boomerang(field, BoomerangOrientation.TOP);
        background = new Texture("background.png");

        viewport = new FitViewport(FIELD_WIDTH, FIELD_HEIGHT, camera);
    }

    @Override
    public void render() {
        // Logical stuff
        player1.Move();
        player2.Move();

        // Graphical stuff
        //Gdx.gl.glClearColor(0F, 0F, 0F, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0F, 0F, field.getWidth(), field.getHeight());
        batch.draw(player1.getTexture(),
                player1.getTextureX(),
                player1.getTextureY(),
                0,
                player1.getSize() / 1.75F,
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
                false);
        batch.draw(player2.getTexture(),
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
                false);
        batch.end();

        // Input
        for (int i = 0; i < 20; i++) {
            if (Gdx.input.isTouched(i)) {
                float x = Gdx.input.getX(i);
                float y = Gdx.input.getY(i);
                Vector2 newPoints = new Vector2(x, y);
                newPoints = viewport.unproject(newPoints);
                if (newPoints.y > field.getHalfHeight()) {
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
}
