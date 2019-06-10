package mainpack;
import java.awt.Color;
import java.awt.Graphics;

public class Entity {
	private double lifeTime = 255;
	private int x,y;
	private float deltaX,deltaY;
	private float speed;
	private float time = 1.0f;
	
	private final float gravityForce = 9.80665f; 
	private final float mass = 0.075f; 
	private final float windFactor = 2.5f; 
	public boolean move(float windForce) {
		lifeTime /= 1.02;
		
		float forceY = deltaY + gravityForce*mass;
		float forceX = deltaX + windForce*windFactor;
		
		deltaY = forceY * time;
		deltaX = forceX * time;
		
		x += deltaX;
		y += deltaY;
		
		if (x > 1274 || x < 0 || y > 649)
			return true;
		return false;
	}
	
	public Entity(int x, int y, int explosionX, int explosionY, float radius) {
		this.x = x;
		this.y = y;
		deltaX = explosionX - x;
		deltaY = explosionY - y;
		speed =  radius / (float)Math.sqrt(Math.pow(explosionX - x, 2) + Math.pow(explosionY - y, 2));
		speed /= 2;
		deltaX *= speed*1;
		deltaY *= speed*1;
	}
	
	
	void removeFromBoard() {
		x = -10000;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public void drawEntity(Graphics graphics) {
		graphics.setColor(new Color(255, 0, 0, (int)lifeTime));
		graphics.drawRect(x, y, 2, 2);
	}
}
