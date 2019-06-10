package mainpack;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import mainpack.Physics;

public class BoardMatrix {
	private Physics physics = new Physics();
	Random random = new Random();
	
	private int matrix[];
	private int mapType = 0;
	
	static final Color DIRT = new Color(60, 38, 12);
	static final Color GRASS = new Color(0, 110, 0);
	static final Color SNOW = new Color(224, 254, 254);
	static final Color ICE = new Color(206, 253, 253);
	static final Color SAND = new Color(254, 232, 107);
	static final Color SANDSTONE = new Color(254, 238, 148);
	static final Color STONE = new Color(200, 200, 200);
	static final Color ROCK = new Color(164, 164, 164);
	static final Color ASPHALT = new Color(129, 129, 129);
	static final Color CONCRETE = new Color(155, 155, 155);
	
	private Color top;
	private Color ground;
	
	final int SMOOTH = 50;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 649;
	
	private void makeForest() {
		for (int i=0;i<WIDTH;++i){
			matrix[i] = minimal(terrainGenerator(i));
		}
		for (int i=0;i<SMOOTH;++i)
			terrainSmooth();
		terrainJerk();
		terrainSmooth();
	}
	private void makeDesert() {
		for (int i=0;i<WIDTH;++i){
			matrix[i] = minimal(terrainGenerator(i));
		}
		for (int i=0;i<SMOOTH*2;++i)
			terrainSmooth();
		terrainJerk();
		terrainSmooth();
	}
	private void makeCity() {
		for (int i=0;i<WIDTH;++i)
			matrix[i] = 250;
	}
	
	public BoardMatrix(int type){
		if (type == -1)
			mapType = getType();
		else
			mapType = type;
		chooseGroundColors();
		matrix = new int[WIDTH];
		if (mapType == 0 || mapType == 2 || mapType == 3)
			makeForest();
		else if (mapType == 1)
			makeDesert();
		else if (mapType == 4)
			makeCity();
	}
	
	public void drawBoard(Graphics graphics, double windForce){
		Graphics2D graphics2d = (Graphics2D)graphics;
		graphics2d.setColor(ground);
		for (int i=0;i<WIDTH;++i){
				graphics2d.drawLine(i, 649-matrix[i]-2, i, 649);
		}
		graphics2d.setColor(top);
		for (int i=0;i<WIDTH;++i)
			graphics2d.drawLine(i, 649-matrix[i], i, 649-matrix[i]-2);

		graphics = graphics2d.create();
		physics.calcGravity(matrix);
		physics.calcColisions(matrix);
		physics.drawEntities(graphics, windForce);
	}
	
	public void explosion(int x, int y, float radius) {
		physics.explosion(x, y, radius, matrix, ground);
	}
	
	public int[] getBoard() {
		return matrix;
	}
	public int getMapType() {
		return mapType;
	}
	public void setBoard(int board[], int type) {
		this.matrix = board;
		this.mapType = type;
		chooseGroundColors();
	}
	
	//###################################################################################
	private int terrainGenerator(int x) {
		if (x == 0){
			return random.nextInt(200) + 150;
		}else if ((x&1) == 0 || (x%5) == 0 || (x%7) == 0 || (x%11) == 0){
			return matrix[x-1];
		}else{
			return matrix[x-1] + random.nextInt(37) - 18;
		}
	}
	private int minimal(int x) {
		if (x < 50)
			return 50;
		if (x > 600)
			return 600;
		return x;
	}
	private void terrainSmooth() {
		int clone[] = matrix.clone();
		for (int i=2;i<matrix.length-2;++i){
			matrix[i] = (clone[i-2] + clone[i-1] + clone[i] + clone[i+1] + clone[i+2]) / 5;
		}
	}
	private void terrainJerk() {
		for (int i=1;i<matrix.length-1;++i){
			matrix[i] += random.nextInt(3);
		}
	}
	//###################################################################################
	
	private int getType() {
		int rand = random.nextInt(100);
		int[] chances = {20,20,20,20,20};
		int tmp = 0;
		for (int i=0;i<chances.length;++i){
			tmp += chances[i];
			if (tmp >= rand)
				return i;
		}
		return 0;
	}
	private void chooseGroundColors() {
		switch (mapType) {
		case 0:
			top = GRASS;
			ground = DIRT;
			break;
		case 1:
			top = SAND;
			ground = SANDSTONE;
			break;
		case 2:
			top = SNOW;
			ground = ICE;
			break;
		case 3:
			top = STONE;
			ground = ROCK;
			break;
		case 4:
			top = ASPHALT;
			ground = CONCRETE;
		}
	}
}
