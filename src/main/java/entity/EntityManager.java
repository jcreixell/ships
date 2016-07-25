package entity;

public interface EntityManager {

	public void removeEntity(Entity entity);

	public void addEntity(Entity entity);

	public void playerHit(int player, float x, float y);

	public void shotFired();

	public void choque();

}
