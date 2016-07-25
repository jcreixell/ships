import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.BasicGame;

import entity.AbstractEntity;
import entity.Entity;
import entity.EntityManager;
import entity.Player;

public class Ships extends BasicGame implements EntityManager
{
	public Ships(String gamename)
	{
		super(gamename);
	}


	protected final int[] player1Keys = { Input.KEY_A, Input.KEY_W,
			Input.KEY_D, Input.KEY_F, Input.KEY_G };

	protected final int[] player2Keys = { Input.KEY_LEFT, Input.KEY_UP,
			Input.KEY_RIGHT, Input.KEY_N, Input.KEY_M };

	int stateID = 0;
	private GameContainer container;

	Image background = null;
	boolean fondoAbajo;
	boolean fondoDerecha;
	float fondoX;
	float fondoY;
	public static AngelCodeFont font;

	private ArrayList<AbstractEntity> entities = new ArrayList<AbstractEntity>();
	private ArrayList<AbstractEntity> addList = new ArrayList<AbstractEntity>();
	private ArrayList<Entity> removeList = new ArrayList<Entity>();

	protected Player player1;
	protected Player player2;

	private ParticleSystem hit;
	float hitCount = 1;
	boolean renderHit = false;

	protected int lifePlayer1 = 50;
	protected int lifePlayer2 = 50;

	Sound shotFx = null;
	Sound explosionFx = null;
	Sound choqueFx = null;

	protected boolean gameOver = false;
	boolean player1win, player2win;

	int penalizacion;
	private int[] teclasPulsadas;

	public void init(GameContainer container)
			throws SlickException {
		this.penalizacion = 5;
		this.player1win = true;
		this.player2win = true;
		this.container = container;
		this.cargarRecursos();

		fondoX = 0;
		fondoY = 0;

	}

	public void render(GameContainer container, Graphics g) {

		background.draw(fondoX - 40, fondoY - 50,
				container.getScreenWidth() + 50,
				container.getScreenHeight() + 50);
		if (fondoAbajo) {
			fondoY += 0.15;
			if (fondoY >= 50)
				fondoAbajo = false;
		} else {
			fondoY -= 0.15;
			if (fondoY <= 0)
				fondoAbajo = true;
		}
		if (fondoDerecha) {
			fondoX += 0.12;
			if (fondoX >= 40)
				fondoDerecha = false;
		} else {
			fondoX -= 0.12;
			if (fondoY <= 40)
				fondoDerecha = true;
		}

		for (int i = 0; i < entities.size(); i++) {
			Entity entity = (Entity) entities.get(i);

			entity.render(container);
		}

		if (renderHit) {
			hit.render();
		}

		drawGUI(g);

		if (gameOver) {
			gameOver();

		}

		if (container.isPaused())
			pause();

	}

	public void update(GameContainer container, int delta) {

		if (!gameOver) {

			// Checkear colisiones (fuerza bruta)

			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);
				for (int j = i + 1; j < entities.size(); j++) {
					Entity other = (Entity) entities.get(j);

					if (entity.collides(other)) {
						entity.collide(this, other);
						other.collide(this, entity);
					}
				}
			}

			entities.removeAll(removeList);
			entities.addAll(addList);

			removeList.clear();
			addList.clear();

			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);

				entity.update(container, this, delta);
			}

			if (renderHit) {
				if (hitCount <= 0) {
					renderHit = false;
					hitCount = 1;
				}
				hitCount -= delta * 0.0005;
				hit.update(delta);
			}
		}

		if ((lifePlayer1 <= 0) || (lifePlayer2 <= 0)) {
			gameOver = true;
		}

		if (teclasPulsadas != null) {
			player1.actualiza(teclasPulsadas, delta);
			player2.actualiza(teclasPulsadas, delta);

		}

	}

	public void drawGUI(Graphics g) {

		String cadena1 = "First";
		String cadena2 = "Second";
		font.drawString(10, 0, cadena1,
				Color.red);
		font.drawString(10, 40, cadena2,
				Color.blue);
	}

	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}

	public void addEntity(Entity entity) {
		addList.add((AbstractEntity) entity);
	}

	public void playerHit(int player, float x, float y) {
		((ConfigurableEmitter) hit.getEmitter(0)).setPosition(x, y);
		((ConfigurableEmitter) hit.getEmitter(1)).setPosition(x, y);
		((ConfigurableEmitter) hit.getEmitter(2)).setPosition(x, y);
		((ConfigurableEmitter) hit.getEmitter(3)).setPosition(x, y);

		hit.reset();
		hitCount = 1;
		renderHit = true;

		if (player == 1)
			lifePlayer1 -= 2;
		else
			lifePlayer2 -= 2;

		if (explosionFx.playing())
			explosionFx.stop();
		explosionFx.play(1.0f, 0.5f);
	}

	public void shotFired() {
		shotFx.play();

	}

	public void gameOver() {
		if (lifePlayer1 <= 0)
			font.drawString(200, 240, "First"
					+ " ha perdido su barco\n PULSA ESPACIO PARA CONTINUAR ",
					Color.green);
		else if (lifePlayer2 <= 0)
			font.drawString(200, 240, "Second"
					+ " ha perdido su barco\n PULSA ESPACIO PARA CONTINUAR  ",
					Color.green);

	}

	public void choque() {
		choqueFx.play(1.0f, 0.4f);
	}

	public void reset() throws SlickException {
		entities.clear();

		System.gc(); // forzamos recolector de basura

		lifePlayer1 = 50;
		lifePlayer2 = 50;

		this.hit.reset();
		renderHit = false;

		gameOver = false;

	}

	public void pause() {
		font.drawString(350, 270, "Pause ", Color.pink);

	}

	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ENTER) {
			if (!container.isPaused())
				container.pause();
			else
				container.resume();
		} else {
			for (int j = 0; j < this.player1Keys.length; j++)
				if ((player1Keys[j] == key) || (player2Keys[j] == key)) {
					boolean encontrada = false;
					int i = 0;
					while (!encontrada && i < teclasPulsadas.length) {
						if (this.teclasPulsadas[i] == key) {
							encontrada = true;
						}
						i++;
					}
					if (!encontrada) {
						int k = 0;
						boolean hecha = false;
						while (k < teclasPulsadas.length && !hecha) {
							if (teclasPulsadas[k] == 0) {
								teclasPulsadas[k] = key;
								hecha = true;
							}
							k++;
						}
					}
				}

		}
	}

	public void keyReleased(int key, char c) {
		if (key == Input.KEY_SPACE && (!container.isPaused()) && (gameOver)) {
			// [Cargador] this.game.enterState(Principal.TABLERO);
		} else if (key == Input.KEY_ENTER) {
		} else {
			for (int j = 0; j < this.player1Keys.length; j++)
				if ((player1Keys[j] == key) || (player2Keys[j] == key)) {
					for (int i = 0; i < teclasPulsadas.length; i++)
						if (this.teclasPulsadas[i] == key) {
							teclasPulsadas[i] = 0;
						}
				}
		}
	}

	public void cargarRecursos() throws SlickException {
		Calendar ahora1 = Calendar.getInstance();
		long tiempo1 = ahora1.getTimeInMillis();

		background = new Image("barcos/sea.png");
		font = new AngelCodeFont("fonts/scorefnt.fnt",
				"fonts/scorefnt.png");

		int[] player1Keys = { Input.KEY_A, Input.KEY_W, Input.KEY_D,
				Input.KEY_F, Input.KEY_G };
		player1 = new Player(1, 200, 300,
				"barcos/barcorojo.png", player1Keys);
		entities.add(player1);

		int[] player2Keys = { Input.KEY_LEFT, Input.KEY_UP, Input.KEY_RIGHT,
				Input.KEY_N, Input.KEY_M };
		player2 = new Player(2, 600, 300,
				"barcos/barcoazul.png", player2Keys);
		entities.add(player2);

		shotFx = new Sound("barcos/fire.ogg");
		explosionFx = new Sound("barcos/explosion_2.ogg");
		choqueFx = new Sound("barcos/choque.ogg");

		teclasPulsadas = new int[10];
		for (int i = 0; i < 10; i++) {
			teclasPulsadas[i] = 0;
		}

		try {
			hit = ParticleIO
					.loadConfiguredSystem("barcos/explosion.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar ahora2 = Calendar.getInstance();
		long tiempo2 = ahora2.getTimeInMillis();
		System.out.println("Cargando Juego Barcos: " + (tiempo2 - tiempo1)
				+ " milisegundos");

	}

	public void liberarRecursos() {

		background = null;
		font = null;
		player1 = null;
		player2 = null;
		shotFx = null;
		explosionFx = null;
		choqueFx = null;
		hit = null;
		this.teclasPulsadas = null;

	}

}