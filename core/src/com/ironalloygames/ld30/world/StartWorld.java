package com.ironalloygames.ld30.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ironalloygames.ld30.Asteroid;
import com.ironalloygames.ld30.CapitalTranslationPoint;
import com.ironalloygames.ld30.Gem;
import com.ironalloygames.ld30.Mothership;

public class StartWorld extends World {

	public StartWorld() {
		Mothership ms = new Mothership();

		CapitalTranslationPoint pt = new CapitalTranslationPoint();
		pt.setPosition(new Vector2(0, 0));
		pt.lifespan = 180;

		ms.immuneTranslationPoint = pt;
		ms.lastTranslationPoint = pt;

		addActor(pt);
		addActor(ms);

		Gem g1 = new Gem();
		g1.setPosition(new Vector2(20, 120));
		g1.setVelocity(new Vector2(1, -1));
		g1.setAngle(MathUtils.random(MathUtils.PI2));
		g1.setAngularVelocity(MathUtils.random(MathUtils.PI));

		addActor(g1);

		for (int i = 0; i < 2; i++) {
			Gem g = new Gem();
			g.setPosition(new Vector2(MathUtils.random(-RADIUS, RADIUS), MathUtils.random(-RADIUS, RADIUS)));

			while (g.getPosition().len2() < 200 * 200) {
				g.setPosition(new Vector2(MathUtils.random(-RADIUS, RADIUS), MathUtils.random(-RADIUS, RADIUS)));
			}

			addActor(g);

			int c = MathUtils.random(7, 10);

			for (int j = 0; j < c; j++) {
				Asteroid ast = new Asteroid();
				ast.setPosition(g.getPosition().add(MathUtils.cos(j / (float) c * MathUtils.PI2) * c, MathUtils.sin(j / (float) c * MathUtils.PI2) * c));
				addActor(ast);
			}
		}

		for (int i = 0; i < 15; i++) {
			Asteroid ast = new Asteroid();
			ast.setPosition(new Vector2(MathUtils.random(-RADIUS, RADIUS), MathUtils.random(-RADIUS, RADIUS)));

			while (ast.getPosition().len2() < 200 * 200) {
				ast.setPosition(new Vector2(MathUtils.random(-RADIUS, RADIUS), MathUtils.random(-RADIUS, RADIUS)));
			}

			addActor(ast);
		}
	}

	@Override
	public void renderBackground() {
		super.renderBackground();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	protected void update() {
		super.update();

	}
}
