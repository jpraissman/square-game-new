package squareGameWithNetwork;
import java.awt.Color;
import java.awt.geom.Point2D;

import processing.core.PApplet;

public class Test extends PApplet
{
	int xPos, yPos;
	boolean aiming = false;
	
	boolean shooting = false;
	
	boolean r, l, u, d;
	
	int speed = 5;
	
	double bulletSpeed = 15;
	
	final double maxBulletSize = 30;
	double bulletSize = 30;
	
	double midBulletXPos, midBulletYPos;
	
	boolean bulletMoving = false;
	
	
	
	double xMove, yMove;
	
  
    public static void main(String[] args)
    {
        PApplet.main("squareGameWithNetwork.Test");
    }

    public void settings()
    {
        size(800, 800);
    }
    
    public void setup()
    {
    	xPos = 200;
    	yPos = 200;
    }
    
    public void draw()
    {
    	background(0, 200, 200);
    	fill(255);
    	
    	if (aiming)
    		drawLine();
    	
    	strokeWeight(1);
    	fill(Color.red.getRGB());
    	rect(xPos, yPos, 50, 50);
    	
    	move();
    	
    	
    	if (shooting)
    		shoot();
    	fill(Color.blue.getRGB());
    	if (bulletMoving) {
    		moveBullet();
    	}
    	if (aiming) {
    		double middleXPos = xPos + 50.0/2;
    		double middleYPos = yPos + 50.0/2;
    		midBulletXPos = mouseX;
    		midBulletYPos = mouseY;
    		double aimDis = Point2D.distance(mouseX, mouseY, middleXPos, middleYPos);
    		bulletSize = (125/aimDis) * maxBulletSize;
    		if (bulletSize > maxBulletSize)
    			bulletSize = maxBulletSize;
    	}
    	rect((int)(midBulletXPos - bulletSize/2), 
    			(int)(midBulletYPos - bulletSize/2), 
    			(int)(bulletSize + 0.5), (int)(bulletSize + 0.5));
    	
    }
    
    private void moveBullet() {
    	midBulletXPos += xMove;
    	midBulletYPos += yMove;
    }
    
    private void shoot() {
    	shooting = false;
    	
    	double middleXPos = xPos + 50.0/2;
		double middleYPos = yPos + 50.0/2;
		
		double xDis = Math.abs(middleXPos - mouseX);
		double yDis = Math.abs(middleYPos - mouseY);
		double totalDis = Math.sqrt((xDis * xDis) + (yDis * yDis));
		
		double aimDis = Point2D.distance((double)mouseX, (double)mouseY, middleXPos, middleYPos);
		
		double newBulletSpeed = (aimDis/200) * bulletSpeed;
		
		
		double numFrames = totalDis/newBulletSpeed;
		
		xMove = xDis/numFrames;
		yMove = yDis/numFrames;
		
		midBulletXPos = middleXPos;
		midBulletYPos = middleYPos;
		
		if (mouseX > middleXPos)
			xMove *= - 1;
		if (mouseY > middleYPos)
			yMove *= -1;
		
	
		
		bulletMoving = true;
		
		
    }
    
    private void drawLine() {
    	int middleX = xPos + 25;
    	int middleY = yPos + 25;
    	strokeWeight(2);
    	line(middleX, middleY, mouseX, mouseY);
    }
    
    private void move() {
    	if (r)
    		xPos += speed;
    	if (l)
    		xPos -= speed;
    	if (u)
    		yPos -= speed;
    	if (d)
    		yPos += speed;
    }
    

	public void mousePressed() {
		aiming = true;
	}
	
	public void keyPressed() {
		if (keyCode == 68) //d
			r = true;
		else if (keyCode == 65) //a
			l = true;
		else if (keyCode == 87) //w
			u = true;
		else if (keyCode == 83) //s
			d = true;
	}
	
	public void keyReleased() {
		if (keyCode == 68) //d
			r = false;
		else if (keyCode == 65) //a
			l = false;
		else if (keyCode == 87) //w
			u = false;
		else if (keyCode == 83) //s
			d = false;
	}
	
	public void mouseReleased() {
		aiming = false;
		shooting = true;
	}
	
	
	
	
	
	
}

