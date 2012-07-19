
public class MovingEntity extends Entity {
	
	// Variables
	private float velX,velY,velXmax,velYmax;
	
	// Accessors
	public float getVelX(){ return this.velX; }
	public float getVelY(){ return this.velY; }
	public float getVelXmax(){ return this.velXmax; }
	public float getVelYmax() { return this.velYmax; }
	
	// Mutators
	public void setVelX(float newVelX) { this.velX = newVelX; }
	public void setVelY(float newVelY) { this.velY = newVelY; }
	public void setVelXmax(float newVelXmax) { this.velXmax = newVelXmax; }
	public void setVelYmax(float newVelYmax) { this.velYmax = newVelYmax; }

}
