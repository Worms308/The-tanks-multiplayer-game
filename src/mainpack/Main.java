package mainpack;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main {

	private GameBoard GameWindow = null;
	private JFrame okno1;
	private JLabel background,logo,bg2,bg3,se,music,nickname,sip;
	private JButton playGame, exit,settings,back,backO,start;
	private JPanel panel1,panel2,panel3;
	private JSlider slider1,slider2;
	private JTextField nick,server;
	private JPopupMenu popupMenu;
	
	public Thread gameLoopThread;
	
	private Serwer serwer; 
	private Thread serwerThread;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.gameLoopThread = new Thread(new Runnable() {
						@Override
						public void run() {
							window.gameLoop();
						}
					});
					window.gameLoopThread.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	public void gameLoop() {
		while(GameWindow != null){
			GameWindow.repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	private void initialize() {
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		okno1 = new JFrame("The Tanks");
		okno1.setBounds((int)((size.width - 1280)/2.), (int)((size.height - 800)/2.5), 1280, 800);
		okno1.setIconImage(new ImageIcon("graphics//icon.png").getImage());

		okno1.getRootPane().setBorder(BorderFactory.createEmptyBorder());
		okno1.getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
		okno1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		

		okno1.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}	
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					SoundManager.saveConfig("sounds//config.cfg");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		
		okno1.setResizable(false);
		
		panel1 = new JPanel();
		panel1. setOpaque(true);
		panel1.setBackground(Color.DARK_GRAY);
		panel1.setLayout(null);
		
		panel2 = new JPanel();
		panel2. setOpaque(true);
		panel2.setBackground(Color.DARK_GRAY);
		panel2.setLayout(null);
		
		JPanel mapPanel = new JPanel();
		panel2.add(mapPanel);
		mapPanel.setBounds(850, 300, 100, 200);
		mapPanel.setLayout(null);
		mapPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.GRAY));
		JRadioButton map1 = new JRadioButton("FOREST");
		map1.setBackground(new Color(150,170,150));
		map1.setFont(new Font("OCR A Extended", Font.BOLD, 12));
		map1.setSelected(true);
		map1.setBounds(2, 2, 96, 40);
		map1.setFocusable(false);
		JRadioButton map2 = new JRadioButton("DESERT");
		map2.setBounds(2, 40, 96, 40);
		map2.setFont(new Font("OCR A Extended", Font.BOLD, 12));
		map2.setBackground(new Color(150,170,150));
		map2.setFocusable(false);
		JRadioButton map3 = new JRadioButton("SNOW");
		map3.setBounds(2, 80, 96, 40);
		map3.setFont(new Font("OCR A Extended", Font.BOLD, 12));
		map3.setBackground(new Color(150,170,150));
		map3.setFocusable(false);
		JRadioButton map4 = new JRadioButton("MOUNTAIN");
		map4.setBounds(2, 120, 96, 40);
		map4.setFont(new Font("OCR A Extended", Font.BOLD, 12));
		map4.setBackground(new Color(150,170,150));
		map4.setFocusable(false);
		JRadioButton map5 = new JRadioButton("CITY");
		map5.setBounds(2, 160, 96, 38);
		map5.setFont(new Font("OCR A Extended", Font.BOLD, 12));
		map5.setBackground(new Color(150,170,150));
		map5.setFocusable(false);
		ButtonGroup mapChoosing = new ButtonGroup();
		mapChoosing.add(map5);
		mapChoosing.add(map4);
		mapChoosing.add(map3);
		mapChoosing.add(map2);
		mapChoosing.add(map1);
		
		mapPanel.add(map5);
		mapPanel.add(map4);
		mapPanel.add(map3);
		mapPanel.add(map2);
		mapPanel.add(map1);
		
		panel3 = new JPanel();
		panel3. setOpaque(true);
		panel3.setBackground(Color.DARK_GRAY);
		panel3.setLayout(null);
		
		logo = new JLabel(new ImageIcon("graphics//logo.png"));
		logo.setBounds(0530, 100, 580, 114);
		logo.setFocusable(false);
		panel1.add(logo);
	
		background = new JLabel(new ImageIcon("graphics//tank2.jpg"));
		background.setBounds(0, 0, 1280, 800);
		panel1.add(background);
		panel1.setFocusable(false);
		
		slider1 = new JSlider();
		slider1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.GRAY));
		slider1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SoundManager.playSound("button");
				slider1.repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				SoundManager.playSound("button");
				slider2.repaint();
			}
		});
		slider1.setFocusable(false);
		slider1.setBackground(new Color(150,170,150));
		slider1.setValue(SoundManager.musicVol);
		slider1.setPaintTicks(true);
		slider1.setMaximum(100);
		slider1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				okno1.repaint();
				SoundManager.setMusicVolume(slider1.getValue());
			}
		});
		slider1.setBounds(550, 200, 200, 68);
		panel3.add(slider1);
				
		
		slider2 = new JSlider();
		slider2.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.GRAY));
		slider2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SoundManager.playSound("button");
				slider2.repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				SoundManager.playSound("button");
				slider2.repaint();
			}
		});
		slider2.setFocusable(false);
		slider2.setBackground(new Color(150,170,150));
		slider2.setValue(SoundManager.soundsVol);
		slider2.setPaintTicks(true);
		slider2.setMaximum(100);
		slider2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				SoundManager.setSoundsVolume(slider2.getValue());
				okno1.repaint();
			}
		});
		slider2.setBounds(550, 500, 200, 68);
		panel3.add(slider2);
		
		JButton runServer = new JButton("Run server");
		runServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				SoundManager.playSound("buttonHover");
			}
		});
		runServer.setFont(new Font("OCR A Extended", Font.PLAIN, 26));
		runServer.setBounds(800, 214, 200, 40);
		runServer.setFocusable(false);
		panel2.add(runServer);
		runServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SoundManager.playSound("button");
				serwer = new Serwer();
				serwerThread = new Thread(serwer);
				serwerThread.start();
				nick.setText("localhost");
				nick.setEditable(false);
			}
		});
		
		nick = new JTextField("");
		nick.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				SoundManager.playSound("buttonHover");
			}
		});
		nick.setFont(new Font("OCR A Extended", Font.PLAIN, 26));
		nick.setBounds(525,200,250,68);
		nick.setHorizontalAlignment(SwingConstants.CENTER);
		nick.setFocusable(true);
		panel2.add(nick);		
		
		server = new JTextField("");
		server.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				SoundManager.playSound("buttonHover");
			}
		});
		server.setHorizontalAlignment(SwingConstants.CENTER);
		server.setFont(new Font("OCR A Extended", Font.PLAIN, 26));
		server.setBounds(525,500,250,68);
		server.setFocusable(true);
		popupMenu = new JPopupMenu();
		popupMenu.setSize(50, 40);
		addPopup(nick, popupMenu);
		
		JMenuItem mntmCopy = new JMenuItem("Copy");
		mntmCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (serwerThread == null){
					StringSelection selection = new StringSelection(nick.getText());
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
				} else {
					try {
						String localIp = InetAddress.getLocalHost().getHostAddress();
						StringSelection selection = new StringSelection(localIp);
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, selection);
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		popupMenu.add(mntmCopy);
		
		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				Transferable transferable = clipboard.getContents(this);
				if (transferable != null){
					try {
						String tmp = (String)transferable.getTransferData(DataFlavor.stringFlavor);
						if (tmp.length() > 15)
							tmp = tmp.substring(0, 15);
						if (nick.isEditable())
							nick.setText(tmp);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		popupMenu.add(mntmPaste);
		
		panel2.add(server);
		JLabel advice = new JLabel("If you turn server on copy your IP from box.");
		advice.setFont(new Font("OCR A Extended", Font.PLAIN, 12));
		panel2.add(advice);
		advice.setForeground(Color.LIGHT_GRAY.brighter());
		advice.setBounds(500, 266, 450, 30);
		
	    
		playGame = new JButton();
		playGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				SoundManager.playSound("buttonHover");
			}
		});
		playGame.setIcon(new ImageIcon("graphics//pg.png"));
		playGame.setBounds(530,350,250,50);
		background.add(playGame);
		playGame.setBorderPainted(false);
		playGame.setBorder(null);
		playGame.setMargin(new Insets(0, 0, 0, 0));
		playGame.setContentAreaFilled(false);
		playGame.setFocusable(false);
		playGame.setPressedIcon(new ImageIcon("graphics//pgClick.png"));
		playGame.setRolloverIcon(new ImageIcon("graphics//pgHover.png"));
		
		
		playGame.addActionListener(new ActionListener()

		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundManager.playSound("button");
				okno1.remove(panel1);
				okno1.getContentPane().add(panel2);
				okno1.revalidate();
				okno1.repaint();
			}
		});
		
		settings = new JButton();
		settings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				SoundManager.playSound("buttonHover");
			}
		});
		settings.setIcon(new ImageIcon("graphics//settings.png"));
		settings.setPressedIcon(new ImageIcon("graphics//settingsClick.png"));
		settings.setRolloverIcon(new ImageIcon("graphics//settingsHover.png"));
		settings.setBounds(530,500,250,50);
		background.add(settings);
		settings.setBorderPainted(false);
		settings.setBorder(null);
		settings.setMargin(new Insets(0, 0, 0, 0));
		settings.setContentAreaFilled(false);
		settings.setFocusable(false);
		settings.addActionListener(new ActionListener()	
		
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundManager.playSound("button");
				okno1.remove(panel1);
				okno1.getContentPane().add(panel3);
				okno1.revalidate();
				okno1.repaint();
			}
		});
		
		exit = new JButton();
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				SoundManager.playSound("buttonHover");
			}
		});
		exit.setIcon(new ImageIcon("graphics//exit.png"));
		exit.setPressedIcon(new ImageIcon("graphics//exitClick.png"));
		exit.setRolloverIcon(new ImageIcon("graphics//exitHover.png"));
		exit.setBounds(530,650,250,50);
		background.add(exit);
		background.setFocusable(false);
		exit.setBorderPainted(false);
		exit.setBorder(null);
		exit.setMargin(new Insets(0, 0, 0, 0));
		exit.setContentAreaFilled(false);
		exit.setFocusable(false);

		
		exit.addActionListener(new ActionListener()
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent ae)
			{
				SoundManager.playSound("button");
				if (serwerThread != null)
					serwerThread.stop();
				try {
					SoundManager.saveConfig("sounds//config.cfg");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		
		back = new JButton();
		back.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				SoundManager.playSound("buttonHover");
			}
		});
		back.setIcon(new ImageIcon("graphics//back.png"));
		back.setRolloverIcon(new ImageIcon("graphics//backHover.png"));
		back.setPressedIcon(new ImageIcon("graphics//backClick.png"));
		back.setBounds(100,700,150,50);
		panel2.add(back);
		back.setBorderPainted(false);
		back.setBorder(null);
		back.setMargin(new Insets(0, 0, 0, 0));
		back.setFocusable(false);
		back.setContentAreaFilled(false);
		back.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundManager.playSound("button");
				okno1.remove(panel2);
				okno1.getContentPane().add(panel1);
				okno1.revalidate();
				okno1.repaint();
			}
		});
		
		backO = new JButton();
		backO.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				SoundManager.playSound("buttonHover");
			}
		});
		backO.setIcon(new ImageIcon("graphics//back.png"));
		backO.setRolloverIcon(new ImageIcon("graphics//backHover.png"));
		backO.setPressedIcon(new ImageIcon("graphics//backClick.png"));
		backO.setBounds(100,700,150,50);
		panel3.add(backO);
		backO.setBorderPainted(false);
		backO.setBorder(null);
		backO.setMargin(new Insets(0, 0, 0, 0));
		backO.setContentAreaFilled(false);
		backO.setFocusable(false);
		backO.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundManager.playSound("button");
				okno1.remove(panel3);
				okno1.getContentPane().add(panel1);
				okno1.revalidate();
				okno1.repaint();
				
			}
		});
		
		start = new JButton();
		start.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				SoundManager.playSound("buttonHover");
			}
		});
		start.setIcon(new ImageIcon("graphics//start.png"));
		start.setPressedIcon(new ImageIcon("graphics//startClick.png"));
		start.setRolloverIcon(new ImageIcon("graphics//startHover.png"));
		start.setBounds(1000,700,150,50);
		panel2.add(start);
		start.setBorderPainted(false);
		start.setBorder(null);
		start.setMargin(new Insets(0, 0, 0, 0));
		start.setContentAreaFilled(false);
		start.setFocusable(false);
		
		se = new JLabel(new ImageIcon("graphics//se.png"));
		se.setBounds(430, 400, 437, 68);
		panel3.add(se);
		se.setFocusable(false);
		
		music = new JLabel(new ImageIcon("graphics//music.png"));
		music.setBounds(555, 100, 182, 68);
		panel3.add(music);
		music.setFocusable(false);
		
		nickname = new JLabel(new ImageIcon("graphics//nickname.png"));
		nickname.setBounds(500, 400, 295, 68);
		panel2.add(nickname);
		nickname.setFocusable(false);

		sip = new JLabel(new ImageIcon("graphics//sip.png"));
		sip.setBounds(500, 100, 303, 68);
		panel2.add(sip);
		sip.setFocusable(false);
		
		
		bg2= new JLabel(new ImageIcon("graphics//tank2darker.jpg"));
		bg2.setBounds(0, 0, 1280, 800);
		bg2.setFocusable(false);
		
		panel2.setFocusable(false);
		
		bg3 = new JLabel(new ImageIcon("graphics//tank2darker.jpg"));
		bg3.setBounds(0, 0, 1280, 800);
		bg3.setFocusable(false);
		panel3.add(bg3);
		panel3.setFocusable(false);
		
	
		
		okno1.getContentPane().add(panel1);
		okno1.setSize(1280, 800);
		okno1.setVisible(true);

		
		
		ButtonGroup bullets = new ButtonGroup();
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setFocusable(true);
		layeredPane.setBounds(0, 0, 1280, 772);
		
		
		GameWindow = new GameBoard();
		GameWindow.setFocusable(false);
		GameWindow.setBounds(0, 0, 1280, 649);
		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameWindow.addTime();
			}
		});
		timer.start();
		layeredPane.add(GameWindow);
		
		layeredPane.setLayout(null);

		okno1.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT){
					GameWindow.controls(true);
				}else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT){
					GameWindow.controls(false);
				}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
					if (layeredPane.getParent() != null)
							if (JOptionPane.showConfirmDialog(layeredPane, "Do you want to quit game?", "", JOptionPane.OK_OPTION) == 0){
								GameWindow.disconnect();
								TurnInfo.notConnecting();
								okno1.remove(layeredPane);
								layeredPane.remove(GameWindow);
								GameWindow = new GameBoard();
								if (map1.isSelected()){
									GameWindow.changeMap(0);
								}else if (map2.isSelected()) {
									GameWindow.changeMap(1);
								}else if (map3.isSelected()) {
									GameWindow.changeMap(2);
								}else if (map4.isSelected()) {
									GameWindow.changeMap(3);
								}else {
									GameWindow.changeMap(4);
								}
								GameWindow.setFocusable(false);
								GameWindow.setBounds(0, 0, 1280, 649);
								layeredPane.add(GameWindow);
								okno1.getContentPane().add(panel1);
								okno1.repaint();
								SoundManager.stopSound("gameMusic");
								SoundManager.loopSound("menuMusic");
							}
				}
			}
		});
		
		JPanel ShotSettings = new JPanel();
		
		ShotSettings.setFocusable(false);
		ShotSettings.setBounds(0, 650, 1274, 122);
		layeredPane.add(ShotSettings);
		ShotSettings.setLayout(null);
		
		JPanel ShotAngle = new JPanel();
		ShotAngle.setFocusable(false);
		ShotAngle.setBounds(1, 0, 426, 122);
		ShotSettings.add(ShotAngle);
		ShotAngle.setLayout(null);
		
		JLabel ShotAngleLabel = new JLabel("Shot Angle:");
		ShotAngleLabel.setFocusable(false);
		ShotAngleLabel.setFont(new Font("OCR A Extended", Font.PLAIN, 26));
		ShotAngleLabel.setBounds(10, 34, 176, 46);
		ShotAngle.add(ShotAngleLabel);
		
		JFormattedTextField AngleBox = new JFormattedTextField();
		AngleBox.setFocusable(false);
		AngleBox.setEditable(false);
		AngleBox.setHorizontalAlignment(SwingConstants.CENTER);
		AngleBox.setText("90");
		AngleBox.setFont(new Font("OCR A Extended", Font.PLAIN, 25));
		AngleBox.setBounds(192, 42, 50, 30);
		ShotAngle.add(AngleBox);
		
		JSlider AngleSlider = new JSlider();
		AngleSlider.setFocusable(false);
		AngleSlider.setValue(90);
		AngleSlider.setPaintTicks(true);
		AngleSlider.setMaximum(180);
		AngleSlider.setBounds(250, 46, 143, 30);
		ShotAngle.add(AngleSlider);
		AngleSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				AngleBox.setText(new Integer(AngleSlider.getValue()).toString());
				GameWindow.changeCannonAngle(-(180 - AngleSlider.getValue()));
			}
		});
		
		JSeparator separator = new JSeparator();
		separator.setFocusable(false);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(425, 0, 1, 122);
		ShotAngle.add(separator);
		
		JPanel ShotPower = new JPanel();
		ShotPower.setFocusable(false);
		ShotPower.setBounds(429, 0, 425, 122);
		ShotSettings.add(ShotPower);
		ShotPower.setLayout(null);
		
		JLabel lblShotPower = new JLabel("Shot Power:");
		lblShotPower.setFocusable(false);
		lblShotPower.setFont(new Font("OCR A Extended", Font.PLAIN, 26));
		lblShotPower.setBounds(10, 34, 176, 46);
		ShotPower.add(lblShotPower);
		
		JFormattedTextField PowerBox = new JFormattedTextField();
		PowerBox.setFocusable(false);
		PowerBox.setEditable(false);
		PowerBox.setFont(new Font("OCR A Extended", Font.PLAIN, 25));
		PowerBox.setHorizontalAlignment(SwingConstants.CENTER);
		PowerBox.setText("50");
		PowerBox.setBounds(192, 42, 50, 30);
		ShotPower.add(PowerBox);
		
		JSlider PowerSlider = new JSlider();
		PowerSlider.setFocusable(false);
		PowerSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				PowerBox.setValue(new Integer(PowerSlider.getValue()));
				GameWindow.changeCannonForce(PowerSlider.getValue());
			}
		});
		PowerSlider.setPaintTicks(true);
		PowerSlider.setBounds(250, 46, 143, 30);
		ShotPower.add(PowerSlider);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setFocusable(false);
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(423, 0, 1, 122);
		ShotPower.add(separator_1);
		
		JPanel BulletChose = new JPanel();
		BulletChose.setFocusable(false);
		BulletChose.setBounds(855, 0, 419, 122);
		ShotSettings.add(BulletChose);
		BulletChose.add(TurnInfo.text);
		BulletChose.setLayout(null);
		
		JButton btnShot = new JButton("Shot");
		
		btnShot.setBounds(220, 70, 100, 30);
		btnShot.setFocusable(false);
		BulletChose.add(btnShot);
		
		JRadioButton rdbtnGun = new JRadioButton("Bullet");
		
		bullets.add(rdbtnGun);
		rdbtnGun.setFont(new Font("OCR A Extended", Font.PLAIN, 24));
		rdbtnGun.setForeground(Color.GREEN.darker().darker());
		rdbtnGun.setFocusable(false);
		rdbtnGun.setSelected(true);
		rdbtnGun.setBounds(6, 8, 160, 23);
		BulletChose.add(rdbtnGun);
		
		JRadioButton rdbtnGun_1 = new JRadioButton("Anti-tank");
		bullets.add(rdbtnGun_1);
		rdbtnGun_1.setFont(new Font("OCR A Extended", Font.PLAIN, 24));
		rdbtnGun_1.setForeground(Color.GREEN.darker().darker());
		rdbtnGun_1.setFocusable(false);
		rdbtnGun_1.setBounds(6, 48, 160, 23);
		BulletChose.add(rdbtnGun_1);
		
		JRadioButton rdbtnGun_2 = new JRadioButton("Nuke");
		bullets.add(rdbtnGun_2);
		rdbtnGun_2.setFont(new Font("OCR A Extended", Font.PLAIN, 24));
		rdbtnGun_2.setForeground(Color.GREEN.darker().darker());
		rdbtnGun_2.setFocusable(false);
		rdbtnGun_2.setBounds(6, 92, 160, 23);
		BulletChose.add(rdbtnGun_2);
		
		btnShot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GameWindow.shotTank();
				int ammo[] = GameWindow.getTankAmmo();
				if (ammo[0] == 0)
					rdbtnGun.setForeground(Color.RED.darker());
				if (ammo[1] == 0)
					rdbtnGun_1.setForeground(Color.RED.darker());
				if (ammo[2] == 0)
					rdbtnGun_2.setForeground(Color.RED.darker());
				//SoundManager.playSound("button");
			}
		});
		
		rdbtnGun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int lastPicked;
				SoundManager.playSound("button");
				if (rdbtnGun.isSelected())
					lastPicked = 0;
				else if (rdbtnGun_2.isSelected())
					lastPicked = 1;
				else 
					lastPicked = 2;
				if (GameWindow.changeGun(0) == false){
					rdbtnGun.setSelected(false);
					if (lastPicked == 1)
						rdbtnGun_1.setSelected(true);
					else
						rdbtnGun_2.setSelected(true);
				}
			}
		});
		rdbtnGun_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int lastPicked;
				SoundManager.playSound("button");
				if (rdbtnGun.isSelected())
					lastPicked = 0;
				else if (rdbtnGun_2.isSelected())
					lastPicked = 1;
				else 
					lastPicked = 2;
				if (GameWindow.changeGun(1) == false){
					rdbtnGun.setSelected(false);
					if (lastPicked == 0)
						rdbtnGun.setSelected(true);
					else
						rdbtnGun_2.setSelected(true);
				}
			}
		});
		rdbtnGun_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int lastPicked;
				SoundManager.playSound("button");
				if (rdbtnGun.isSelected())
					lastPicked = 0;
				else if (rdbtnGun_2.isSelected())
					lastPicked = 1;
				else 
					lastPicked = 2;
				if (GameWindow.changeGun(2) == false){
					rdbtnGun.setSelected(false);
					if (lastPicked == 0)
						rdbtnGun.setSelected(true);
					else
						rdbtnGun_1.setSelected(true);
				}
			}
		});
		
		panel2.add(TurnInfo.connecting);
		TurnInfo.notConnecting();
		start.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				TurnInfo.connecting();
			}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}	
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		start.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundManager.playSound("button");
				okno1.remove(panel2);
				if (map1.isSelected()){
					GameWindow.changeMap(0);
				}else if (map2.isSelected()) {
					GameWindow.changeMap(1);
				}else if (map3.isSelected()) {
					GameWindow.changeMap(2);
				}else if (map4.isSelected()) {
					GameWindow.changeMap(3);
				}else {
					GameWindow.changeMap(4);
				}
				GameWindow.connectToServer(nick.getText(), server.getText()); /// KTOS TU POPIEPRZYL POLA ...
				SoundManager.stopSound("menuMusic");
				okno1.requestFocus();
				okno1.requestFocusInWindow();
				okno1.getContentPane().add(layeredPane);
				okno1.repaint();
				int ammo[] = GameWindow.getTankAmmo();
				if (ammo[0] == 0)
					rdbtnGun.setForeground(Color.RED.darker());
				if (ammo[1] == 0)
					rdbtnGun_1.setForeground(Color.RED.darker());
				if (ammo[2] == 0)
					rdbtnGun_2.setForeground(Color.RED.darker());
			}
		});
		
		panel2.add(bg2);

		SoundManager.loopSound("menuMusic");
		
	}
}
