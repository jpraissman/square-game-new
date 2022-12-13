package squareGameWithNetwork;

import java.util.ArrayList;

public class Build {
		
	public int speed, bulletSpeed ,maxAmmo, bulletDamage, maxHealth, fireTime;
	
	private Player player;

	public Build(Player player, int speed, int bulletSpeed,
			int maxAmmo, int maxHealth, int bulletDamage, int fireTime) {
		this.speed = speed;
		this.bulletSpeed = bulletSpeed;
		
		this.maxAmmo = maxAmmo;
		
		this.maxHealth = maxHealth;
		this.bulletDamage = bulletDamage;
		this.fireTime = fireTime;
		
		this.player = player;
		this.player.maxHealth = maxHealth;
		this.player.health = maxHealth;
		this.player.curAmmo = maxAmmo;
		
	}
}
