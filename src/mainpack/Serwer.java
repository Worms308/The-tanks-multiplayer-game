package mainpack;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Serwer implements Runnable{
	public static int PORT = 15308;
	public Packet actualGame;
	
	public Socket player1;
	public Socket player2;
	
	public boolean shouldWork = true;
	
	public int lastConnected = 0;
	public boolean checkConnections() {
		return player1.isClosed() | player2.isClosed();
	}
	public void mainWhile(){
		ServerSocket serverSocket = null;
		Serwer serwer = null;
		ObjectOutputStream player1Out = null;
		ObjectInputStream player1In = null;
		ObjectOutputStream player2Out = null;
		ObjectInputStream Player2In = null;
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server up and ready to work!");
			serwer = new Serwer();
			serwer.player1 = serverSocket.accept();
			player1Out = new ObjectOutputStream(serwer.player1.getOutputStream());
			player1Out.writeObject(new String("Waiting for second player!"));
			///pobranie mapy
			player1In = new ObjectInputStream(serwer.player1.getInputStream());
			serwer.actualGame = (Packet)player1In.readObject();
			
			System.out.println("Player one connected!");
			serwer.player2 = serverSocket.accept();
			player2Out = new ObjectOutputStream(serwer.player2.getOutputStream());
			System.out.println("Player two connected!");
			player1Out.writeObject(new String("Both players connected!"));
			///wyslanie mapy do 2 gracza
			player2Out.writeObject(new String("Downloading map from first player!"));
			player2Out.writeObject(serwer.actualGame);
			player2Out.writeObject(new String("Both players connected!"));
			

			Player2In = new ObjectInputStream(serwer.player2.getInputStream());
			
			/// WYSLANIE MAPY
			serwer.actualGame = (Packet)player1In.readObject();
			player2Out.writeObject(serwer.actualGame);
			
			/// GLOWNA PETLA ROZGRYWKI
			exit:
			while(serwer.actualGame.gameEnd == false){
				while(serwer.actualGame.roundEnd == false){
					serwer.lastConnected = 1;
					if (serwer.checkConnections())
						break exit;
					serwer.actualGame = (Packet)player1In.readObject();
					player2Out.writeObject(serwer.actualGame);
				}
				serwer.actualGame.roundEnd = false;
				while(serwer.actualGame.roundEnd == false){
					serwer.lastConnected = 2;
					if (serwer.checkConnections())
						break exit;
					serwer.actualGame = (Packet)Player2In.readObject();
					player1Out.writeObject(serwer.actualGame);
				}
				serwer.actualGame.roundEnd = false;
			}
			
			serverSocket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SocketException | EOFException e) {
			serwer.actualGame.enemyDisconnected = true;
			//if (serwer.lastConnected == 2){
				try {
					player2Out.writeObject(serwer.actualGame);
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
			//} else {
				try {
					player1Out.writeObject(serwer.actualGame);
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
			//}

			System.err.println("Utracono polaczenie!");
			System.err.println("Reboot serwera!");
			try {
				serverSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (serwer.shouldWork)
				mainWhile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		this.mainWhile();
	}
}
