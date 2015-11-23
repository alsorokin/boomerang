package me.snay.boomerang;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BoomerangGame extends ApplicationAdapter {
    final float FIELD_HEIGHT = 800;
    final float FIELD_WIDTH = 480;

    SpriteBatch batch;
    Boomerang player1;
    Boomerang player2;
    private Field field;
    private OrthographicCamera camera;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, FIELD_WIDTH, FIELD_HEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        field = new Field(FIELD_WIDTH, FIELD_HEIGHT);
        player1 = new Boomerang(field, BoomerangOrientation.STRAIGHT);
        player2 = new Boomerang(field, BoomerangOrientation.GAY);
    }

    @Override
    public void render() {
        // Logical stuff
        player1.Move();
        player2.Move();

        // Graphical stuff
        Gdx.gl.glClearColor(0.1F, 0.3F, 0.5F, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(player1.getTexture(),
                   player1.getTextureX(),
                   player1.getTextureY(),
                   16,
                   16,
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
        batch.draw(player2.getTexture(), player2.getTextureX(), player2.getTextureY(),
                player2.getSize(), player2.getSize());
        batch.end();

        // Input
        if(Gdx.input.isTouched()) {
            player1.toss();
            //player2.toss();
        }
    }
}
