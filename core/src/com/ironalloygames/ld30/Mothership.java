package com.ironalloygames.ld30;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.ironalloygames.ld30.world.World;

public class Mothership extends Actor {

	int firstMessageTimer = 0;

	boolean firstSpawnPossible = false;

	public int gems = 0;

	public boolean hasEngine = true;

	int heyDialogTimer = -100000000;

	boolean isRayClear = false;

	int startTime = 0;

	int talkCooldown = 3000;

	ArrayList<Vector2> towBeamTargets = new ArrayList<Vector2>();

	public Mothership() {
		angle = MathUtils.PI / 2;
		LD30.mothership = this;

	}

	@Override
	public void beginContact(Actor other, Fixture localFixture) {
		super.beginContact(other, localFixture);

		if (other instanceof Gem && other.hp > 0) {
			gems++;
			other.hp = -1000;

			LD30.a.getSound("gem").play();
		}

		if (other instanceof MothershipEngine && other.hp > 0) {
			this.hasEngine = true;
			other.hp = -1000;
		}
	}

	public void detachEngine() {
		hasEngine = false;

		MothershipEngine me = new MothershipEngine();
		me.setPosition(getPosition().add(0, -175 / LD30.METER_SCALE));
		me.lastTranslationPoint = this.lastTranslationPoint;
		me.immuneTranslationPoint = this.immuneTranslationPoint;

		world.addActor(me);

		if (heyDialogTimer < 0)
			heyDialogTimer = 0;
	}

	@Override
	public void enteringWorld(World world) {
		super.enteringWorld(world);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(425 / 2 / LD30.METER_SCALE, 314 / 2 / LD30.METER_SCALE, new Vector2(0, 0), 0);

		FixtureDef fd = new FixtureDef();
		fd.filter.groupIndex = -5;
		fd.shape = ps;
		fd.density = 0;

		body.createFixture(fd);
	}

	@Override
	protected BodyType getBodyType() {
		return BodyType.KinematicBody;
	}

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public boolean isTranslatable() {
		return false;
	}

	@Override
	public void render() {
		super.render();

		this.drawDefault("mothership" + (hasEngine ? "3" : "1"));

		if (LD30.pc != null && LD30.pc.world == world && LD30.pc.position.dst2(position) < 200 * 200 && LD30.pc.hp < LD30.pc.getMaxHP()) {
			Vector2 d = LD30.pc.position.cpy().sub(position);
			LD30.batch.setColor(Color.GREEN);
			LD30.batch.draw(LD30.a.getSprite("ray"), getPosition().x, getPosition().y, 0, .5f, 1, 1, d.len(), 64f / LD30.METER_SCALE, d.angle());
		}

		for (Vector2 v2 : towBeamTargets) {
			Vector2 d = v2.cpy().sub(position);
			LD30.batch.setColor(Color.BLUE);
			LD30.batch.draw(LD30.a.getSprite("ray"), getPosition().x, getPosition().y, 0, .5f, 1, 1, d.len(), 64f / LD30.METER_SCALE, d.angle());
		}
	}

	@Override
	public void update() {
		super.update();

		firstMessageTimer++;
		heyDialogTimer++;

		if (firstMessageTimer == 40)
			this.addDialogue("Excellent, this dimension looks ideal for gem mining", new Vector2(40, 0), 240);

		if (firstMessageTimer == 180)
			this.addDialogue("You've said it", new Vector2(35, 0), 120);

		if (heyDialogTimer == 50)
			this.addDialogue("Hey!!!", new Vector2(30, 0), 120);

		if (heyDialogTimer == 250)
			this.addDialogue("All stations, an unknown ship has stolen a ship component", new Vector2(25, 0), 240);

		if (heyDialogTimer == 350)
			this.addDialogue("Please report anything missing", new Vector2(20, 0), 200);

		if (heyDialogTimer == 400)
			this.addDialogue("Tow beam, nothing missing", new Vector2(40, -50), 200);

		if (heyDialogTimer == 410)
			this.addDialogue("Gem processing, nothing missing", new Vector2(30, 50), 200);

		if (heyDialogTimer == 550)
			this.addDialogue("Engine?", new Vector2(40, 0), 120);

		if (heyDialogTimer == 650)
			this.addDialogue("Engine? Dammit!", new Vector2(35, 0), 120);

		if (heyDialogTimer == 800) {
			this.addDialogue("Exploration ship, please see if you can retrieve our engine.", new Vector2(50, 0), 600);
			firstSpawnPossible = true;
		}

		if (heyDialogTimer == 820)
			this.addDialogue("And gems!!! ... If you want upgrades", new Vector2(45, 0), 600);

		if (heyDialogTimer == 900) {
			this.addDialogue("Create translation points to bring the engine back to this sector", new Vector2(40, 0), 600);
			firstSpawnPossible = true;
		}

		if (heyDialogTimer == 960) {
			this.addDialogue("Then we'll pull it in. Also make sure that guy is gone, or he'll just steal it again!", new Vector2(35, 0), 600);
			firstSpawnPossible = true;
		}

		if (startTime < 140) {
			body.setLinearVelocity(0, 15);
		} else {
			body.setLinearVelocity(0, 0);
		}

		startTime++;
		talkCooldown--;

		LD30.respawnTimer--;

		if (LD30.pc == null && (firstSpawnPossible)) {

			if (LD30.respawnTimer <= 0) {
				PlayerMiniShip pc = new PlayerMiniShip();
				LD30.pc = pc;
				pc.setPosition(new Vector2(0, 90));
				world.addActor(pc);
			}
		}

		if (LD30.pc != null && LD30.pc.world == world && LD30.pc.position.dst2(position) < 110 * 110 && talkCooldown <= 0 && !LD30.victoryDialogEverShown) {
			talkCooldown = 1200;

			this.addDialogue("Create some translation points to bring our engine back to this sector", new Vector2(40, 0), 600);
			this.addDialogue("We'll take it from there, but make sure that guy is gone so he can't steal it again!", new Vector2(35, 0), 600);
		}

		if (LD30.pc != null && LD30.pc.world == world && LD30.pc.position.dst2(position) < 150 * 150 && LD30.pc.hp < LD30.pc.getMaxHP()) {
			LD30.pc.hp = Math.min(LD30.pc.hp + 0.005f, LD30.pc.getMaxHP());

		}

		towBeamTargets.clear();

		if (firstSpawnPossible) {
			for (final Actor a : world.actors) {
				if (a instanceof Gem || a instanceof MothershipEngine) {

					if (a instanceof MothershipEngine && LD30.enemyMiniShip != null && LD30.enemyMiniShip.keep() && LD30.enemyMiniShip.world == world)
						continue;

					isRayClear = true;

					world.physicsWorld.rayCast(new RayCastCallback() {

						@Override
						public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
							if (fixture.isSensor() || fixture.getBody().getUserData() instanceof Mothership || fixture.getBody().getUserData() == a) {
								return 1;
							}

							isRayClear = false;
							return 0;
						}
					}, position, a.getPosition());

					if (isRayClear) {
						Vector2 impulse = position.cpy().sub(a.position);
						impulse.nor();
						impulse.scl(1500 / 4);
						a.body.applyLinearImpulse(impulse, a.body.getWorldCenter(), true);

						towBeamTargets.add(a.position.cpy());

						if (position.dst2(a.position) < 50 * 50 && a instanceof MothershipEngine && ((MothershipEngine) a).offTimer > 200 * 3) {
							a.hp = -1000;
							this.hasEngine = true;
						}
					}
				}
			}
		}
	}

}
