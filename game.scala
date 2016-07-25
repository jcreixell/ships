import java.util.logging.Level
import java.util.logging.Logger
import org.newdawn.slick.AppGameContainer
import org.newdawn.slick.BasicGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.SlickException

class SimpleSlickGame(name: String) extends BasicGame(name) {
  def init(gc: GameContainer) = {
  
  }

  def update(gc: GameContainer, i: Int) = {
  
  }

  def render(gc: GameContainer, g: Graphics) = {
    g.drawString("Howdy!", 10, 10);
  }

}

object Game extends App {
  try {
    var appgc = new AppGameContainer(new Ships("Ships"))
    appgc.setDisplayMode(640, 480, false)
    appgc.setShowFPS(false)
    appgc.start()
  } catch {
    case ex: SlickException => {
      Logger.getLogger("Game").log(Level.SEVERE, null, ex);
    }
  } 
}
