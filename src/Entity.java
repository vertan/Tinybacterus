import java.io.IOException;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;


public class Entity {
	
	// Variables
	private Image sprite;
	private float x,y;
	private Rectangle hitbox;
	private boolean isAlive;
	private ParticleSystem particles;
	private int hp;
	
	// Accessors
	public Image getSprite() { return this.sprite; }
	public float getX() { return this.x; }
	public float getY() { return this.y; }
	public Rectangle getHitbox() { return this.hitbox; }
	public boolean getAlive() { return this.isAlive; }
	public ParticleSystem getParticles() { return this.particles; }
	public int getHP() { return this.hp; }
	
	// Mutators
	public void setSprite(Image newSprite) { this.sprite = newSprite; }
	public void setX(float newX) { this.x = newX; }
	public void setY(float newY) { this.y = newY; }
	public void setHitbox(Rectangle newHitbox) { this.hitbox = newHitbox; }
	public void setAlive(boolean newAlive) { this.isAlive = newAlive; }
	public void setHP(int newHP) { this.hp = newHP; }
	
	public void setParticles(String particlePath) {
    	try {
    		particles = ParticleIO.loadConfiguredSystem(particlePath);
		} catch (IOException e) {
			System.out.print("Gick inte att ladda config-filen för partikeln!");
		}
	}
	
}
