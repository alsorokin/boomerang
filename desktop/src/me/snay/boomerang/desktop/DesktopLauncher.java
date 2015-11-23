package me.snay.boomerang.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import me.snay.boomerang.BoomerangGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 800;
        config.width = 480;
        config.title = "Boomerang";
        config.resizable = false;
        new LwjglApplication(new BoomerangGame(), config);
    }
}
