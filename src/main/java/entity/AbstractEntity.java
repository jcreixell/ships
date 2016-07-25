package entity;

import java.util.ArrayList;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

public abstract class AbstractEntity implements Entity {

	public boolean collides(Entity other) {

		ArrayList<Circle> shapes1 = this.getShape();
		ArrayList<Circle> shapes2 = other.getShape();

		for (int i = 0; i < shapes1.size(); i++) {
			Shape shape1 = (Shape) shapes1.get(i);
			for (int j = 0; j < shapes2.size(); j++) {
				Shape shape2 = (Shape) shapes2.get(j);

				if (shape1.intersects(shape2)) {

					return true;
				}
			}
		}
		return false;

	}
}
