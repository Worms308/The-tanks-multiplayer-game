package mainpack;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import mainpack.Entity;

public class Physics {
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private Random random = new Random();
	private ArrayList<FallGround> grounds = new ArrayList<FallGround>();
	
	public void calcGravity(int board[]) {
		for (int i=0;i<grounds.size();++i){
			grounds.get(i).gravity();
			grounds.get(i).addToBoard(board);
			if (grounds.get(i).size == 0 || grounds.get(i).y < 0){
				 grounds.remove(i);
				 --i;
			}
		}
	}
	public void calcColisions(int board[]) {
		for (int i=0;i<entities.size();++i){
			if (board[Math.abs(entities.get(i).getX()-1)] > 649 - entities.get(i).getY()){
				entities.get(i).removeFromBoard();
			}
		}
	}

	private double sgn(double val) {
		if (val < 0)
			return 0;
		else
			return val;
	}
	private void makeBank(int board[], int x, int stopX, int lenght) {
		int center = (stopX - x);
		lenght += (stopX - x)/3;
		int centerOfFor = ((x-(lenght/2)) + (x+center+(lenght/2)))/2;
		for (int i=x-(lenght/2);i<x+center+(lenght/2);++i){
			if (centerOfFor - i != 0)
				board[i] += sgn((lenght/3) - Math.abs((double)i - centerOfFor)/3.0);
			else
				board[i] += (lenght/3);
		}
	}
	public void explosion(int x, int y, float radius, int board[], Color groundColor) {
		y -= radius/1.7;
		int startX = x - (int)radius, stopX = x + (int)radius;
		int startY = y - (int)radius, stopY = y + (int)radius;

		int tmp[] = new int[board.length];
		Arrays.fill(tmp, -1);
		
		System.out.println(board[x] + " " + (649-y));
		if (board[x] + (radius) > 649-y)
			makeBank(board, startX, stopX, (int) radius);
		
		for (int i=startX;i<stopX;++i){
			for (int j=startY;j<stopY;++j){
				 float lenght = (float) Math.sqrt(Math.pow(x - i, 2) + Math.pow(y - j, 2));
				 if (lenght <= radius && tmp[i] < j)
					 tmp[i] = j;
			}
		}
		
		for(int i=0;i<tmp.length;++i){
			if (tmp[i] != -1 && board[i] > 649 - tmp[i]){
				//System.out.println(board[i] + " " + (649-y) + " " + (tmp[i]-y));
				if (board[i] > (649-y) + (tmp[i]-y)){
					grounds.add(new FallGround(i, board[i], Math.abs((649-y) + (tmp[i]-y) - board[i]), groundColor));
				}
				board[i] = 649 - tmp[i];
			}
		}
		
		int explosionEntities =  random.nextInt(50) + 80;
		for (int i=0;i<explosionEntities;++i){
			entities.add(new Entity(random.nextInt(stopX - startX) + startX, random.nextInt(stopY - startY) + startY + 30, x, y, radius));
		}
	}
	
	public void drawEntities(Graphics graphics, double windForce) {
		for (int i=0;i<entities.size();++i){
			if (entities.get(i).move((float)windForce)){
				entities.remove(i);
				--i;
				continue;
			}
			entities.get(i).drawEntity(graphics);
		}
		for (int i=0;i<grounds.size();++i)
			grounds.get(i).drawGround(graphics);
	}
}
