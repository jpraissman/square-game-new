package squareGameWithNetwork;

import java.awt.Color;
import java.util.ArrayList;

import processing.core.PApplet;

public class Player {

	public boolean inLobby, ready;
	
	public Build build;
	public Color color;
	
	private PApplet window;
	
	public boolean r, l, u, d, dead, isShooting, canShoot;
	
	public int xPos, yPos, width, height;
	public int health, maxHealth;
	public int curAmmo;
	
	public int timeUntilNextShot, timeUntilReload;
	
	public ArrayList<Bullet> bullets;
	
	public Player(PApplet window, int x, int y, int w, int h, Color color) {
		this.window = window;
		this.color = color;
		
		xPos = x;
		yPos = y;
		width = w;
		height = h;
		
		dead = false;
		
		health = -100;
		
		bullets = new ArrayList<Bullet>();
	}
	
	public void drawSelf() {
		window.fill(color.getRGB());
		window.rect(xPos, yPos, width, height);
	}
	
	public void drawHealthBar() {
		window.fill(Color.RED.getRGB());
    	window.rect(xPos, yPos - 15, width, 10);
    	
    	window.fill(Color.GREEN.getRGB());
    	window.rect(xPos, yPos - 15, 
    			(int)(width * ((double) health/maxHealth)), 10);
	}
	
	public void move() {
		if (r)
    		xPos += build.speed;
    	if (l)
    		xPos -= build.speed;
    	if (u)
    		yPos -= build.speed;
    	if (d)
    		yPos += build.speed;
    	
    	if (xPos < 0)
    		xPos = 0;
    	if (xPos > SquareGameServer.MAP_WIDTH - width)
    		xPos = SquareGameServer.MAP_WIDTH - width;
    	
    	if (yPos < 0)
    		yPos = 0;
    	if (yPos > SquareGameServer.MAP_HEIGHT - height)
    		yPos = SquareGameServer.MAP_HEIGHT - height;
	}
	
	public void checkAndSetDead() {
		if (health <= 0 && health != -100) {
			dead = true;
//			System.out.println("Here");
		}
			
			
	}
	
	public void hit(int damage) {
		health -= damage;
	}
	
	public void moveBullets() {
		for (Bullet bullet : bullets)
			bullet.move();
	}
	
	public void shoot(int direction) {
		if (curAmmo > 0) {
			
			double middleXPos = xPos + 50.0/2;
			double middleYPos = yPos + 50.0/2;
			
			double xDis = Math.abs(middleXPos - window.mouseX);
			double yDis = Math.abs(middleYPos - window.mouseY);
			double totalDis = Math.sqrt((xDis * xDis) + (yDis * yDis));
			double numFrames = totalDis/build.bulletSpeed;
			
			double xMove = xDis/numFrames;
			double yMove = yDis/numFrames;
			
			
			if (window.mouseX < middleXPos)
				xMove *= - 1;
			if (window.mouseY < middleYPos)
				yMove *= -1;
			
			
			bullets.add(new Bullet(window, this, Color.BLACK,
					middleXPos, middleYPos, xMove, yMove, build.bulletDamage));
			curAmmo--;
			canShoot = false;
			timeUntilNextShot = build.fireTime;
		}
	}
	
	
	
	public void decreaseTimeUntilNextShot() {
		timeUntilNextShot--;
		if (timeUntilNextShot <= 0)
			canShoot = true;
	}
	
	
	/**
	 * 
	 * @param mapWidth
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return true if bullet collides with given parameters
	 */
	public boolean checkBullets(int mapWidth, int x, int y, int w, int h) {
		boolean hit = false;
		for (int i = 0; i < bullets.size(); i++) {
//			Remove bullet if outside of arena
			if (bullets.get(i).midXPos < - 25 || bullets.get(i).midXPos > mapWidth + 25 || 
					bullets.get(i).midYPos < - 25 || bullets.get(i).midYPos > 800 + 25) {
				bullets.remove(i);
				i--;
				curAmmo++;
			}
//			Remove bullet if hit given bounds
			else if (bullets.get(i).collision(x, y, w, h)) {
				bullets.remove(i);
				i--;
				curAmmo++;
				hit = true;
			}
					
		}
		
		return hit;
	}
	
	public void drawBullets() {
		for (Bullet bullet : bullets)
			bullet.drawSelf();
	}
	
	
	
	/**
	 * Each bullet is seperated by '@'and each number seperated by '#'
	 * @return formatted String
	 */
	public String getBulletInfo() {
		String result = "";
		for (Bullet bullet : bullets) {
			result += (int) bullet.midXPos + "/" + (int) bullet.midYPos + "@";
		}
		
		if (result.length() == 0)
			result = "NULL";
		return result;
	}
}
