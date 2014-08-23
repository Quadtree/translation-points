package com.ironalloygames.ld30;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LD30 extends ApplicationAdapter {
	public static Assets a;
	public static SpriteBatch batch;

	Texture img;

	float r = 0;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		a = new Assets();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(a.getSprite("mini_ship"), 0, 0, .5f, .5f, 1, 1, 32, 32, r);
		r++;
		batch.draw(a.getSprite("mini_ship"), 0, 0);
		batch.end();
	}
}
