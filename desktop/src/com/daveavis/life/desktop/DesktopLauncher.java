package com.daveavis.life.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.daveavis.life.Life;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Life";
		//cfg.useGL20 = true;
		cfg.width = 500;
		cfg.height = 400;

		new LwjglApplication(new Life(), cfg);
	}
}
