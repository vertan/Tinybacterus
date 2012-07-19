import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class MotherShip extends MovingEntity {
	
	public MotherShip() throws SlickException {
		setSprite(new Image("data/ship.png"));
		setX(0.0f);
		setY(0.0f);
		setHitbox(new Rectangle(this.getX(),this.getY(),50,50));
		setVelX(0.0f);
		setVelY(0.0f);
		setVelXmax(5f);
		setVelYmax(5f);
		setHP(100);
		setParticles("data/smoke.xml");
	}

}
