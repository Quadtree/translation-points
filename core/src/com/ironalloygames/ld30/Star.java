package com.ironalloygames.ld30;

import java.util.HashSet;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ironalloygames.ld30.world.World;

public class Star extends Actor {

	public static final float BURN_POWER = 0.1f;
	public static final float BURN_RANGE = 20;

	Fixture burnSensor;

	HashSet<Actor> inBurnRange = new HashSet<Actor>();

	@Override
	public void beginContact(Actor other, Fixture localFixture) {
		super.beginContact(other, localFixture);

		if (localFixture == burnSensor && !(other instanceof Star)) {
			inBurnRange.add(other);
		}
	}

	@Override
	public void endContact(Actor other, Fixture localFixture) {
		super.endContact(other, localFixture);

		if (localFixture == burnSensor) {
			inBurnRange.remove(other);
		}
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		FixtureDef fd = new FixtureDef();
		fd.density = 0;
		fd.isSensor = true;

		CircleShape cs1 = new CircleShape();
		cs1.setRadius(BURN_RANGE);
		fd.shape = cs1;

		body.createFixture(fd);

		CircleShape cs2 = new CircleShape();
		body.createFixture(cs2, 0.2f);
	}

	@Override
	public void render() {
		super.render();

		drawDefault("star1");
	}

	@Override
	public void update() {
		super.update();

		for (Actor a : inBurnRange) {
			a.takeDamage(BURN_POWER * (1 - (a.position.dst(position) / BURN_RANGE)));
		}
	}

}
