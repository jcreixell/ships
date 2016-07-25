package entity;

import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import org.newdawn.slick.geom.Circle;

public class Shot extends AbstractEntity {
	Image imagen = null;
	int scale = 1;
	private int life = 800;

	protected float positionX = 0;
	protected float positionY = 0;

	protected float velocityX = 0;
	protected float velocityY = 0;

	public Shot(float x, float y, float vx, float vy) throws SlickException {
		positionX = x;
		positionY = y;
		velocityX = vx;
		velocityY = vy;

		imagen = new Image("barcos/bala.png");
		imagen.setCenterOfRotation(51, 265);
	}

	public void update(GameContainer gc, EntityManager manager, int delta) {

		positionX += (velocityX * delta) / 1000.0f;
		positionY += (velocityY * delta) / 1000.0f;

		life -= delta;
		if (life < 0)
			manager.removeEntity(this);
	}

	public void render(GameContainer gc) {
		imagen.draw(positionX, positionY, scale);
		gc.getGraphics().draw((Circle) getShape().get(0));
	}

	public ArrayList<Circle> getShape() {
		Circle circle1 = new Circle(positionX + (imagen.getWidth() / 2),
				positionY + (imagen.getHeight() / 2), 5);

		ArrayList<Circle> shape = new ArrayList<Circle>();
		shape.add(circle1);

		return shape;
	}

	public void collide(EntityManager manager, Entity other) {
		if (other instanceof Player) {

			manager.removeEntity(this);
			manager.playerHit(((Player) other).getID(), positionX, positionY);
		}

		else if (other instanceof Shot) {
			manager.removeEntity(this);
			manager.removeEntity(other);
		}
	}

	public float getX() {
		return positionX;
	}

	public float getY() {
		return positionY;
	}

}
