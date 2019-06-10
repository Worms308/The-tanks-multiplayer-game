package mainpack;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Bullet{
	private static Image texture;
	static {
		texture = Toolkit.getDefaultToolkit().getImage("graphics//Bullet.png");
	}
	
	private double damage;
	private double explosionRadius;
	private int x,y;
	private double dx,dy;
	private double speed;
	private double windForce = 10;
	
	
	private double getX(double angle) {
		return ((angle/180) * 2) + 1;
	}
	private double getY(double angle) {
		double toCos = ((angle/180) * 2) + 1;
		toCos = toCos * Math.PI/2;
		return -Math.cos(toCos);
	}
	public Bullet(int x, int y, double angle, double force, String owner, int type) {
		this.x = x;
		this.y = y;
		dx = getX(angle);
		dy = getY(angle);
		speed = force/2;
		dx *= speed;
		dy *= speed;
		
		switch (type) {
		case 1:
			damage = 16;
			explosionRadius = 40;
			break;
		case 2:
			damage = 20;
			explosionRadius = 80;
			break;
		case 3:
			damage = 30;
			explosionRadius = 150;
			break;
		default:
			damage = 5;
			explosionRadius = 30;
			break;
		}
	}
	
	public double getDamage() {
		return damage;
	}
	
	public double getRadius() {
		return explosionRadius;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public boolean move(double windForce) {
		if (this.windForce == 10){
			this.windForce = windForce;
		}
		dx /= 1.02;
		dy /= 1.02;
		dy += 0.7f;
		x += dx + this.windForce;
		y += dy;
		if (x > 1274 || x < 0 || y > 649)
			return true;
		return false;
	}
	
	public void draw(Graphics grap) {
		grap.setColor(new Color(255, 0, 0));
		//grap.fillRect(x, y, 3, 3);
		grap.drawImage(texture, x-5, y-5, null);
	}
}
