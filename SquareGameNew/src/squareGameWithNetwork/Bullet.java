package squareGameWithNetwork;

import java.awt.Color;

import processing.core.PApplet;

public class Bullet {

	public int damage;
	double xMove, yMove, midXPos, midYPos;
	public Player player;
	PApplet window;
	Color color;
	
	public Bullet(PApplet window, Player player, Color color, double midXPos, 
			double midYPos, double xMove, double yMove, int damage) {
		
		this.player = player;
		this.midXPos = midXPos;
		this.midYPos = midYPos;
		this.xMove = xMove;
		this.yMove = yMove;
		this.damage = damage;
		
		this.window = window;
		this.color = color;
	}
	
	public void move() {
		midXPos += xMove;
		midYPos += yMove;
	}
	
	public boolean collision(int x, int y, int w, int h) {
		if (midXPos >= x && midXPos <= x + w)
			if (midYPos >= y && midYPos <= y + h)
				return true;
		
		return false;
	}
	
	public void drawSelf() {
		window.fill(color.getRGB());
		window.rect((int) (midXPos - 4.5), (int) (midYPos - 4.5), 10, 10);
	}
}
