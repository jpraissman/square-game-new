package squareGameWithNetwork;
import processing.core.PApplet;

public class UpdatesTester extends PApplet
{
	int xPos, yPos;
	double middleWeaponX, middleWeaponY;
	double midBulletX, midBulletY;
	double xMove, yMove;
	double bulletSpeed = 10;
	int seperation = 60;
	
	boolean shooting = false;
	
  
    public static void main(String[] args)
    {
        PApplet.main("squareGameWithNetwork.UpdatesTester");
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
    	rect(xPos, yPos, 50, 50);
    	drawWeapon();
    	if (shooting)
    		moveAndDrawBullet();
    }
    
    private void moveAndDrawBullet() {
    	midBulletX += xMove;
    	midBulletY += yMove;
    	
    	fill(0);
    	rect((int) (midBulletX - 5), (int) (midBulletY - 5), 10, 10);
    }
	public void mouseClicked() {
		shoot();
	}
	
	private void shoot() {
		double middleXPos = xPos + 50.0/2;
		double middleYPos = yPos + 50.0/2;
		double midWeaponX = middleWeaponX + middleXPos;
		double midWeaponY = middleWeaponY + middleXPos;
		
		double xDis = Math.abs(midWeaponX - mouseX);
		double yDis = Math.abs(midWeaponY - mouseY);
		double totalDis = Math.sqrt((xDis * xDis) + (yDis * yDis));
		double numFrames = totalDis/bulletSpeed;
		
		xMove = xDis/numFrames;
		yMove = yDis/numFrames;
		
		
		
		if (mouseX < middleXPos)
			xMove *= - 1;
		if (mouseY < middleYPos)
			yMove *= -1;
		
		
		midBulletX = midWeaponX;
		midBulletY = midWeaponY;
		shooting = true;
	
	}
	
	private void drawWeapon() {
		double middleXPos = xPos + 50.0/2;
		double middleYPos = yPos + 50.0/2;
		double m = ((double) (mouseY - middleYPos))/(mouseX - middleXPos);
		double num = seperation * seperation;
		double denom = 1 + (m * m);
		middleWeaponX = Math.sqrt(num/denom);
		middleWeaponY = Math.abs(m) * middleWeaponX;
		if (middleXPos > mouseX)
			middleWeaponX *= -1;
		if (middleYPos > mouseY)
			middleWeaponY *= -1;
		int weaponX = (int) (middleWeaponX + middleXPos) - 5;
		int weaponY = (int) (middleWeaponY + middleYPos) - 5;
		fill(0);
		rect(weaponX, weaponY, 10, 10);
	}
	
	
}

