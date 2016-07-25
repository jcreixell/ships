package entity;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;

import org.newdawn.slick.geom.Circle;

public interface Entity {

	public void update(GameContainer gc, EntityManager manager, int delta);

	public void render(GameContainer gc);

	public ArrayList<Circle> getShape();

	public float getX();

	public float getY();

	public void collide(EntityManager manager, Entity other);

	public boolean collides(Entity other);
}
