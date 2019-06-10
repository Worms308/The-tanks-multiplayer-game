package mainpack;

import java.awt.Color;
import java.awt.Graphics;

public class FallGround {
	public int size;
	private int x;
	private double speed = 1;
	private Color groundColor;
	double y;
	
	public FallGround(int x, int groundLevel, int size, Color color) {
		this.size = size;
		this.x = x;
		y = groundLevel - size;
		groundColor = color;
	}
	
	public void gravity() {
		y = y - (speed += 0.5);
	}
	
	public void drawGround(Graphics grap) {
		grap.setColor(groundColor);
		grap.drawLine(x, 649 - (int)y, x, 649 - (int)y - size);
	}
	
	public void addToBoard(int board[]) {
		//System.out.println(board[x] + " " + ((int)y));
		if (board[x] >= (int)y){
			board[x] += size;
			size = 0;
		}
	}
}
