package entity;

import java.io.Serializable;

public class EstadoPlayer implements Serializable {

	boolean acelerando = false;
	float rotation = 0;
	float speed = 0;

	private float forwardX = 0;
	private float forwardY = 1;

	private int shotTimeout;

	protected float positionX = 0;
	protected float positionY = 0;

	public EstadoPlayer(int posX, int posY) {
		positionX = posX;
		positionY = posY;

	}

	public boolean isAcelerando() {
		return acelerando;
	}

	public void setAcelerando(boolean acelerando) {
		this.acelerando = acelerando;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getForwardX() {
		return forwardX;
	}

	public void setForwardX(float forwardX) {
		this.forwardX = forwardX;
	}

	public float getForwardY() {
		return forwardY;
	}

	public void setForwardY(float forwardY) {
		this.forwardY = forwardY;
	}

	public int getShotTimeout() {
		return shotTimeout;
	}

	public void setShotTimeout(int shotTimeout) {
		this.shotTimeout = shotTimeout;
	}

	public float getPositionX() {
		return positionX;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public float getPositionY() {
		return positionY;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}
}
