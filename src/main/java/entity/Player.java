package entity;

import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class Player extends AbstractEntity {
	int id = 0;

	EstadoPlayer estado;

	Image imagen = null;
	float scale = 1;

	int[] keys;

	private int shotInterval = 1000;

	private ParticleSystem trail;
	boolean renderTrail = false;

	private ParticleSystem shotFire1;
	private ParticleSystem shotFire2;
	private ParticleSystem shotFire3;

	float shotFireCount = 1;
	boolean renderShotFire = false;
	private GameContainer container;
	private EntityManager manager;
	private int delta;

	public Player(int id, int posX, int posY, String imageUrl, int[] playerKeys)
			throws SlickException {

		this.id = id;

		estado = new EstadoPlayer(posX, posY);

		imagen = new Image(imageUrl);
		imagen.setCenterOfRotation(51, 265);
		keys = playerKeys;

		try {
			trail = ParticleIO
					.loadConfiguredSystem("barcos/trail.xml");

			shotFire1 = ParticleIO
					.loadConfiguredSystem("barcos/small-explosion.xml");
			shotFire2 = ParticleIO
					.loadConfiguredSystem("barcos/small-explosion.xml");
			shotFire3 = ParticleIO
					.loadConfiguredSystem("barcos/small-explosion.xml");

		} catch (IOException e) {
			throw new SlickException("Failed to load particle systems", e);
		}

	}

	public int getID() {
		return this.id;
	}

	public void update(GameContainer gc, EntityManager manager, int delta) {
		this.manager = manager;
		this.container = gc;
		this.delta = delta;
		estado.setForwardX((float) Math.sin(Math
				.toRadians(estado.getRotation())));
		estado.setForwardY((float) -Math.cos(Math.toRadians(estado
				.getRotation())));

		estado.setPositionX(estado.getPositionX()
				+ ((estado.getForwardX() * delta) / 10) * estado.getSpeed());
		estado.setPositionY(estado.getPositionY()
				+ ((estado.getForwardY() * delta) / 10) * estado.getSpeed());

		if ((estado.getPositionX() + (imagen.getWidth() / 2)) > container
				.getWidth()) {
			estado.setPositionX(estado.getPositionX() - 2);
			estado.setSpeed(0);
		} else if ((estado.getPositionX() + (imagen.getWidth() / 2)) < 0) {
			estado.setPositionX(estado.getPositionX() + 2);
			estado.setSpeed(0);
		}

		if ((estado.getPositionY() + (imagen.getHeight() / 2)) > container
				.getHeight()) {
			estado.setPositionY(estado.getPositionY() - 2);
			estado.setSpeed(0);
		} else if ((estado.getPositionY() + (imagen.getHeight() / 2)) < 0) {
			estado.setPositionY(estado.getPositionY() + 2);
			estado.setSpeed(0);
		}
		trail.update(delta);
		((ConfigurableEmitter) trail.getEmitter(0)).setPosition(estado
				.getPositionX()
				+ (imagen.getWidth() / 2) - estado.getForwardX() * 125, estado
				.getPositionY()
				+ (imagen.getHeight() / 2) - estado.getForwardY() * 125);

		if (estado.getSpeed() > 0.5) {
			renderTrail = true;
		} else {
			renderTrail = false;
			trail.reset();
		}

		estado.setShotTimeout(estado.getShotTimeout() - delta);
		if (renderShotFire) {
			if (shotFireCount <= 0) {
				renderShotFire = false;
				shotFireCount = 1;
			}
			shotFireCount -= delta * 0.0005;

			shotFire1.update(delta);
			shotFire2.update(delta);
			shotFire3.update(delta);

		}
	}

	public void actualiza(int[] teclasPulsadas, int delta) {
		boolean acelerando = false;
		for (int i = 0; i < teclasPulsadas.length; i++) {
			if (keys[0] == teclasPulsadas[i]) {
				estado.setRotation(estado.getRotation() - (delta / 10.0f));
				imagen.setRotation(estado.getRotation());
			}
			if (keys[2] == teclasPulsadas[i]) {
				estado.setRotation(estado.getRotation() + (delta / 10.0f));
				imagen.setRotation(estado.getRotation());
			}

			if (keys[1] == teclasPulsadas[i]) {
				if (estado.getSpeed() < 3) {
					estado.setSpeed(estado.getSpeed() + delta * 0.001f);
					acelerando = true;
				}
			}

			if (estado.getShotTimeout() <= 0) {
				if ((keys[3] == teclasPulsadas[i]) && !container.isPaused()) {
					fireLeft(manager);
					estado.setShotTimeout(shotInterval);
				} else if ((keys[4] == teclasPulsadas[i])
						&& !container.isPaused()) {
					fireRight(manager);
					estado.setShotTimeout(shotInterval);
				}
			}
		}
		if (!acelerando && estado.getSpeed() > 0)
			estado.setSpeed(estado.getSpeed() - delta * 0.001f);
	}

	public void render(GameContainer gc) {
		imagen.draw(estado.getPositionX(), estado.getPositionY(), scale);

		if (renderTrail)
			trail.render();

		if (renderShotFire) {
			shotFire1.render();
			shotFire2.render();
			shotFire3.render();
		}

		// gc.getGraphics().draw(getCircle());
		//ArrayList<Circle> shape = getShape();
		//for (int i = 0; i < shape.size(); i++) {

			//gc.getGraphics().draw((Circle) shape.get(i));
		//}
	}

	public Circle getCircle() {
		Circle circle = new Circle(estado.getPositionX()
				+ (imagen.getWidth() / 2), estado.getPositionY()
				+ (imagen.getHeight() / 2), 50);
		return circle;
	}

	public ArrayList<Circle> getShape() {

		ArrayList<Circle> shape = new ArrayList<Circle>();

		float x1 = (estado.getPositionX() + (imagen.getWidth() / 2))
				+ estado.getForwardX() * 20;
		float y1 = (estado.getPositionY() + (imagen.getHeight() / 2))
				+ estado.getForwardY() * 20;
		float x2 = (estado.getPositionX() + (imagen.getWidth() / 2))
				+ estado.getForwardX() * -30;
		float y2 = (estado.getPositionY() + (imagen.getHeight() / 2))
				+ estado.getForwardY() * -30;
		float x3 = (estado.getPositionX() + (imagen.getWidth() / 2))
				+ estado.getForwardX() * -80;
		float y3 = (estado.getPositionY() + (imagen.getHeight() / 2))
				+ estado.getForwardY() * -80;

		Circle circle1 = new Circle(x1 + estado.getForwardX(), y1
				+ estado.getForwardY(), 30);
		Circle circle2 = new Circle(x2 + estado.getForwardX(), y2
				+ estado.getForwardY(), 30);
		Circle circle3 = new Circle(x3 + estado.getForwardX(), y3
				+ estado.getForwardY(), 30);

		shape.add(circle1);
		shape.add(circle2);
		shape.add(circle3);

		return shape;
	}

	public void collide(EntityManager manager, Entity other) {
		if (other instanceof Player) {
			estado.setSpeed(0);
			if (estado.getPositionX() >= other.getX())
				estado.setPositionX(estado.getPositionX() + 2);
			else
				estado.setPositionX(estado.getPositionX() - 2);

			if (estado.getPositionY() >= other.getY())
				estado.setPositionY(estado.getPositionY() + 2);
			else
				estado.setPositionY(estado.getPositionY() - 2);
		}

		manager.choque();
	}

	private void fireRight(EntityManager manager) {
		float dx = -estado.getForwardY();
		float dy = estado.getForwardX();
		float x = (estado.getPositionX() + (imagen.getWidth() / 2)) + dx * 70;
		float y = (estado.getPositionY() + (imagen.getHeight() / 2)) + dy * 70;

		try {
			Shot shot1 = new Shot(x, y, dx * 500, dy * 500);
			manager.addEntity(shot1);

			Shot shot2 = new Shot(x + 15 * estado.getForwardX(), y + 15
					* estado.getForwardY(), dx * 500, dy * 500);
			manager.addEntity(shot2);

			Shot shot3 = new Shot(x - 15 * estado.getForwardX(), y - 15
					* estado.getForwardY(), dx * 500, dy * 500);
			manager.addEntity(shot3);

		} catch (SlickException e) {
			e.printStackTrace();
		}

		float shotX = (estado.getPositionX() + (imagen.getWidth() / 2)) + dx
				* 40;
		float shotY = (estado.getPositionY() + (imagen.getHeight() / 2)) + dy
				* 40;

		((ConfigurableEmitter) shotFire1.getEmitter(0)).setPosition(shotX,
				shotY);
		((ConfigurableEmitter) shotFire2.getEmitter(0)).setPosition(shotX + 15
				* estado.getForwardX(), shotY + 15 * estado.getForwardY());
		((ConfigurableEmitter) shotFire3.getEmitter(0)).setPosition(shotX - 15
				* estado.getForwardX(), shotY - 15 * estado.getForwardY());

		shotFire1.reset();
		shotFire2.reset();
		shotFire3.reset();

		shotFireCount = 1;
		renderShotFire = true;

		manager.shotFired();

	}

	private void fireLeft(EntityManager manager) {
		float dx = estado.getForwardY();
		float dy = -estado.getForwardX();
		float x = (estado.getPositionX() + (imagen.getWidth() / 2)) + dx * 70;
		float y = (estado.getPositionY() + (imagen.getHeight() / 2)) + dy * 70;

		try {
			Shot shot1 = new Shot(x, y, dx * 500, dy * 500);
			manager.addEntity(shot1);

			Shot shot2 = new Shot(x + 15 * estado.getForwardX(), y + 15
					* estado.getForwardY(), dx * 500, dy * 500);
			manager.addEntity(shot2);

			Shot shot3 = new Shot(x - 15 * estado.getForwardX(), y - 15
					* estado.getForwardY(), dx * 500, dy * 500);
			manager.addEntity(shot3);

		} catch (SlickException e) {
			e.printStackTrace();
		}

		float shotX = (estado.getPositionX() + (imagen.getWidth() / 2)) + dx
				* 40;
		float shotY = (estado.getPositionY() + (imagen.getHeight() / 2)) + dy
				* 40;

		((ConfigurableEmitter) shotFire1.getEmitter(0)).setPosition(shotX,
				shotY);
		((ConfigurableEmitter) shotFire2.getEmitter(0)).setPosition(shotX + 15
				* estado.getForwardX(), shotY + 15 * estado.getForwardY());
		((ConfigurableEmitter) shotFire3.getEmitter(0)).setPosition(shotX - 15
				* estado.getForwardX(), shotY - 15 * estado.getForwardY());

		shotFire1.reset();
		shotFire2.reset();
		shotFire3.reset();

		shotFireCount = 1;
		renderShotFire = true;

		manager.shotFired();

	}

	@Override
	public float getX() {
		return estado.getPositionX();
	}

	@Override
	public float getY() {
		return estado.getPositionY();
	}

	public EstadoPlayer getEstado() {
		return estado;
	}

	public void setEstado(EstadoPlayer estado) {
		this.estado = estado;
	}

}
