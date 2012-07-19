import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class Bullet extends MovingEntity {

	public Bullet() throws SlickException {
		setSprite(new Image("data/bullet.png"));
		setX(0.0f);
		setY(0.0f);
		setHitbox(new Rectangle(this.getX(),this.getY(),5,5));
		setVelX(0.0f);
		setVelY(0.0f);
		setVelXmax(8f);
		setVelYmax(8f);
		setAlive(true);
		setParticles("data/bulletTrail.xml");
	}

}