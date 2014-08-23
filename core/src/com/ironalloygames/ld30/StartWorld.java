package com.ironalloygames.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class StartWorld extends World {

	@Override
	public void renderBackground() {
		super.renderBackground();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

}
