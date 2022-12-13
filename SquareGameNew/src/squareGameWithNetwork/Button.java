package squareGameWithNetwork;

import processing.core.PApplet;

public class Button
{
	public static final int WIDTH = 75, HEIGHT = 25;
	
	private PApplet parent;
	private int topLeftX, topLeftY;
	public String playerType;
	
	public Button(PApplet parent, int tlx, int tly, String playerType)
	{
		this.parent = parent;
		topLeftX = tlx;
		topLeftY = tly;
		this.playerType = playerType;
	}
	
	public void drawSelf()
	{
		parent.text(playerType, topLeftX, topLeftY - 10);
		parent.rect(topLeftX, topLeftY, WIDTH, HEIGHT);
	}
	
	public boolean isInside(int x, int y)
	{
		return x >= topLeftX && x <= topLeftX + WIDTH &&
				y >= topLeftY && y <= topLeftY + HEIGHT;
	}
}

