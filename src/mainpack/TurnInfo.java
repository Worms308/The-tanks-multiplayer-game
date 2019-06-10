package mainpack;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TurnInfo {
	public static JLabel text = new JLabel();
	public static JLabel connecting = new JLabel("Connecting ...");
	public static void enemy() {
		text.setText("Enemy turn!");
		text.setForeground(new Color(250, 30, 30));
	}
	public static void your() {
		text.setText("Your turn!");
		text.setForeground(new Color(30, 250, 30));
	}
	public static void connected() {
		connecting.setText("Connected!");
	}
	public static void connecting() {
		connecting.setForeground(new Color(100, 250, 100));
	}
	public static void notConnecting() {
		connecting.setForeground(new Color(100, 250, 100, 0));
	}
	static{
		//text.setText("dfghdghdh");
		text.setForeground(new Color(250, 30, 30));
		text.setFont(new Font("OCR A Extended", Font.BOLD, 26));
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setBounds(120, 15, 300, 40);
		
		connecting.setForeground(new Color(100, 250, 100));
		connecting.setFont(new Font("OCR A Extended", Font.BOLD, 26));
		connecting.setBounds(970, 650, 300, 50);
	}
	private TurnInfo() {
		
	}
}
