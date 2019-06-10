package mainpack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

import javax.swing.JPanel;

public class GameBoard extends JPanel{
	private static final long serialVersionUID = -2531130647575060196L;
	public int turn = 25;
	private Image background[];
	private Image errorScreen;
	private BoardMatrix board;
	private double newWindForce = 0;
	private double oldWindForce = 0;
	private Tank tank;
	private Tank enemyTank;
	private boolean playerRound;
	
	private Socket localSocket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private Packet lastPacket;
	
	private Thread netThread;
	private boolean netShouldWork = true;
	private boolean isConnected = true;
	
	private double lastDamage;
	
	private int turnTime = 0;
	
	public GameBoard(){
		background = new Image[5];
		background[0] = Toolkit.getDefaultToolkit().createImage("graphics//background1.png");
		background[1] = Toolkit.getDefaultToolkit().createImage("graphics//background2.png");
		background[2] = Toolkit.getDefaultToolkit().createImage("graphics//background3.png");
		background[3] = Toolkit.getDefaultToolkit().createImage("graphics//background4.png");
		background[4] = Toolkit.getDefaultToolkit().createImage("graphics//background5.png");
		errorScreen = Toolkit.getDefaultToolkit().createImage("graphics//error.png");
		for (int i=0;i<background.length;++i)
			background[i].getWidth(null);

		board = new BoardMatrix(-1);	
		netThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(netShouldWork){
					receivePacket();
				}
		}});
	}
	
	public void changeMap(int type) {
		board = new BoardMatrix(type);
	}
	
	public int[] getTankAmmo() {
		return tank.getAmmo();
	}
	
	public void addTime() {
		if (tank != null && enemyTank != null)
			if (tank.isAlive() && enemyTank.isAlive())
				turnTime++;
	}
	
	public void disconnect() {
		try {
			isConnected = false;
			if (localSocket != null)
				localSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connectToServer(String ip, String nickname){
		if (nickname.equals(""))
			nickname = "Lazybones";
		try {
			localSocket = new Socket(ip, Serwer.PORT);
			localSocket.setSoTimeout(10000);
			objectInputStream = new ObjectInputStream(localSocket.getInputStream());
			objectOutputStream = new ObjectOutputStream(localSocket.getOutputStream());
			String message = (String)objectInputStream.readObject();
			playerRound = false;
			TurnInfo.enemy();
			if (message.equals(new String("Waiting for second player!"))){ ///pierwszy gracz
				System.out.println(message);
				playerRound = true;
				TurnInfo.your();
				randWindForce();
				lastPacket = new Packet(board.getBoard(), board.getMapType(), newWindForce, nickname, false);
				objectOutputStream.writeObject(lastPacket);
				message = (String)objectInputStream.readObject();
				tank = new Tank(300, 100, nickname, "green", board.getBoard());
				enemyTank = new Tank(1000, 200, "", "green", board.getBoard());
			}else{ /// drugi gracz
				System.out.println(message);
				lastPacket = (Packet)objectInputStream.readObject();
				board.setBoard(lastPacket.board, lastPacket.background);
				message = (String)objectInputStream.readObject();
				setWindForce(lastPacket.windForce);
				enemyTank = new Tank(300, 100, lastPacket.playerName, "green", board.getBoard());
				tank = new Tank(1000, 200, nickname, "green", board.getBoard());
			}
			System.out.println(message);
			SoundManager.loopSound("gameMusic");
			localSocket.setSoTimeout(0);
			turnTime = 0;
		} catch (IOException | ClassNotFoundException e) {
			isConnected = false;
			if (e.getMessage().equals("Read timed out")){
				isConnected = false;
				try {
					localSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			System.err.println("Unable to connect to server!");

			SoundManager.playSound("error");
			SoundManager.stopSound("gameMusic");
		}
		netThread.start();
	}
	
	private void packetMoving() {
		enemyTank.setPacket(lastPacket);
		enemyTank.setName(lastPacket.playerName);
	}
	
	public void receivePacket() {
		if (tank == null)
			return;
		try {
			lastPacket = (Packet)objectInputStream.readObject();
			if (lastPacket.enemyDisconnected){
				isConnected = false;
				return;
			}
			if (lastPacket.shot || lastPacket.tankMoved)
				packetMoving();
			if (lastPacket.shot || lastPacket.roundEnd){
				setWindForce(lastPacket.windForce);
				playerRound = true;
				turnTime = 0;
				tank.addNewFuel();
				TurnInfo.your();
			}
			if (lastPacket.gameEnd)
				netShouldWork = false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			netShouldWork = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void timeAction(Graphics graphics){
		if (turnTime > turn + 10 && playerRound == false)
			isConnected = false;
		if (playerRound){
			graphics.setColor(Color.DARK_GRAY);
			graphics.setFont(new Font("OCR A Extended", Font.PLAIN, 35));
			graphics.drawString("TIME", 1090, 55);
			if (turnTime < turn/2)
				graphics.setColor(Color.GREEN);
			else if (turnTime < turn/1.3)
				graphics.setColor(Color.ORANGE.darker());
			else 
				graphics.setColor(Color.RED);
			
			graphics.setFont(new Font("OCR A Extended", Font.BOLD, 50));
			graphics.drawString(turn-turnTime+"", 1200, 60);
		}
		if (turnTime == turn && playerRound){
			System.out.println("elo");
			turnTime = 0;
			playerRound = false;
			TurnInfo.enemy();
			lastPacket = new Packet();
			tank.getData(lastPacket);
			lastPacket.shot = false;
			lastPacket.roundEnd = true;
			try {
				objectOutputStream.writeObject(lastPacket);
			} catch (IOException e) {
				isConnected = false;
				System.err.println("Server is down!");
			}
		}
	}
	private double windChanger = 0;
	private int windPointer() {
		if (windChanger < newWindForce && newWindForce > 0)
			windChanger += newWindForce/20;
		else if (windChanger > newWindForce && newWindForce < 0) 
			windChanger += newWindForce/20;

		if (windChanger == 0.0)
			return 0;
		else
			return (int) (37 * windChanger);
	}
	public void drawWind(Graphics graphics) {
		graphics.setColor(Color.GRAY);
		graphics.setFont(new Font("OCR A Extended", Font.PLAIN, 22));
		graphics.drawString("WIND", 650, 35);
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(600, 50, 150, 20);
		graphics.setColor(Color.WHITE.darker());
		graphics.fillRect(676, 51, windPointer(), 18);
		graphics.setColor(Color.BLACK);
		graphics.fillRect(674, 48, 4, 24);
	}
	private final Color infoBox = new Color(150, 150, 150, 50);
	public void paint(Graphics grap) {
		if (isConnected == false){
			if (tank == null)
				grap.drawImage(errorScreen, 0, 0, null);

			grap.setFont(new Font("OCR A Extended", Font.PLAIN, 26));
			grap.setColor(Color.RED);
			grap.drawString("Enemy did not connect!", 480, 190);
		}
		if (tank == null){
			grap.drawString("Unable to connect to server!", 430, 480);
			grap.setColor(infoBox);
			grap.fillRect(200, 570, 920, 70);
			grap.setFont(new Font("OCR A Extended", Font.PLAIN, 16));
			grap.setColor(Color.LIGHT_GRAY);
			grap.drawString("Maybe you and your friend are not in the same LAN?", 415, 590);
			grap.drawString("If you want to play with someone who is not in your LAN, you must have a public IP address.", 208, 610);
			grap.drawString("Ask your network administrator if your IP is public or not.", 380, 630);			
			return;
		}
		grap.drawImage(background[board.getMapType()], 0, 0, null);
		board.drawBoard(grap, oldWindForce);
		tank.draw(grap);
		tank.drawStat(grap);
		enemyTank.draw(grap);
		tank.gravity();
		enemyTank.gravity();
		drawWind(grap);
		timeAction(grap);
		int boom[] = tank.calcBullets(oldWindForce, enemyTank);
		int boomEnemy[] = enemyTank.calcBullets(oldWindForce, tank);
		if (boom != null){
			lastDamage = boom[3];
			explosion(boom[0], boom[1], boom[2]);
		}
		if (boomEnemy != null){
			lastDamage = boomEnemy[3];
			explosion(boomEnemy[0], boomEnemy[1], boomEnemy[2]);
		}
		
		if (isConnected == false){
			grap.setColor(Color.GRAY);
			grap.fillRect(548, 248, 204, 104);
			grap.setColor(Color.LIGHT_GRAY);
			grap.fillRect(550, 250, 200, 100);
			grap.setFont(new Font("OCR A Extended", Font.PLAIN, 26));
			grap.setColor(Color.RED);
			grap.drawString("Enemy left!", 565, 308);
		}
		
		if (tank.isAlive() == false){
			grap.setColor(Color.GRAY);
			grap.fillRect(548, 248, 204, 104);
			grap.setColor(Color.LIGHT_GRAY);
			grap.fillRect(550, 250, 200, 100);
			grap.setFont(new Font("OCR A Extended", Font.PLAIN, 26));
			grap.setColor(Color.RED);
			grap.drawString("You died!", 578, 308);
		}
		if (enemyTank.isAlive() == false){
			grap.setColor(Color.GRAY);
			grap.fillRect(548, 248, 204, 104);
			grap.setColor(Color.LIGHT_GRAY);
			grap.fillRect(550, 250, 200, 100);
			grap.setFont(new Font("OCR A Extended", Font.PLAIN, 26));
			grap.setColor(Color.GREEN.darker());
			grap.drawString("You won!", 590, 308);
		}
	}
	
	public boolean changeGun(int gunID) {
		return tank.changeWeapon(gunID);
	}
	public void controls(boolean left) {
		if (playerRound){
			tank.move(left);
			lastPacket = new Packet();
			tank.getData(lastPacket);
			lastPacket.tankMoved = true;
			try {
				objectOutputStream.writeObject(lastPacket);
			} catch (IOException e) {
				isConnected = false;
				System.err.println("Server is down!");
			}
		}
	}
	public void changeCannonAngle(int newAngle) {
		if (playerRound){
			tank.changeAngle(newAngle);
			lastPacket = new Packet();
			tank.getData(lastPacket);
			lastPacket.tankMoved = true;
			try {
				objectOutputStream.writeObject(lastPacket);
			} catch (IOException e) {
				isConnected = false;
				System.err.println("Server is down!");
			}
		}
	}
	public void changeCannonForce(int newForce) {
		if (playerRound){
			tank.changeForce(newForce);
			lastPacket = new Packet();
			tank.getData(lastPacket);
			lastPacket.tankMoved = true;
			try {
				objectOutputStream.writeObject(lastPacket);
			} catch (IOException e) {
				isConnected = false;
				System.err.println("Server is down!");
			}
		}
	}
	public void shotTank() {
		if (playerRound){
			if (tank.shot() == false)
				return;
			playerRound = false;
			TurnInfo.enemy();
			lastPacket = new Packet();
			tank.getData(lastPacket);
			randWindForce();
			lastPacket.windForce = newWindForce;
			lastPacket.shot = true;
			lastPacket.roundEnd = true;
			try {
				objectOutputStream.writeObject(lastPacket);
			} catch (IOException e) {
				isConnected = false;
				System.err.println("Server is down!");
			}
		}
	}

	public void explosion(int i, int j, float radius) {
		board.explosion(i, j, radius);		
		SoundManager.playSound("explosion");
		if (tank.takeExplosionDamage(i, j, radius, lastDamage))
			tank.killYourself();
		if (enemyTank.takeExplosionDamage(i, j, radius, lastDamage))
			enemyTank.killYourself();
	}
	
	public void randWindForce() {
		this.oldWindForce = newWindForce;
		Random random = new Random();
		newWindForce = (double)random.nextInt(1000000) / 1000000.0;
		newWindForce *= 4;
		newWindForce -= 2;
	}

	public void setWindForce(double force) {
		this.oldWindForce = newWindForce;
		this.newWindForce = force;
	}
	
	public boolean localHasFuel() {
		return tank.getFuel()>0?true && playerRound:false;
	}
}