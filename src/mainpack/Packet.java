package mainpack;

import java.io.Serializable;

public class Packet implements Serializable{
	private static final long serialVersionUID = -950915242107123268L;

	///TANK PROPERTIES
	public String playerName;
	public double x,y;
	public int cannonAngle = -90;
	public int cannonForce = 50;
	public int choosenGun = 1;
	
	///BOARD PROPERTIES
	public int board[];
	public int background;
	public double windForce;
	
	///WHAT HAPPENED
	public boolean shot;
	public boolean tankMoved;
	public boolean mapGeneration;
	public boolean gameEnd;
	public boolean roundEnd = false;
	public boolean enemyDisconnected = false;
	public boolean timedOut = false;
	
	public Packet(int gunType) {
		/// FOR SHOTS
		choosenGun = gunType;
		roundEnd = true;
		gameEnd = false;
	}
	public Packet(double x, double y, int angle, int force) {
		/// FOR MOVING
		this.x = x;
		this.y = y;
		cannonAngle = angle;
		cannonForce = force;
		gameEnd = false;
	}
	public Packet(int board[], int type, double windForce, String playerName, boolean timedOut) {
		/// FOR INITIATION
		this.windForce = windForce; 
		this.playerName = playerName;
		this.background = type;
		this.board = board.clone();
		this.timedOut = timedOut;
		gameEnd = false;
	}
	public Packet() {
		/// FOR TANKING TANK DATA
		gameEnd = false;
	}
}
