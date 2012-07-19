//////////////////////////////////////////////////////////////////
//
// Coded by Filip "Vertigo" Hedman (@DeepFriedMedaka at Twitter)
// for the Ludum Dare 23 competition!
//
//         Version 1.12
//	
//////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.particles.ConfigurableEmitter;

public class DareMain extends BasicGame {
	
	private MotherShip ship;
	private ArrayList bullets;
	private ArrayList enemies;
	private ArrayList explosions;
	private Sound soundShoot,soundBackground,soundExplosion;
	private int enemiesKilled;
	private int gameState;
	private Rectangle button;
	private Rectangle button2;
	private Image logo;
	private long time,endTime;
	
    public DareMain() {
        super("Tinybacterus");
    }

    public static void main(String[] arguments) {
        try {
            AppGameContainer app = new AppGameContainer(new DareMain());
            app.setDisplayMode(1024, 768, false);
            app.setVSync(true);
            app.setTargetFrameRate(60);
            app.setShowFPS(false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    public void restart(GameContainer container) throws SlickException {
    	// Set default values
    	ship = new MotherShip();
    	ship.setX(container.getWidth()/2);
    	ship.setY(container.getHeight()/2);
    	bullets = new ArrayList();
    	enemies = new ArrayList();
    	explosions = new ArrayList();
    	enemiesKilled = 0;
    	gameState = 1;
		soundShoot = new Sound("data/shoot2.wav");
		soundBackground = new Sound("data/background_music.wav");
		soundExplosion = new Sound("data/explosion.wav");
		button = new Rectangle(0,0,0,0);
		button2 = new Rectangle(0,0,0,0);
		time = System.currentTimeMillis()/1000;
    }

    public void init(GameContainer container) throws SlickException {
    	this.restart(container);
    	ship = new MotherShip();
    	ship.setHP(0);
    	gameState = 3;
    }

    public void update(GameContainer container, int delta) throws SlickException {
    	Input input = container.getInput();
    	
    	if(gameState == 1) { // The game is underway
    		
    		if(!soundBackground.playing()) {
    			soundBackground.play();
    		}
    		
        	if(explosions.size()>40) {
        		for(int i=0;i<10;i++) {
        			explosions.remove(i);
        		}
        	}
    		
    		// Make some new bacterias
	    	int r = (int) (Math.random()*4+1);
	    	if(enemies.size()<5) {
	    		enemies.add(new Enemy());
	    		if(r == 1){
		    		((Enemy) enemies.get(enemies.size()-1)).setX((float)(Math.random()*container.getWidth())+1);
		    		((Enemy) enemies.get(enemies.size()-1)).setY((float)container.getHeight()+100);
	    		} 
	    		else if(r == 2) {
		    		((Enemy) enemies.get(enemies.size()-1)).setX((float)container.getWidth()+100);
		    		((Enemy) enemies.get(enemies.size()-1)).setY((float)(Math.random()*container.getHeight())+1);
	    		}
	    		else if(r == 3) {
		    		((Enemy) enemies.get(enemies.size()-1)).setX(-100);
		    		((Enemy) enemies.get(enemies.size()-1)).setY((float)(Math.random()*container.getHeight())+1);
	    		}
	    		else if(r == 4) {
		    		((Enemy) enemies.get(enemies.size()-1)).setX((float)container.getWidth()+100);
		    		((Enemy) enemies.get(enemies.size()-1)).setY(-100);
	    		}
	    	}
	    	
	    	// Update the particles for the explosions
			if(explosions.size()>0) {
				for(int n=0;n<explosions.size();n++) {
					Entity explosion = (Entity) explosions.get(n);
					explosion.getParticles().update(delta);
	    			((ConfigurableEmitter) explosion.getParticles().getEmitter(0)).setEnabled(false);
				}
			}
	    	
			// Makes sure that one can't go through walls on the X axis
	    	float tempVel = ship.getVelX();
	    	if(ship.getX()<0) { ship.setVelX(-(tempVel/2)); ship.setX(ship.getX()+1); }
	    	if(ship.getX()>container.getWidth()-50) { ship.setVelX(-(tempVel/2)); ship.setX(ship.getX()-1); }
	    	
	    	// Makes sure that one can't go through walls on the X axis
	    	tempVel = ship.getVelY();
	    	if(ship.getY()<0) { ship.setVelY(-(tempVel/2)); ship.setY(ship.getY()+1); }
	    	if(ship.getY()>container.getHeight()-50) { ship.setVelY(-(tempVel/2)); ship.setY(ship.getY()-1); }
	    	
	    	// Ship movement
	    	if(input.isKeyDown(Input.KEY_S) && ship.getY()<container.getHeight()-50) {
	    		if(ship.getVelY()<ship.getVelYmax()) {
	    			ship.setVelY(ship.getVelY()+0.2f);
	    		}
	    	}
	    	if(input.isKeyDown(Input.KEY_W) && ship.getY()>0) {
	    		if(ship.getVelY()>(-ship.getVelYmax())) {
	    			ship.setVelY(ship.getVelY()-0.2f);
	    		}
	    	}
	    	if(input.isKeyDown(Input.KEY_D) && ship.getX()<container.getWidth()-50) {
	    		if(ship.getVelX()<ship.getVelXmax()) {
	    			ship.setVelX(ship.getVelX()+0.2f);
	    		}
	    	}
	    	if(input.isKeyDown(Input.KEY_A)) {
	    		if(ship.getVelX()>(-ship.getVelXmax()) && ship.getX()>0) {
	    			ship.setVelX(ship.getVelX()-0.2f);
	    		}
	    	}
	    	
	    	// Shooting bullets!
	    	if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
	    		soundShoot.play();
	    		bullets.add(new Bullet());
	    		Bullet current = ((Bullet) bullets.get(bullets.size()-1));
	    		current.setX(ship.getX()+25);
	    		current.setY(ship.getY()+25);
	    		
	    		current.setVelX((input.getMouseX()-ship.getX()-25));
	    		current.setVelY((input.getMouseY()-ship.getY()-25));
	    		double temp = Math.atan(current.getVelY()/current.getVelX());
	    		
	    		// The bullet's direction
	    		if(input.getMouseX()>ship.getX()+25 && input.getMouseX() != 0){
	    			current.setVelX((float)Math.cos(temp)*current.getVelXmax());
	    			current.setVelY((float)Math.sin(temp)*current.getVelYmax());
	    		} else {
	        		current.setVelX((float)Math.cos(temp)*-current.getVelXmax());
	            	current.setVelY((float)Math.sin(temp)*-current.getVelYmax());
	    		}
	    	}
	    	
	    	// Update bullet particles
	    	if(bullets.size() > 0) {
		    	for(int i = 0;i<bullets.size(); i++) {
		    		(((Bullet) bullets.get(i)).getParticles()).update(delta);
		    	}
	    	}
	    	
	    	// Update bacteria particles
	    	if(enemies.size() > 0) {
		    	for(int i = 0;i<enemies.size(); i++) {
		    		(((Enemy) enemies.get(i)).getParticles()).update(delta);
		    	}
	    	}
	    	
	    	// Update ship particles
	    	ship.getParticles().update(delta);
	    	
	    	// Update ship position, and it's hitbox's position
	    	ship.setX(ship.getX()+ship.getVelX());
	    	ship.setY(ship.getY()+ship.getVelY());
	    	ship.setHitbox(new Rectangle(ship.getX(),ship.getY(),50,50));
	    	ship.getSprite().setRotation(ship.getSprite().getRotation()+1.5f);
	    	
	    	// Go through bullets, update the values
	    	if(bullets.size() > 0) {
	    		for(int i = 0;i<bullets.size(); i++) {
	    			Bullet current = ((Bullet) bullets.get(i));
	    			current.setX(current.getX()+current.getVelX());
	    			current.setY(current.getY()+current.getVelY());
	    			current.setHitbox(new Rectangle(current.getX(),current.getY(),5,5));
	    			if(current.getX() < 0 || current.getX() > container.getWidth() || current.getY() < 0 || current.getY() > container.getHeight()) {
	    				bullets.remove(i);
	    			}
	    			// Check if the bullet's hit any bacteria
	    			for(int n = 0; n<enemies.size(); n++) {
	    				Enemy currentEnemy = (Enemy) enemies.get(n);
		    			if(current.getHitbox().intersects(currentEnemy.getHitbox())) {
		    				currentEnemy.setHP(currentEnemy.getHP()-20);
		    				bullets.remove(i);
		    				
		    				soundExplosion.play();
		    			
		    				// Add small explosion for the bullet
		    				Entity newExplosion = new Entity();
		    				newExplosion.setX(current.getX());
		    				newExplosion.setY(current.getY());
		    				newExplosion.setParticles("data/bulletExplosion.xml");
		    				((ConfigurableEmitter) newExplosion.getParticles().getEmitter(0)).setPosition(current.getX(),current.getY());
		    				explosions.add(newExplosion);
		    			}
	    			}
	    		}
	    	}
	    	
	    	// Update the values of the bacteria
	    	if(enemies.size() > 0) {
	    		for(int i = 0;i<enemies.size(); i++) {
	    			Enemy current = ((Enemy) enemies.get(i));
	    			current.setX(current.getX()+current.getVelX());
	    			current.setY(current.getY()+current.getVelY());
	    			current.setHitbox(new Rectangle(current.getX(),current.getY(),50,50));
	    			current.getSprite().setRotation(current.getSprite().getRotation()+1.5f);
	    			if(current.getHP() <= 0) {
	    				enemies.remove(i);
	    				enemiesKilled++;
	    				
	    				Entity newExplosion = new Entity();
	    				newExplosion.setX(current.getX());
	    				newExplosion.setY(current.getY());
	    				newExplosion.setParticles("data/explosion.xml");
	    				((ConfigurableEmitter) newExplosion.getParticles().getEmitter(0)).setPosition(current.getX(),current.getY());
	    				explosions.add(newExplosion);
	    			}
	    			
	    			// If the ship hits a bacterium, delete it and make the ship take some damage
	    			if(current.getHitbox().intersects(ship.getHitbox())) {
	    				ship.setHP(ship.getHP()-25);
	    				enemies.remove(i);
	    				enemiesKilled++;
	    				soundExplosion.play();
	    				
	    				// Explosion wednesday (hello beardlover)
	    				Entity newExplosion = new Entity();
	    				newExplosion.setX(current.getX());
	    				newExplosion.setY(current.getY());
	    				newExplosion.setParticles("data/explosion.xml");
	    				((ConfigurableEmitter) newExplosion.getParticles().getEmitter(0)).setPosition(current.getX(),current.getY());
	    				explosions.add(newExplosion);
	    			}
	    			
	    	    	// Makes sure that the enemies can't go through walls on the X axis
	    	    	float tempfiendeVel = current.getVelX();
	    	    	if(current.getX()<0) { current.setVelX(-(tempfiendeVel/2)); current.setX(current.getX()+1); }
	    	    	if(current.getX()>container.getWidth()-50) { current.setVelX(-(tempfiendeVel/2)); current.setX(current.getX()-1); }
	    	    	
	    	    	// // Makes sure that the enemies can't go through walls on the Y axis
	    	    	tempfiendeVel = current.getVelY();
	    	    	if(current.getY()<0) { current.setVelY(-(tempfiendeVel/2)); current.setY(current.getY()+1); }
	    	    	if(current.getY()>container.getHeight()-50) { current.setVelY(-(tempfiendeVel/2)); current.setY(current.getY()-1); }
	    			
	
	    	    	// Some random (and not so random) movement algorithms for the bacteria
	    	    	int random = (int) (Math.random()*4+1);
	    	    	if(random == 1) {
		    	    	current.setVelX((float)(current.getVelX()+(Math.random()*0.4)+0.065f));
		    	    	current.setVelY((float)(current.getVelY()+(Math.random()*0.4)+0.065f)); 
	    	    	}
	    	    	else if(random == 2){
		    	    	current.setVelX((float)(current.getVelX()+(Math.random()*0.4)+0.065f));
		    	    	current.setVelY((float)(current.getVelY()-(Math.random()*0.4)+0.065f));
	    	    	}
	    	    	else if(random == 3){
		    	    	current.setVelX((float)(current.getVelX()-(Math.random()*0.4)+0.065f));
		    	    	current.setVelY((float)(current.getVelY()+(Math.random()*0.4)+0.065f));
	    	    	} else {
		    	    	current.setVelX((float)(current.getVelX()-(Math.random()*0.4)+0.065f));
		    	    	current.setVelY((float)(current.getVelY()-(Math.random()*0.4)+0.065f));
	    	    	}
	    	    	
	    	    	if(current.getX()<ship.getX()) {
	    	    		current.setVelX(current.getVelX()+0.085f);
	    	    	} else {
	    	    		current.setVelX(current.getVelX()-0.085f);
	    	    	}
	    	    	if(current.getY()<ship.getY()) {
	    	    		current.setVelY(current.getVelY()+0.085f);
	    	    	} else {
	    	    		current.setVelY(current.getVelY()-0.085f);
	    	    	}
	    		}
	    	}
    	}
    	// Check if player is alive
    	if(ship.getHP()>0) {
    		gameState = 1;
    		endTime = System.currentTimeMillis()/1000;
    	}
    	// Dead meat
    	if(ship.getHP()<=0 && gameState == 1) {
    		gameState = 2;
    	}
    	
    	// Set some values for the game over menu, and check if we clicked on any button
    	if(gameState == 2) { // Game over menu
    		
    		// Stop background sound!
    		if(soundBackground.playing()) {
    			soundBackground.stop();
    		}
    		
    		button = new Rectangle(container.getWidth()/2-125,container.getHeight()/2+100,250,50);
    		if(button.contains(input.getMouseX(),input.getMouseY())) {
    			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
    				this.restart(container);
    			}
    		}
    		button2 = new Rectangle(container.getWidth()/2-125,container.getHeight()/2+160,250,50);
    		if(button2.contains(input.getMouseX(),input.getMouseY())) {
    			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
    				gameState = 3;
    			}
    		}
    	}
    	// Set some values, and check if we clicked on the button
    	if(gameState == 3) {
    		if(soundBackground.playing()) {
    			soundBackground.stop();
    		}
    		button2 = new Rectangle(container.getWidth()/2-125,container.getHeight()/2+160,250,50);
    		if(button2.contains(input.getMouseX(),input.getMouseY())) {
    			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
    				this.restart(container);
    			}
    		}
    	}
    	
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
    	
    	// If the game is running
    	if(gameState == 1) {
        	g.drawString("Health: "+ship.getHP(), 10, 50);
        	g.drawString("Bacteria killed: "+enemiesKilled, 10, 65);
        	g.drawString("Timer: "+(endTime-time)/60+" minutes "+(endTime-time)%60+" seconds", 10, 80);
    		
        	// Render the explosions
        	if(explosions.size()>0) {
        		for(int i=0;i<explosions.size();i++) {
        			Entity explosion = (Entity) explosions.get(i);
        			explosion.getParticles().render();
        		}
        	}
        	
        	// Update and render the ship's particles
	        ((ConfigurableEmitter) ship.getParticles().getEmitter(0)).setPosition(ship.getX()+25,ship.getY()+25);
	        ship.getParticles().render();
	    	ship.getSprite().draw(ship.getX(),ship.getY());
	    	
	    	// Render the bullets
	    	if(bullets.size()>0) {
		    	for(int i = 0;i<bullets.size();i++) {
		    		Bullet current = (Bullet) bullets.get(i);
		    		((ConfigurableEmitter) current.getParticles().getEmitter(0)).setPosition(current.getX(),current.getY()); 
		        	current.getParticles().render();
		    	}
	    	}
	
	    	// Render the bacteria
	    	if(enemies.size()>0) {
		    	for(int i = 0;i<enemies.size();i++) {
		    		Enemy current = (Enemy) enemies.get(i);
		    		((ConfigurableEmitter) current.getParticles().getEmitter(0)).setPosition(current.getX()+25,current.getY()+25); 
		        	current.getParticles().render();
		    	}
	    	}
    	}
    	// If we lost the game, show the game over menu
    	if(gameState == 2) {
    		int resultTime = (int) (endTime-time);
    		g.drawString("GAME OVER",container.getWidth()/2-50,container.getHeight()/2-200);
    		g.drawString("You survived for "+resultTime/60+" minutes and "+resultTime%60+" seconds!",container.getWidth()/2-200,container.getHeight()/2);
    		g.drawString("      You managed to kill "+enemiesKilled+" bacteria!",container.getWidth()/2-200,container.getHeight()/2+25);
    		g.draw(button);
    		g.drawString("Play again!",button.getCenterX()-50,button.getCenterY()-10);
    		g.draw(button2);
    		g.drawString("Main menu",button2.getCenterX()-50,button2.getCenterY()-10);
    	}
    	// If we're in the main menu
    	if(gameState == 3) {
    		button2 = new Rectangle(container.getWidth()/2-125,container.getHeight()/2+160,250,50);
    		logo = new Image("data/logo.png");
    		logo.draw(container.getWidth()/2-275,container.getHeight()/2-300);
    		
    		g.drawString("The year is 2047. The Russian Federation has released biological weapons through food.\n" +
    				"  You're a Swedish doctor, and you're specialized in killing the bacteria\n" +
    				"     that floats through the infected's veins. Money is getting tight however,\n" +
    				"           and each person gets only one anti-bacteria module, \n" +
    				"     and your job is now to kill as many bacteria as possible with one.",container.getWidth()/2-380,container.getHeight()/2-100);
    		g.drawString("Steer the anti-bacteria module with WASD. Aim with the mouse, and shoot with left mouse button.",container.getWidth()/2-420,container.getHeight()/2+50);
    		g.draw(button2);
    		g.drawString("Start the game!",button2.getCenterX()-65,button2.getCenterY()-10);
    	}
    }
}