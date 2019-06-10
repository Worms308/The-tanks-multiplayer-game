package mainpack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

public class Tank{
	private String playerName;
	private String texturePath;
	private double health;
	private double x,y;
	private int cannonAngle = -90;
	private int cannonForce = 50;
	private int choosenGun = 0;
	private int[] ammo = {99999,3,1};
	
	private double dy;
	private boolean canMove;
	private int fuel;
	private boolean dead;
	
	private int animation;
	private int drawHp;
	private int board[];
	private Image txt1,txt2,txt3;
	private Image cannon;
	private Image fuelIcon;
	private Image healthIcon;
	private Bullet bullet;
	
	public Tank(int x, int y, String player, String texture, int board[]) {
		this.x = x;
		this.y = y;
		this.board = board;
		animation = 0;
		texturePath = texture;
		playerName = player;
		health = 100.0;
		drawHp = 60;
		fuel = 80;
		dead = false;
		
		txt1 = Toolkit.getDefaultToolkit().createImage("graphics//" + texturePath + "1Tank.png");
		txt2 = Toolkit.getDefaultToolkit().createImage("graphics//" + texturePath + "2Tank.png");
		txt3 = Toolkit.getDefaultToolkit().createImage("graphics//" + texturePath + "3Tank.png");
		fuelIcon = Toolkit.getDefaultToolkit().createImage("graphics//fuel.png");
		healthIcon = Toolkit.getDefaultToolkit().createImage("graphics//health.png");
		cannon = Toolkit.getDefaultToolkit().createImage("graphics//lufa.png");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	public int[] getAmmo() {
		return ammo.clone();
	}
	public void setPacket(Packet packet) {
		if (packet.tankMoved){
			if (x != packet.x || y != packet.y)
				SoundManager.playSound("tankStarts");
			x = packet.x;
			y = packet.y;
			cannonAngle = packet.cannonAngle;
			cannonForce = packet.cannonForce;
			choosenGun = packet.choosenGun;
		}else if (packet.shot){
			choosenGun = packet.choosenGun;
			shot();
		}
	}
	
	public void setName(String newName) {
		this.playerName = newName;
	}
	
	public void setBoardGround(int board[]) {
		this.board = board;
	}
	
	public void getData(Packet packet) {
		packet.cannonAngle = cannonAngle;
		packet.cannonForce = cannonForce;
		packet.choosenGun = choosenGun;
		packet.playerName = playerName;
		packet.x = x;
		packet.y = y;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	public int getFuel() {
		return fuel;
	}
	
	public void addNewFuel() {
		fuel = 80;
	}
	public void changeAngle(int newAngle) {
		cannonAngle = newAngle;
	}
	public void changeForce(int newForce) {
		cannonForce = newForce;
	}
	public boolean changeWeapon(int newCannon) {
		if (ammo[newCannon] > 0){
			choosenGun = newCannon;
			return true;
		}
		return false;
	}
	
	public boolean shot() {
		if (!dead && ammo[choosenGun] > 0){
			SoundManager.playSound("tankShot");
			ammo[choosenGun]--;
			bullet = new Bullet((int)x, (int)y - 26, cannonAngle, cannonForce, playerName, choosenGun);
			return true;
		}
		return false;
	}
	
	public void killYourself() {
		dead = true;
	}
	
	public boolean isAlive() {
		return !dead;
	}
	
	public boolean takeExplosionDamage(int x, int y, double radius, double maxDamage) {
		if (dead)
			return false;
		drawHp = 100;
		double power = Math.sqrt(Math.pow(this.x - x, 2) + Math.pow((this.y-15) - y, 2));
		if (radius >= power){
			health -= maxDamage/(1/(radius/power));
			System.out.println(health);
			if (health <= 0){
				health = 0.0;
				return true;
			}
		}
		return false;
	}
	
	public void gravity() {
		if (dead)
			return;
		if (649 - board[(int)x] > y){
			y += (dy += 0.5);
			if (y > 649)
				dead = true;
			canMove = false;
		}else{
			dy = 0.0;
			canMove = true;
		}
		if (649 - board[(int)x] < y){
			y = 649 - board[(int)x];
		}
	}
	
	public void move(boolean left) {
		if (!canMove || dead || fuel <= 0)
			return;

		SoundManager.playSound("tankStarts");
		--fuel;
		
		if (fuel == 0){
			SoundManager.stopSound("tankStarts");
			SoundManager.playSound("tankStops");
		}
		
		if (left){
			if (x > 50){
				if (board[(int)x-1] < board[(int)x]+3){
					x -= 0.8;
					animation--;
					if (animation < 0)
						animation = 2;
					if (649 - board[(int)x] > y)
						y = 649 - board[(int)x];
				}
			}
		}else{
			if (x < 1220){
				if (board[(int)x+1] < board[(int)x]+3){
					x += 0.8;
					animation++;
					if (animation > 2)
						animation = 0;
					if (649 - board[(int)x] > y)
						y = 649 - board[(int)x];
				}
			}
		}
	}
	
	private boolean hit(int tx, int ty, int bx, int by) {
		if (bx > tx-25 && bx < tx+29)
			if (by > ty-20 && by < ty)
				return true;
		return false;
	}
	public int[] calcBullets(double windForce, Tank enemy) {
		if (bullet != null){
			if (bullet.move(windForce)){
				bullet = null;
				return null;
			}
			if (hit((int)x, (int)y, bullet.getX(), bullet.getY())){
				int result[] = new int[4];
				result[0] = bullet.getX();
				result[1] = bullet.getY();
				result[2] = (int)bullet.getRadius();
				result[3] = (int)bullet.getDamage();
				bullet = null;
				return result;
			}
			if (hit((int)enemy.x, (int)enemy.y, bullet.getX(), bullet.getY())){
				int result[] = new int[4];
				result[0] = bullet.getX();
				result[1] = bullet.getY();
				result[2] = (int)bullet.getRadius();
				result[3] = (int)bullet.getDamage();
				bullet = null;
				return result;
			}
			if (board[Math.abs(bullet.getX()-1)] > 649 - bullet.getY()){
				int result[] = new int[4];
				result[0] = bullet.getX();
				result[1] = bullet.getY();
				result[2] = (int)bullet.getRadius();
				result[3] = (int)bullet.getDamage();
				bullet = null;
				return result;
			}
		}
		return null;
	}
	
	public void drawStat(Graphics graphics) {
		graphics.drawImage(fuelIcon, 25, 20, null);
		graphics.setColor(Color.BLACK);
		graphics.fillRect(70, 31, 70, 10);
		graphics.setColor(Color.GREEN.darker());
		graphics.fillRect(71, 32, (int) (68 * fuel/80), 8);
		///
		graphics.drawImage(healthIcon, 25, 52, null);
		graphics.setColor(Color.BLACK);
		graphics.fillRect(70, 71, 70, 10);
		graphics.setColor(Color.RED);
		graphics.fillRect(71, 72, (int) (68 * health/100), 8);
		///
		graphics.setColor(Color.GREEN);
		graphics.setFont(new Font("OCR A Extended", Font.PLAIN, 15));
	}

	public void draw(Graphics graphics) {
		if (dead)
			return;
		
		SoundManager.stopSound("tankStarts");
		SoundManager.stop--;
		
		Graphics2D tmpGraphics = (Graphics2D)graphics;

		int localXPos = (int)x - 34;
		int localYPos = (int)y - 76;
		drawHp--;
		//graphics.drawRect((int)x-25, (int)y-20, 54, 20);
		//Maska kolizji
		tmpGraphics.setFont(new Font("OCR A Extended", Font.BOLD, 15));
		tmpGraphics.setColor(new Color(40, 255, 40));
		tmpGraphics.drawString(playerName, (int)x - (int)(playerName.length()*3.8), (int)y - 40);
		
		if (drawHp > 0){
			tmpGraphics.setColor(new Color(255, 0, 0));
			tmpGraphics.fillRect((int)x - 33, (int)y - 37, 70, 4);
			
			tmpGraphics.setColor(new Color(0, 255, 0));
			tmpGraphics.fillRect((int)x - 33, (int)y - 37, (int)(70 * (health/100.0)), 4);
		}
		
		if (bullet != null){
			bullet.draw(graphics);
		}

		
		AffineTransform backup = tmpGraphics.getTransform();
		double rotationRequired = Math.toRadians(cannonAngle);
		tmpGraphics.rotate(rotationRequired,localXPos+ 32, localYPos+ 52);
		tmpGraphics.translate(0, 0);
		tmpGraphics.drawImage(cannon, localXPos + 32, localYPos + 50, null);
		tmpGraphics.setTransform(backup);
		
		switch (animation) {
		case 0:
			tmpGraphics.drawImage(txt1, localXPos, localYPos, null);
			break;
		case 1:
			tmpGraphics.drawImage(txt2, localXPos, localYPos, null);
			break;
		case 2:
			tmpGraphics.drawImage(txt3, localXPos, localYPos, null);
			break;
		default:
			tmpGraphics.drawImage(txt1, localXPos, localYPos, null);
			break;
		}
	}
}
