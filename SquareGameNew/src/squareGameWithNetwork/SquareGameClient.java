package squareGameWithNetwork;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import processing.net.*;
import processing.core.PApplet;

public class SquareGameClient extends PApplet
{

	private Client myClient;
	
    private Player thisPlayer;
    private Player otherPlayer;
    
    private ArrayList<Button> buttons;
    
    public static final int MAP_WIDTH = 1000;
    public static final int MAP_HEIGHT = 700;
    
    private boolean gameStarted, gameOver, otherPlayerReady, connectingToServer;
    
    private String whatTheyTyped;
  
    public static void main(String[] args)
    {
        PApplet.main("squareGameWithNetwork.SquareGameClient");
    }

    public void settings()
    {
        size(MAP_WIDTH, MAP_HEIGHT);
    }
    
    public void setup()
    {
    	
    	thisPlayer = new Player(this, (int) (Math.random() * 800), (int) (Math.random() * 600), 50, 50, Color.BLUE);
    	otherPlayer = new Player(this, (int) (Math.random() * 800), (int) (Math.random() * 600), 50, 50, Color.RED);
    	
    	
    	buttons = new ArrayList<Button>();
    	buttons.add(new Button(this, 50, 100, "Sniper"));
        buttons.add(new Button(this, 50, 200, "Athletic"));
        buttons.add(new Button(this, 50, 300, "Sprayer"));
        buttons.add(new Button(this, 250, 100, "Speedy"));
        buttons.add(new Button(this, 250, 200, "Tank"));
        
        whatTheyTyped = "";
        
        gameOver = false;
        otherPlayerReady = false;
        gameStarted = false;
        connectingToServer = true;
    }
    
    public void draw()
    {
    	background(0, 200, 200);
    	
    	if (myClient != null && myClient.available() > 0) {
    		readServerData();
		}
    	
    	if (connectingToServer) {
    		textSize(30);
    		text("Type IP Address To Connect To. Click Space When Done", 50, 50);
    		text(whatTheyTyped, 400, 400);
    	}
    	else if (!gameStarted && !connectingToServer) {
    		if (!thisPlayer.ready) {
        		displayPlayersToChoose();
        	}
        	if (thisPlayer.ready && !gameStarted) {
        		myClient.write("Ready\n");
        		displayWaitingScreen();
        		checkIfGameCanStart();
        	}
    	}
    	else {
    		if (gameOver) {
    			writeData();
    			displayGameOverScreen();
    		}
    		else {
    			writeData();
    			thisPlayer.decreaseTimeUntilNextShot();
        		thisPlayer.move();
        		
        		if (thisPlayer.isShooting && thisPlayer.canShoot) {
        			if (mouseX < thisPlayer.xPos)
        				thisPlayer.shoot(-1);
        			else
        				thisPlayer.shoot(1);
        		}
        		
                moveBullet();
                checkBulletsAndHits();
             
                checkDead();
                drawThisPlayer();
                drawOtherPlayer();
                drawBullets();
        		
    		}
    	}
    }
    
    public void checkDead() {
    	thisPlayer.checkAndSetDead();
    	otherPlayer.checkAndSetDead();
    	if (thisPlayer.dead || otherPlayer.dead)
    		gameOver = true;
    }
	//
	public void checkBulletsAndHits() {
		if (thisPlayer.checkBullets(MAP_WIDTH, 
				otherPlayer.xPos, otherPlayer.yPos, 
				otherPlayer.width, otherPlayer.height)) {
			otherPlayer.hit(thisPlayer.build.bulletDamage);
		}
	}
	
	public void drawBullets() {
		thisPlayer.drawBullets();
	}
	
	public void drawThisPlayer() {
		thisPlayer.drawSelf();
	    thisPlayer.drawHealthBar();
	}
	
	public void drawOtherPlayer() {
		otherPlayer.drawSelf();
	    otherPlayer.drawHealthBar();
	}
	
	
	public void moveBullet() {
		thisPlayer.moveBullets();
	}
	
	public void keyPressed()
	{
		if (connectingToServer) {
			if (keyCode == 32) {
				connectingToServer = false;
				myClient = new Client(this, whatTheyTyped, 8888); //10.13.9.53
			}
			else if (keyCode == 8) {
				whatTheyTyped = whatTheyTyped.substring(0, whatTheyTyped.length() - 1);
			}
			else
				whatTheyTyped += key;
		}
		else if (gameOver && keyCode == 32) {
    		gameOver = false;
            otherPlayerReady = false;
            gameStarted = false;
            otherPlayer = new Player(this, 200, 200, 50, 50, Color.RED);
        	thisPlayer = new Player(this, 400, 400, 50, 50, Color.BLUE);
		}
    	else if (gameStarted) {
    		
    		if (keyCode == 68) //d
    			thisPlayer.r = true;
    		else if (keyCode == 65) //a
    			thisPlayer.l = true;
    		else if (keyCode == 87) //w
    			thisPlayer.u = true;
    		else if (keyCode == 83) //s
    			thisPlayer.d = true;
    	}
	}
	
	public void keyReleased()
	{
		if (!gameOver && gameStarted) {
			if (keyCode == 68) //d
				thisPlayer.r = false;
			else if (keyCode == 65) //a
				thisPlayer.l = false;
			else if (keyCode == 87) //w
				thisPlayer.u = false;
			else if (keyCode == 83) //s
				thisPlayer.d = false;
		}
	}
	
	public void mousePressed() {
		if (!gameOver && gameStarted) {
			thisPlayer.isShooting = true;
		}
	}
	
	public void mouseReleased() {
		if (!gameOver && gameStarted) {
			thisPlayer.isShooting = false;
		}
	}
	
	public void mouseClicked() {
		for (int i = 0; i < buttons.size(); i++)
			if (buttons.get(i).isInside(mouseX, mouseY))
				playerChoosen(i);
	}
	
	public void playerChoosen(int buttonIndex) {
		//speed, bulletSpeed, maxAmmo, maxHealth, bulletDamage, fireTime
		if (buttons.get(buttonIndex).playerType.equals("Sniper"))
			thisPlayer.build = new Build(thisPlayer, 4, 30, 4, 100, 30, 20);
		if (buttons.get(buttonIndex).playerType.equals("Athletic"))
			thisPlayer.build = new Build(thisPlayer, 7, 15, 10, 120, 10, 10);
		if (buttons.get(buttonIndex).playerType.equals("Sprayer"))
			thisPlayer.build = new Build(thisPlayer, 5, 12, 20, 65, 5, 5);
		if (buttons.get(buttonIndex).playerType.equals("Speedy"))
			thisPlayer.build = new Build(thisPlayer, 9, 18, 4, 80, 8, 9);
		if (buttons.get(buttonIndex).playerType.equals("Tank"))
			thisPlayer.build = new Build(thisPlayer, 2, 20, 5, 250, 10, 20);
		
		thisPlayer.ready = true;
			
	}
	
	public void displayPlayersToChoose() {
		fill(0);
		textSize(30);
	
		text("Choose Player", 400, 50);
		
		for (Button button : buttons)
			button.drawSelf();
		
	}
	
	public void displayWaitingScreen() {
		fill(0);
		textSize(30);
	
		text("Waiting For Other Player...", 320, 350);
	}
	
	public void checkIfGameCanStart() {
		if (thisPlayer.ready && otherPlayerReady)
			gameStarted = true;
	}
	
	
	
	public void displayGameOverScreen() {
		textSize(50);
		
		if (!thisPlayer.dead)
			text("You win. Click Space", 100, 400);
		else
			text("You lose. Click Space", 100, 400);
	}
	
	public void displayOtherPlayerBullets(String[] playerBulletData) {
		for (String bulletData : playerBulletData) {
			String[] bulletPos = bulletData.split("/");
			fill(Color.black.getRGB());
			rect(Integer.parseInt(bulletPos[0]), Integer.parseInt(bulletPos[1]), 10, 10);
		}
		
	}
    
    
    
    public void readServerData() {
    	String dataFromServer = myClient.readString();
		String[] dataFromServerSeperated = dataFromServer.split("\n");
		if (dataFromServerSeperated[0].equals("Ready")) {
			otherPlayerReady = true;
		}
		else if (gameStarted && dataFromServerSeperated[0].length() > 2) {
			String[] allData = dataFromServerSeperated[0].split("%");
			String[] playerPositionData = allData[0].split("@");
			String[] playerHealthData = allData[1].split("@");
			if (!allData[2].equals("NULL")) {
				String[] playerBulletData = allData[2].split("@");
				displayOtherPlayerBullets(playerBulletData);
			}
			otherPlayer.xPos = Integer.parseInt(playerPositionData[0]);
			otherPlayer.yPos = Integer.parseInt(playerPositionData[1]);
			if (Integer.parseInt(allData[3]) != -100)
				thisPlayer.health = Integer.parseInt(allData[3]);
			if (otherPlayer.health == -100 || otherPlayer.health >= Integer.parseInt(playerHealthData[0]))
				otherPlayer.health = Integer.parseInt(playerHealthData[0]);
			otherPlayer.maxHealth = Integer.parseInt(playerHealthData[1]);
		}
    }
    
    public void writeData() {
    	String playerPositionData = (int) thisPlayer.xPos + "@" + (int) thisPlayer.yPos;
    	String playerHealthData = thisPlayer.health + "@" + thisPlayer.maxHealth;
    	String playerBulletInfo = thisPlayer.getBulletInfo();
    	String otherPlayerHealth = Integer.toString(otherPlayer.health);
    	myClient.write(playerPositionData + "%" + playerHealthData + "%" + playerBulletInfo + "%" + otherPlayerHealth + "%\n");
    }
}

