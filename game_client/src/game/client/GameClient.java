package game.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import javax.swing.UIManager;

public class GameClient implements Runnable{
	public final static GameClient _gameClient = new GameClient();
	
	public final static int NULL = 0;
	public final static int DISCONNECTED = 1;
	public final static int DISCONNECTING = 2;
	public final static int BEGIN_CONNECT = 3;
	public final static int CONNECTED = 4;	
	
	public final static String IMAGE_PATH = "/images/";
	public final static String IMAGE_EXT = ".jpg";
	
	public final static String statusMessages[] = {
		" Error! Could not connect!", " Disconnected",
		" Disconnecting...", " Connecting...", " Connected"
	};
	public static String _hostIP = "localhost";
	   								//"140.209.123.186"; //OSS-LL12_01
	   								//"140.209.122.249"; //Prof
									//"140.209.226.160"; //Josh
									//"140.209.122.199"; //OSS-LL12_03
	public static int _port = 80;
	public static Socket _client = null;
	public static PrintWriter _out = null;
	public static BufferedReader _in = null;
	
	public static StringBuffer toAppend = new StringBuffer("");
	public static StringBuffer toSend = new StringBuffer("");
	
	public static int connectionStatus = DISCONNECTED;
	public static String statusString = statusMessages[connectionStatus];
	
	private static JFrame frmBlackjack;
	private static JTextField input;
	private static JLabel lblBet;
	private static JTextArea output;
	private static JScrollPane scrollPane;
	private static JTextField statusColor;
	private static JLabel statusField;
	private static JLabel lblMsg;
	private static JLabel lblMoneyValue;
	private static JLabel lblStatsValue;
	private static JButton btnHit;
	private static JButton btnBet;
	private static JButton btnStand;
	
	private static JLabel p8C5;
	private static JLabel p8C4;
	private static JLabel p8C3;
	private static JLabel p8C2;
	private static JLabel p8C1;
	private static JLabel p8Score;
	private static JLabel p8Bet;
	private static JLabel p8Name;
	private static JLabel[] p8;
	private static JLabel p7C5;
	private static JLabel p7C4;
	private static JLabel p7C3;
	private static JLabel p7C2;
	private static JLabel p7C1;
	private static JLabel p7Score;
	private static JLabel p7Bet;
	private static JLabel p7Name;
	private static JLabel[] p7;
	private static JLabel p6C5;
	private static JLabel p6C4;
	private static JLabel p6C3;
	private static JLabel p6C2;
	private static JLabel p6C1;
	private static JLabel p6Score;
	private static JLabel p6Bet;
	private static JLabel p6Name;
	private static JLabel[] p6;
	private static JLabel p5C5;
	private static JLabel p5C4;
	private static JLabel p5C3;
	private static JLabel p5C2;
	private static JLabel p5C1;
	private static JLabel p5Score;
	private static JLabel p5Bet;
	private static JLabel p5Name;
	private static JLabel[] p5;
	private static JLabel p4C5;
	private static JLabel p4C4;
	private static JLabel p4C3;
	private static JLabel p4C2;
	private static JLabel p4C1;
	private static JLabel p4Score;
	private static JLabel p4Bet;
	private static JLabel p4Name;
	private static JLabel[] p4;
	private static JLabel p3C5;
	private static JLabel p3C4;
	private static JLabel p3C3;
	private static JLabel p3C2;
	private static JLabel p3C1;
	private static JLabel p3Score;
	private static JLabel p3Bet;
	private static JLabel p3Name;
	private static JLabel[] p3;
	private static JLabel p2C5;
	private static JLabel p2C4;
	private static JLabel p2C3;
	private static JLabel p2C2;
	private static JLabel p2C1;
	private static JLabel p2Score;
	private static JLabel p2Bet;
	private static JLabel p2Name;
	private static JLabel[] p2;
	private static JLabel p1C5;
	private static JLabel p1C4;
	private static JLabel p1C3;
	private static JLabel p1C2;
	private static JLabel p1C1;
	private static JLabel p1Score;
	private static JLabel p1Bet;
	private static JLabel p1Name;
	private static JLabel[] p1;
	private static JLabel[][] _players;
	private static JLabel dC5;
	private static JLabel dC4;
	private static JLabel dC3;
	private static JLabel dC2;
	private static JLabel dC1;
	private static JLabel dScore;
	private static JLabel[] d;
	private static JLabel p1Results;
	private static JLabel p2Results;
	private static JLabel p3Results;
	private static JLabel p4Results;
	private static JLabel p5Results;
	private static JLabel p6Results;
	private static JLabel p7Results;
	private static JLabel p8Results;
	private static JLabel[] _results;
	private static JLabel currentTable;
	private static JButton btnLeaveTable;
	//TODO
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		initialize();
		String hold = "";
		
		while(true)
		{
			try { // Poll every ~10 ms
				Thread.sleep(10);
			}
			catch (InterruptedException e) {}
			
			switch(connectionStatus){
			case BEGIN_CONNECT:
				try {
					_client = new Socket(_hostIP, _port);
					_in = new BufferedReader(new InputStreamReader(_client.getInputStream()));
					_out = new PrintWriter(_client.getOutputStream(), true);
					changeStatus(CONNECTED, true);
					input.selectAll();
					input.requestFocus();
				} catch (Exception e) {
					cleanUp();
					changeStatus(DISCONNECTED, false);
					e.printStackTrace();
				}
				break;
			case CONNECTED:
				try {
					if(toSend.length() != 0){
						_out.print(toSend); _out.flush();
						toSend.setLength(0);
						changeStatus(NULL, true);
						lblMsg.setText(".");
					}
					
					if(_in.ready()){
						hold = _in.readLine();
						if(hold != null && hold.length() != 0){
							if(hold.equals("end")){
								currentTable.setText("");
								changeStatus(DISCONNECTING, true);
							}
							else if(hold.startsWith("REGISTER")){
								if(!hold.contains("success")){
									JOptionPane.showMessageDialog(frmBlackjack, 
										hold.substring(8), 
										"Register Error", JOptionPane.ERROR_MESSAGE, null);
									changeStatus(DISCONNECTED, true);
								}
								else
									changeStatus(NULL, true);
							}
							else if(hold.startsWith("LOGIN")){
								if(!hold.contains("success")){
									JOptionPane.showMessageDialog(frmBlackjack, 
										hold.substring(5), 
										"Warning", JOptionPane.ERROR_MESSAGE, null);
									changeStatus(DISCONNECTED, true);
								}
								else
									changeStatus(NULL, true);
							}
							else if(hold.startsWith("GAME")){
								currentTable.setText("");
								menuList(hold.substring(5));
								changeStatus(NULL, true);
							}
							else if(hold.startsWith("YESNO")){
								btnBet.setEnabled(false);
								btnHit.setEnabled(true);
								btnStand.setEnabled(true);
								lblMsg.setText(hold.substring(5));
								changeStatus(NULL, true);
								/*JOptionPane.showMessageDialog(frmBlackjack, 
										hold.substring(5), 
										"Yes or No?", JOptionPane.QUESTION_MESSAGE, null);
								int n;
								n = JOptionPane.showOptionDialog(frmBlackjack, 
										hold.substring(5), "Yes or No?",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE,										
										 null, null, null);
								n = openYesNoDialog(hold.substring(5));	
								System.out.println(n);
								if(n == JOptionPane.YES_OPTION ||
										n == JOptionPane.OK_OPTION ||
										n > 0)
									sendString("yes");
								else
									sendString("no");*/
							}
							else if(hold.startsWith("WAIT")){
								btnBet.setEnabled(false);
								btnHit.setEnabled(false);
								btnStand.setEnabled(false);
								lblMsg.setText(hold.substring(4));
								changeStatus(NULL, true);
							}
							else if(hold.startsWith("BANK")){
								lblMoneyValue.setText(hold.substring(4));
								changeStatus(NULL, true);
							}
							else if(hold.startsWith("STATS")){
								lblStatsValue.setText(hold.substring(5));
								changeStatus(NULL, true);
							}
							else if(hold.startsWith("BET")){
								btnBet.setEnabled(true);
								btnHit.setEnabled(false);
								btnStand.setEnabled(false);
								lblMsg.setText(hold.substring(3));
								changeStatus(NULL, true);
							}
							else if(hold.startsWith("POP")){
								pop(hold.substring(3));
								changeStatus(NULL, true);
							}
							else if(hold.startsWith("HANDS")){
								if(hold.contains("CLEAR"))
									clearTable();
								else
									updateTable(hold.substring(6));
							}
							else if(hold.startsWith("RESULTS")){
								updateResults(hold.substring(7));
							}
							else if(hold.startsWith("ERROR")){
								error(hold.substring(5));
								/*JOptionPane.showMessageDialog(frmBlackjack, 
										hold.substring(5), 
										"Error", JOptionPane.ERROR_MESSAGE, null);*/
								changeStatus(NULL, true);
							}
							else if(hold.equals("DEALER")){
								btnBet.setEnabled(false);
								btnHit.setEnabled(false);
								btnStand.setEnabled(false);
							}
							else if(hold.startsWith("TABLE")){
								currentTable.setText(hold.substring(5));
								changeStatus(NULL, true);
							}
							else{
								//appendToOutput(hold + "\n");
								changeStatus(NULL, true);
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					cleanUp();
					changeStatus(DISCONNECTED, false);
				}
				break;
			case DISCONNECTING:
				cleanUp();
				changeStatus(DISCONNECTED, true);
				break;
			default:
				break;	
			}			
		}
	}

	private static void cleanUp(){
		try {
			if (_client != null) {
				_client.close();
				_client = null;
			}
     	}
		catch (IOException e) { _client = null; }
	
		try {
			if (_in != null) {
				_in.close();
				_in = null;
			}
		}
     	catch (IOException e) { _in = null; }
	
     	if (_out != null) {
     		_out.close();
     		_out = null;
     	}		
	}
	
	/**
	 * Create the application.
	 */
	/*public GameClientTest() {
		initialize();
	}*/

	/**
	 * Initialize the contents of the frame.
	 */
	private static void initialize() {
		frmBlackjack = new JFrame();
		frmBlackjack.setResizable(false);
		frmBlackjack.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				logout();
			}
		});
		frmBlackjack.setTitle("BJ or GTFO");
		frmBlackjack.setBounds(100, 100, 1024, 780);
		frmBlackjack.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 440, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 510, 124, 0, 38, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		frmBlackjack.getContentPane().setLayout(gridBagLayout);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_1.gridx = 1;
		gbc_verticalStrut_1.gridy = 0;
		frmBlackjack.getContentPane().add(verticalStrut_1, gbc_verticalStrut_1);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut.gridx = 0;
		gbc_horizontalStrut.gridy = 1;
		frmBlackjack.getContentPane().add(horizontalStrut, gbc_horizontalStrut);
		
		JPanel tablePanel = new JPanel();
		tablePanel.setBorder(null);
		tablePanel.setBackground(new Color(169, 169, 169));
		tablePanel.setLayout(null);
		GridBagConstraints gbc_tablePanel = new GridBagConstraints();
		gbc_tablePanel.gridwidth = 2;
		gbc_tablePanel.insets = new Insets(0, 0, 5, 5);
		gbc_tablePanel.fill = GridBagConstraints.BOTH;
		gbc_tablePanel.gridx = 1;
		gbc_tablePanel.gridy = 1;
		frmBlackjack.getContentPane().add(tablePanel, gbc_tablePanel);
		
		p8C5 = new JLabel("");
		p8C5.setIcon(null);
		p8C5.setBounds(154, 85, 48, 65);
		tablePanel.add(p8C5);
		
		p8C4 = new JLabel("");
		p8C4.setIcon(null);
		p8C4.setBounds(140, 85, 48, 65);
		tablePanel.add(p8C4);
		
		p8C3 = new JLabel("");
		p8C3.setIcon(null);
		p8C3.setBounds(126, 85, 48, 65);
		tablePanel.add(p8C3);
		
		p8C2 = new JLabel("");
		p8C2.setIcon(null);
		p8C2.setBounds(112, 85, 48, 65);
		tablePanel.add(p8C2);
		
		p8C1 = new JLabel("");
		p8C1.setIcon(null);
		p8C1.setBounds(98, 85, 48, 65);
		tablePanel.add(p8C1);
		
		p8Results = new JLabel("");
		p8Results.setHorizontalAlignment(SwingConstants.CENTER);
		p8Results.setForeground(Color.WHITE);
		p8Results.setBounds(98, 161, 104, 14);
		tablePanel.add(p8Results);
		
		p8Score = new JLabel("");
		p8Score.setForeground(new Color(255, 255, 255));
		p8Score.setBackground(new Color(255, 255, 255));
		p8Score.setHorizontalAlignment(SwingConstants.CENTER);
		p8Score.setFont(new Font("Tahoma", Font.BOLD, 15));
		p8Score.setBounds(0, 72, 86, 14);
		tablePanel.add(p8Score);
		
		p8Bet = new JLabel("");
		p8Bet.setHorizontalAlignment(SwingConstants.CENTER);
		p8Bet.setForeground(Color.WHITE);
		p8Bet.setBounds(15, 136, 73, 14);
		tablePanel.add(p8Bet);
		
		p8Name = new JLabel("");
		p8Name.setHorizontalAlignment(SwingConstants.CENTER);
		p8Name.setForeground(Color.WHITE);
		p8Name.setBounds(0, 111, 93, 14);
		tablePanel.add(p8Name);
		
		p7C5 = new JLabel("");
		p7C5.setIcon(null);
		p7C5.setBounds(220, 145, 48, 65);
		tablePanel.add(p7C5);
		
		p7C4 = new JLabel("");
		p7C4.setIcon(null);
		p7C4.setBounds(206, 157, 48, 65);
		tablePanel.add(p7C4);
		
		p7C3 = new JLabel("");
		p7C3.setIcon(null);
		p7C3.setBounds(192, 171, 48, 65);
		tablePanel.add(p7C3);
		
		p7C2 = new JLabel("");
		p7C2.setIcon(null);
		p7C2.setBounds(178, 185, 48, 65);
		tablePanel.add(p7C2);
		
		p7C1 = new JLabel("");
		p7C1.setIcon(null);
		p7C1.setBounds(164, 199, 48, 65);
		tablePanel.add(p7C1);
		
		p7Results = new JLabel("");
		p7Results.setHorizontalAlignment(SwingConstants.CENTER);
		p7Results.setForeground(Color.WHITE);
		p7Results.setBounds(140, 261, 114, 14);
		tablePanel.add(p7Results);
		
		p7Score = new JLabel("");
		p7Score.setForeground(new Color(255, 255, 255));
		p7Score.setBackground(new Color(255, 255, 255));
		p7Score.setHorizontalAlignment(SwingConstants.CENTER);
		p7Score.setFont(new Font("Tahoma", Font.BOLD, 15));
		p7Score.setBounds(16, 214, 86, 14);
		tablePanel.add(p7Score);
		
		p7Bet = new JLabel("");
		p7Bet.setHorizontalAlignment(SwingConstants.CENTER);
		p7Bet.setForeground(Color.WHITE);
		p7Bet.setBounds(58, 279, 73, 14);
		tablePanel.add(p7Bet);
		
		p7Name = new JLabel("");
		p7Name.setHorizontalAlignment(SwingConstants.CENTER);
		p7Name.setForeground(Color.WHITE);
		p7Name.setBounds(43, 254, 93, 14);
		tablePanel.add(p7Name);
		
		p6C5 = new JLabel("");
		p6C5.setIcon(null);
		p6C5.setBounds(307, 223, 48, 65);
		tablePanel.add(p6C5);
		
		p6C4 = new JLabel("");
		p6C4.setIcon(null);
		p6C4.setBounds(293, 237, 48, 65);
		tablePanel.add(p6C4);
		
		p6C3 = new JLabel("");
		p6C3.setIcon(null);
		p6C3.setBounds(279, 251, 48, 65);
		tablePanel.add(p6C3);
		
		p6C2 = new JLabel("");
		p6C2.setIcon(null);
		p6C2.setBounds(265, 264, 48, 65);
		tablePanel.add(p6C2);
		
		p6C1 = new JLabel("");
		p6C1.setIcon(null);
		p6C1.setBounds(251, 278, 48, 65);
		tablePanel.add(p6C1);
		
		p6Results = new JLabel("");
		p6Results.setHorizontalAlignment(SwingConstants.CENTER);
		p6Results.setForeground(Color.WHITE);
		p6Results.setBounds(247, 354, 108, 14);
		tablePanel.add(p6Results);
		
		p6Score = new JLabel("");
		p6Score.setForeground(new Color(255, 255, 255));
		p6Score.setBackground(new Color(255, 255, 255));
		p6Score.setHorizontalAlignment(SwingConstants.CENTER);
		p6Score.setFont(new Font("Tahoma", Font.BOLD, 15));
		p6Score.setBounds(112, 346, 86, 14);
		tablePanel.add(p6Score);
		
		p6Bet = new JLabel("");
		p6Bet.setHorizontalAlignment(SwingConstants.CENTER);
		p6Bet.setForeground(Color.WHITE);
		p6Bet.setBounds(168, 396, 73, 14);
		tablePanel.add(p6Bet);
		
		p6Name = new JLabel("");
		p6Name.setHorizontalAlignment(SwingConstants.CENTER);
		p6Name.setForeground(Color.WHITE);
		p6Name.setBounds(162, 371, 93, 14);
		tablePanel.add(p6Name);
		
		p5C5 = new JLabel("");
		p5C5.setIcon(null);
		p5C5.setBounds(423, 264, 48, 65);
		tablePanel.add(p5C5);
		
		p5C4 = new JLabel("");
		p5C4.setIcon(null);
		p5C4.setBounds(409, 278, 48, 65);
		tablePanel.add(p5C4);
		
		p5C3 = new JLabel("");
		p5C3.setIcon(null);
		p5C3.setBounds(395, 292, 48, 65);
		tablePanel.add(p5C3);
		
		p5C2 = new JLabel("");
		p5C2.setIcon(null);
		p5C2.setBounds(381, 306, 48, 65);
		tablePanel.add(p5C2);
		
		p5C1 = new JLabel("");
		p5C1.setIcon(null);
		p5C1.setBounds(367, 320, 48, 65);
		tablePanel.add(p5C1);
		
		p5Results = new JLabel("");
		p5Results.setHorizontalAlignment(SwingConstants.CENTER);
		p5Results.setForeground(Color.WHITE);
		p5Results.setBounds(348, 396, 123, 14);
		tablePanel.add(p5Results);
		
		p5Score = new JLabel("");
		p5Score.setForeground(new Color(255, 255, 255));
		p5Score.setBackground(new Color(255, 255, 255));
		p5Score.setHorizontalAlignment(SwingConstants.CENTER);
		p5Score.setFont(new Font("Tahoma", Font.BOLD, 15));
		p5Score.setBounds(269, 429, 86, 14);
		tablePanel.add(p5Score);
		
		p5Bet = new JLabel("");
		p5Bet.setHorizontalAlignment(SwingConstants.CENTER);
		p5Bet.setForeground(Color.WHITE);
		p5Bet.setBounds(337, 468, 73, 14);
		tablePanel.add(p5Bet);
		
		p5Name = new JLabel("");
		p5Name.setHorizontalAlignment(SwingConstants.CENTER);
		p5Name.setForeground(Color.WHITE);
		p5Name.setBounds(331, 443, 93, 14);
		tablePanel.add(p5Name);
		
		p4C5 = new JLabel("");
		p4C5.setIcon(null);
		p4C5.setBounds(508, 268, 48, 65);
		tablePanel.add(p4C5);
		
		p4C4 = new JLabel("");
		p4C4.setIcon(null);
		p4C4.setBounds(522, 282, 48, 65);
		tablePanel.add(p4C4);
		
		p4C3 = new JLabel("");
		p4C3.setIcon(null);
		p4C3.setBounds(536, 296, 48, 65);
		tablePanel.add(p4C3);
		
		p4C2 = new JLabel("");
		p4C2.setIcon(null);
		p4C2.setBounds(550, 310, 48, 65);
		tablePanel.add(p4C2);
		
		p4C1 = new JLabel("");
		p4C1.setIcon(null);
		p4C1.setBounds(564, 324, 48, 65);
		tablePanel.add(p4C1);
		
		p4Results = new JLabel("");
		p4Results.setHorizontalAlignment(SwingConstants.CENTER);
		p4Results.setForeground(Color.WHITE);
		p4Results.setBounds(508, 396, 123, 14);
		tablePanel.add(p4Results);
		
		p4Score = new JLabel("");
		p4Score.setForeground(new Color(255, 255, 255));
		p4Score.setBackground(new Color(255, 255, 255));
		p4Score.setHorizontalAlignment(SwingConstants.CENTER);
		p4Score.setFont(new Font("Tahoma", Font.BOLD, 15));
		p4Score.setBounds(622, 429, 86, 14);
		tablePanel.add(p4Score);
		
		p4Bet = new JLabel("");
		p4Bet.setForeground(new Color(255, 255, 255));
		p4Bet.setHorizontalAlignment(SwingConstants.CENTER);
		p4Bet.setBounds(562, 468, 73, 14);
		tablePanel.add(p4Bet);
		
		p4Name = new JLabel("");
		p4Name.setForeground(new Color(255, 255, 255));
		p4Name.setHorizontalAlignment(SwingConstants.CENTER);
		p4Name.setBounds(542, 443, 93, 14);
		tablePanel.add(p4Name);
		
		p3C5 = new JLabel("");
		p3C5.setIcon(null);
		p3C5.setBounds(636, 239, 48, 65);
		tablePanel.add(p3C5);
		
		p3C4 = new JLabel("");
		p3C4.setIcon(null);
		p3C4.setBounds(650, 253, 48, 65);
		tablePanel.add(p3C4);
		
		p3C3 = new JLabel("");
		p3C3.setIcon(null);
		p3C3.setBounds(664, 267, 48, 65);
		tablePanel.add(p3C3);
		
		p3C2 = new JLabel("");
		p3C2.setIcon(null);
		p3C2.setBounds(678, 281, 48, 65);
		tablePanel.add(p3C2);
		
		p3C1 = new JLabel("");
		p3C1.setIcon(null);
		p3C1.setBounds(692, 295, 48, 65);
		tablePanel.add(p3C1);
		
		p3Results = new JLabel("");
		p3Results.setHorizontalAlignment(SwingConstants.CENTER);
		p3Results.setForeground(Color.WHITE);
		p3Results.setBounds(622, 371, 104, 14);
		tablePanel.add(p3Results);
		
		p3Score = new JLabel("");
		p3Score.setForeground(new Color(255, 255, 255));
		p3Score.setBackground(new Color(255, 255, 255));
		p3Score.setHorizontalAlignment(SwingConstants.CENTER);
		p3Score.setFont(new Font("Tahoma", Font.BOLD, 15));
		p3Score.setBounds(778, 346, 86, 14);
		tablePanel.add(p3Score);
		
		p3Bet = new JLabel("");
		p3Bet.setForeground(new Color(255, 255, 255));
		p3Bet.setHorizontalAlignment(SwingConstants.CENTER);
		p3Bet.setBounds(718, 398, 93, 14);
		tablePanel.add(p3Bet);
		
		p3Name = new JLabel("");
		p3Name.setForeground(new Color(255, 255, 255));
		p3Name.setHorizontalAlignment(SwingConstants.CENTER);
		p3Name.setBounds(718, 371, 93, 14);
		tablePanel.add(p3Name);
		
		p2C5 = new JLabel("");
		p2C5.setIcon(null);
		p2C5.setBounds(726, 158, 48, 65);
		tablePanel.add(p2C5);
		
		p2C4 = new JLabel("");
		p2C4.setIcon(null);
		p2C4.setBounds(740, 172, 48, 65);
		tablePanel.add(p2C4);
		
		p2C3 = new JLabel("");
		p2C3.setIcon(null);
		p2C3.setBounds(754, 186, 48, 65);
		tablePanel.add(p2C3);
		
		p2C2 = new JLabel("");
		p2C2.setIcon(null);
		p2C2.setBounds(768, 200, 48, 65);
		tablePanel.add(p2C2);
		
		p2C1 = new JLabel("");
		p2C1.setIcon(null);
		p2C1.setBounds(782, 214, 48, 65);
		tablePanel.add(p2C1);
		
		p2Results = new JLabel("");
		p2Results.setHorizontalAlignment(SwingConstants.CENTER);
		p2Results.setForeground(Color.WHITE);
		p2Results.setBounds(722, 279, 108, 14);
		tablePanel.add(p2Results);
		
		p2Score = new JLabel("");
		p2Score.setForeground(new Color(255, 255, 255));
		p2Score.setBackground(new Color(255, 255, 255));
		p2Score.setHorizontalAlignment(SwingConstants.CENTER);
		p2Score.setFont(new Font("Tahoma", Font.BOLD, 15));
		p2Score.setBounds(872, 223, 86, 14);
		tablePanel.add(p2Score);
		
		p2Bet = new JLabel("");
		p2Bet.setForeground(new Color(255, 255, 255));
		p2Bet.setHorizontalAlignment(SwingConstants.CENTER);
		p2Bet.setBounds(839, 291, 73, 14);
		tablePanel.add(p2Bet);
		
		p2Name = new JLabel("");
		p2Name.setForeground(new Color(255, 255, 255));
		p2Name.setHorizontalAlignment(SwingConstants.CENTER);
		p2Name.setBounds(829, 265, 84, 14);
		tablePanel.add(p2Name);
		
		p1C5 = new JLabel("");
		p1C5.setIcon(null);
		p1C5.setBounds(771, 85, 48, 65);
		tablePanel.add(p1C5);
		
		p1C4 = new JLabel("");
		p1C4.setIcon(null);
		p1C4.setBounds(785, 85, 48, 65);
		tablePanel.add(p1C4);
		
		p1C3 = new JLabel("");
		p1C3.setIcon(null);
		p1C3.setBounds(799, 85, 48, 65);
		tablePanel.add(p1C3);
		
		p1C2 = new JLabel("");
		p1C2.setIcon(null);
		p1C2.setBounds(813, 85, 48, 65);
		tablePanel.add(p1C2);
		
		p1C1 = new JLabel("");
		p1C1.setIcon(null);
		p1C1.setBounds(827, 85, 48, 65);
		tablePanel.add(p1C1);
		
		p1Results = new JLabel("");
		p1Results.setForeground(Color.WHITE);
		p1Results.setHorizontalAlignment(SwingConstants.CENTER);
		p1Results.setBounds(784, 160, 91, 14);
		tablePanel.add(p1Results);
		
		p1Score = new JLabel("");
		p1Score.setForeground(new Color(255, 255, 255));
		p1Score.setBackground(new Color(255, 255, 255));
		p1Score.setHorizontalAlignment(SwingConstants.CENTER);
		p1Score.setFont(new Font("Tahoma", Font.BOLD, 15));
		p1Score.setBounds(882, 72, 86, 14);
		tablePanel.add(p1Score);
		
		p1Bet = new JLabel("");
		p1Bet.setForeground(new Color(255, 255, 255));
		p1Bet.setHorizontalAlignment(SwingConstants.CENTER);
		p1Bet.setBounds(885, 136, 73, 14);
		tablePanel.add(p1Bet);
		
		p1Name = new JLabel("");
		p1Name.setForeground(new Color(255, 255, 255));
		p1Name.setHorizontalAlignment(SwingConstants.CENTER);
		p1Name.setBounds(885, 111, 73, 14);
		tablePanel.add(p1Name);
		
		dC5 = new JLabel("");
		dC5.setIcon(null);
		dC5.setBounds(520, 69, 48, 65);
		tablePanel.add(dC5);
		
		dC4 = new JLabel("");
		dC4.setIcon(null);
		dC4.setBounds(492, 69, 48, 65);
		tablePanel.add(dC4);
		
		dC3 = new JLabel("");
		dC3.setIcon(null);
		dC3.setBounds(462, 69, 48, 65);
		tablePanel.add(dC3);
		
		dC2 = new JLabel("");
		dC2.setIcon(null);
		dC2.setBounds(434, 69, 48, 65);
		tablePanel.add(dC2);
		
		dC1 = new JLabel("");
		dC1.setIcon(null);
		dC1.setBounds(404, 69, 48, 65);
		tablePanel.add(dC1);
		
		dScore = new JLabel("");
		dScore.setHorizontalAlignment(SwingConstants.CENTER);
		dScore.setForeground(Color.WHITE);
		dScore.setFont(new Font("Tahoma", Font.BOLD, 15));
		dScore.setBackground(Color.WHITE);
		dScore.setBounds(423, 145, 117, 14);
		tablePanel.add(dScore);
		
		currentTable = new JLabel("");
		currentTable.setFont(new Font("Tahoma", Font.BOLD, 15));
		currentTable.setForeground(Color.WHITE);
		currentTable.setBounds(10, 11, 405, 19);
		tablePanel.add(currentTable);
		
		JLabel blackjackTable = new JLabel("");
		blackjackTable.setHorizontalAlignment(SwingConstants.CENTER);
		blackjackTable.setBackground(UIManager.getColor("Button.background"));
		blackjackTable.setIcon(new ImageIcon(GameClient.class.getResource("/images/BJtable.jpg")));
		blackjackTable.setBounds(0, 0, 968, 505);
		tablePanel.add(blackjackTable);
		
		p8 = new JLabel[]{p8Bet, p8Name, p8Score, p8C1, p8C2, p8C3, p8C4, p8C5};
		p7 = new JLabel[]{p7Bet, p7Name, p7Score, p7C1, p7C2, p7C3, p7C4, p7C5};
		p6 = new JLabel[]{p6Bet, p6Name, p6Score, p6C1, p6C2, p6C3, p6C4, p6C5};
		p5 = new JLabel[]{p5Bet, p5Name, p5Score, p5C1, p5C2, p5C3, p5C4, p5C5};
		p4 = new JLabel[]{p4Bet, p4Name, p4Score, p4C1, p4C2, p4C3, p4C4, p4C5};
		p3 = new JLabel[]{p3Bet, p3Name, p3Score, p3C1, p3C2, p3C3, p3C4, p3C5};
		p2 = new JLabel[]{p2Bet, p2Name, p2Score, p2C1, p2C2, p2C3, p2C4, p2C5};
		p1 = new JLabel[]{p1Bet, p1Name, p1Score, p1C1, p1C2, p1C3, p1C4, p1C5};
		d = new JLabel[]{dScore, dC1, dC2, dC3, dC4, dC5};
		_players = new JLabel[][]{p1, p2, p3, p4, p5, p6, p7, p8};
		_results = new JLabel[]{p1Results, p2Results, p3Results, p4Results, p5Results, p6Results, p7Results, p8Results};
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_1.gridx = 3;
		gbc_horizontalStrut_1.gridy = 1;
		frmBlackjack.getContentPane().add(horizontalStrut_1, gbc_horizontalStrut_1);
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 2;
		frmBlackjack.getContentPane().add(scrollPane, gbc_scrollPane);
		
		output = new JTextArea();
		output.setText("Please Login/Register by selecting File -> Login/Register.\r\n\r\nThank you and enjoy!");
		output.setEditable(false);
		output.setLineWrap(true);
		scrollPane.setViewportView(output);
		
		JPanel panelUserInput = new JPanel();
		GridBagConstraints gbc_panelUserInput = new GridBagConstraints();
		gbc_panelUserInput.insets = new Insets(0, 0, 5, 5);
		gbc_panelUserInput.gridheight = 2;
		gbc_panelUserInput.fill = GridBagConstraints.BOTH;
		gbc_panelUserInput.gridx = 2;
		gbc_panelUserInput.gridy = 2;
		frmBlackjack.getContentPane().add(panelUserInput, gbc_panelUserInput);
		GridBagLayout gbl_panelUserInput = new GridBagLayout();
		gbl_panelUserInput.columnWidths = new int[]{22, 50, 40, 21, 41, 0, 0, 0, 0, 0, 0};
		gbl_panelUserInput.rowHeights = new int[]{0, 0, 0, 0, 18, 0, 0, 0};
		gbl_panelUserInput.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelUserInput.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelUserInput.setLayout(gbl_panelUserInput);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 3;
		gbc_verticalStrut.gridy = 0;
		panelUserInput.add(verticalStrut, gbc_verticalStrut);
		
		lblMsg = new JLabel("Welcome to the game server!");
		lblMsg.setHorizontalAlignment(SwingConstants.CENTER);
		lblMsg.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblMsg = new GridBagConstraints();
		gbc_lblMsg.fill = GridBagConstraints.BOTH;
		gbc_lblMsg.gridwidth = 10;
		gbc_lblMsg.insets = new Insets(0, 0, 5, 5);
		gbc_lblMsg.gridx = 0;
		gbc_lblMsg.gridy = 1;
		panelUserInput.add(lblMsg, gbc_lblMsg);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea = new GridBagConstraints();
		gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea.gridx = 3;
		gbc_rigidArea.gridy = 2;
		panelUserInput.add(rigidArea, gbc_rigidArea);
		
		final JSlider slider = new JSlider();
		slider.setValue(100);
		slider.setMajorTickSpacing(100);
		slider.setMaximum(1000);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lblBet.setText("$" + slider.getValue());
			}
		});
		
		btnBet = new JButton("Bet");
		btnBet.setEnabled(false);
		btnBet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendString(slider.getValue()+"");
				btnBet.setEnabled(false);
			}
		});
		
		lblBet = new JLabel("$100");
		GridBagConstraints gbc_lblBet = new GridBagConstraints();
		gbc_lblBet.insets = new Insets(0, 0, 5, 5);
		gbc_lblBet.gridx = 2;
		gbc_lblBet.gridy = 3;
		panelUserInput.add(lblBet, gbc_lblBet);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(50);
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider.gridwidth = 5;
		gbc_slider.insets = new Insets(0, 0, 5, 5);
		gbc_slider.gridx = 3;
		gbc_slider.gridy = 3;
		panelUserInput.add(slider, gbc_slider);
		GridBagConstraints gbc_btnBet = new GridBagConstraints();
		gbc_btnBet.insets = new Insets(0, 0, 5, 5);
		gbc_btnBet.gridx = 8;
		gbc_btnBet.gridy = 3;
		panelUserInput.add(btnBet, gbc_btnBet);
		
		btnHit = new JButton("Hit");
		btnHit.setEnabled(false);
		btnHit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendString("yes");
			}
		});
		GridBagConstraints gbc_btnHit = new GridBagConstraints();
		gbc_btnHit.insets = new Insets(0, 0, 5, 5);
		gbc_btnHit.gridx = 3;
		gbc_btnHit.gridy = 5;
		panelUserInput.add(btnHit, gbc_btnHit);
		
		btnStand = new JButton("Stand");
		btnStand.setEnabled(false);
		btnStand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendString("no");
			}
		});
		GridBagConstraints gbc_btnStand = new GridBagConstraints();
		gbc_btnStand.insets = new Insets(0, 0, 5, 5);
		gbc_btnStand.gridx = 6;
		gbc_btnStand.gridy = 5;
		panelUserInput.add(btnStand, gbc_btnStand);
		
		input = new JTextField();
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = input.getText();
				if(!s.equals("") && connectionStatus == CONNECTED){
					//appendToOutput("Outgoing: " + s + "\n");
					//input.selectAll();
					//sendString("CHAT " + s);
				}
			}
		});
		GridBagConstraints gbc_input = new GridBagConstraints();
		gbc_input.insets = new Insets(0, 0, 5, 5);
		gbc_input.fill = GridBagConstraints.HORIZONTAL;
		gbc_input.gridx = 1;
		gbc_input.gridy = 3;
		frmBlackjack.getContentPane().add(input, gbc_input);
		input.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridheight = 2;
		gbc_panel_1.insets = new Insets(0, 0, 0, 5);
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 4;
		frmBlackjack.getContentPane().add(panel_1, gbc_panel_1);
		
		statusColor = new JTextField();
		statusColor.setBackground(new Color(255, 0, 0));
		statusColor.setBounds(29, 4, 20, 20);
		panel_1.add(statusColor);
		statusColor.setColumns(10);
		
		statusField = new JLabel("Disconnected");
		statusField.setFont(new Font("Tahoma", Font.BOLD, 13));
		statusField.setBounds(63, 7, 252, 14);
		panel_1.add(statusField);
		
		JLabel lblStats = new JLabel("Stats:");
		lblStats.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblStats.setBounds(484, 6, 46, 14);
		panel_1.add(lblStats);
		
		lblStatsValue = new JLabel("0-0-0");
		lblStatsValue.setBounds(532, 7, 170, 14);
		panel_1.add(lblStatsValue);
		
		JLabel lblNewLabel_1 = new JLabel("Money:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_1.setBounds(712, 7, 62, 14);
		panel_1.add(lblNewLabel_1);
		
		lblMoneyValue = new JLabel("$0.00");
		lblMoneyValue.setBounds(768, 7, 179, 14);
		panel_1.add(lblMoneyValue);
		
		btnLeaveTable = new JButton("Leave Table");//TODO
		btnLeaveTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendString("quit");
				btnLeaveTable.setEnabled(false);
			}
		});
		btnLeaveTable.setEnabled(false);
		btnLeaveTable.setBounds(325, 3, 123, 23);
		panel_1.add(btnLeaveTable);
		
		JMenuBar menuBar = new JMenuBar();
		frmBlackjack.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLogin = new JMenuItem("Login");
		mntmLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});
		mnFile.add(mntmLogin);
		
		JMenuItem mntmRegister = new JMenuItem("Register");
		mntmRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				register();
			}
		});
		mnFile.add(mntmRegister);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logout();
			}
		});
		mnFile.add(mntmLogout);
		
		frmBlackjack.setVisible(true);
	}
	
	private static void connect(){
		connectionStatus = BEGIN_CONNECT;
		statusString = statusMessages[connectionStatus];
		_gameClient.run();
	}
	
	private static void logout(){
		if(_out != null){
			_out.println("*L0gM30ut*"); _out.flush();
		}
		cleanUp();
		changeStatus(DISCONNECTED, true);
	}
	
	private static void login(){
		try {
			Object[] options = {"Login", "Cancel"};
			LoginPanel loginPanel = new LoginPanel();
			int n = JOptionPane.showOptionDialog(frmBlackjack, loginPanel, 
					"Login", JOptionPane.OK_CANCEL_OPTION, 
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			
			if(n == JOptionPane.OK_OPTION){
				connect();
				sendString("yes");
				sendString(loginPanel.getUsername());
				sendString(loginPanel.getPassword());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void register(){
		try {
			Object[] options = {"Register", "Cancel"};
			RegisterPanel registerPanel = new RegisterPanel();
			int n = JOptionPane.showOptionDialog(frmBlackjack, registerPanel,
					"Register", JOptionPane.OK_CANCEL_OPTION, 
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			
			String username = registerPanel.getUsername();
			String pass1 = registerPanel.getPassword1();
			String pass2 = registerPanel.getPassword2();
			String email = registerPanel.getEmail();
			
			if(n == JOptionPane.OK_OPTION){	
				connect();
				sendString("no");
				sendString(username);
				sendString(pass1);
				sendString(pass2);
				sendString(email);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void menuList(String add){
		try {
			Object[] options = {"OK", "Cancel"};
			MenuPanel menuPanel = new MenuPanel();
			menuPanel.addItems(add);
			int n = JOptionPane.showOptionDialog(frmBlackjack, menuPanel, 
					"Menu Selection", JOptionPane.OK_CANCEL_OPTION, 
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			
			if(n == JOptionPane.OK_OPTION){
				int selected = menuPanel.getSeletedIndex();
				if(selected == 0)
					btnLeaveTable.setEnabled(true);
				sendString(selected+"");
			}
			else
				sendString("" + -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void pop(String hold){
		JOptionPane pane = new JOptionPane( 
				new JLabel(hold,JLabel.CENTER), 
				JOptionPane.PLAIN_MESSAGE);
		JDialog dialog = pane.createDialog(frmBlackjack, "");
		dialog.setModal(false);
		dialog.setVisible(true);
	}
	
	private static void error(String hold){
		JOptionPane pane = new JOptionPane( 
				new JLabel(hold,JLabel.CENTER), 
				JOptionPane.ERROR_MESSAGE);
		JDialog dialog = pane.createDialog(frmBlackjack, "");
		dialog.setModal(false);
		dialog.setVisible(true);
	}
	
	private static void clearTable(){
		d[0].setText("");
		for(int i=1; i< d.length; i++){
			d[i].setIcon(null);
		}
		for(int i=0; i<_players.length;i++){
			_results[i].setText("");
			_players[i][0].setText("");
			_players[i][1].setText("");
			_players[i][2].setText("");
			for(int j=0; j< 5; j++){
				_players[i][j+3].setIcon(null);
			}
		}
	}
	
	private static void updateResults(String results){
		String[] updates = results.split("<>");
		for(int i=0; i<updates.length;i++){
			_results[i].setText(updates[i]);
		}
	}
	
	//TODO
	private static void updateTable(String hands){
		//System.out.println("Hands: " + hands);
		
		String[] players = hands.split("/");
		/*System.out.println("\nPlayers:");
		for(int i=0; i<players.length; i++)
			System.out.print(i+": " + players[i]+"\n");*/
		
		String[] parts = players[0].split("=");
		/*System.out.println("\nParts:");
		for(int i=0; i<parts.length; i++)
			System.out.print(i+": " + parts[i]+"\n");*/
		
		String[] cards = parts[2].split("<>");
		/*System.out.println("\nCards:");
		for(int i=0; i<cards.length; i++)
			System.out.print(i+": " + cards[i]+"\n");*/
		
		if(parts[1].equalsIgnoreCase("0"))
			d[0].setText("");//Dealer Score
		else
			d[0].setText(parts[1]);//Dealer Score
		
		for(int i=0; i< cards.length; i++){
			d[i+1].setIcon(new ImageIcon(GameClient.class.getResource(IMAGE_PATH + cards[i] + IMAGE_EXT)));
		}
		for(int i=1; i<players.length;i++){
			parts = players[i].split("=");
			_players[i-1][0].setText(parts[0]);
			_players[i-1][1].setText(parts[1]);
			_players[i-1][2].setText(parts[2]);
			cards = parts[3].split("<>");
			for(int j=0; j< cards.length; j++){
				_players[i-1][j+3].setIcon(new ImageIcon(GameClient.class.getResource(IMAGE_PATH + cards[j] + IMAGE_EXT)));
			}
		}
	}
	
	/*
	private static int openYesNoDialog(String hold){
		Object[] options = {"Yes", "No"};
		JOptionPane pane = new JOptionPane( 
				hold, 
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION,
				 null, options, options[0]);
		JDialog dialog = pane.createDialog(frmBlackjack, "Yes or No?");
		dialog.setModal(false);
		dialog.setVisible(true);
		Object selectedValue = pane.getValue();
		while(selectedValue == pane.UNINITIALIZED_VALUE);
		System.out.println("Selected value: " + selectedValue);
		for(int i = 0, maxCounter = options.length;
	    i < maxCounter; i++) {
			System.out.println("Options["+i+"] "+ options[i]);
		    if(options[i].equals(selectedValue))
		    	return i;
		}
		return -1;
	}*/
	
	/*
	private static void appendToOutput(String s) {
		synchronized (toAppend) {
		   toAppend.append(s);
		}
	}*/
	
	private static void sendString(String s){
		synchronized(toSend){
			toSend.append(s + "\n");
		}
	}

	private static void changeStatus(int newConnectStatus, boolean noError){
		if(newConnectStatus != NULL){
			connectionStatus = newConnectStatus;
		}
		
		if(noError)
			statusString = statusMessages[connectionStatus];
		else
			statusString = statusMessages[NULL];
		
		SwingUtilities.invokeLater(_gameClient);
	}
	
	@Override
	public void run() {
		switch(connectionStatus){
		case BEGIN_CONNECT:
			statusColor.setBackground(Color.ORANGE);
			break;
		case CONNECTED:
			statusColor.setBackground(Color.GREEN);
			break;
		case DISCONNECTING:
			statusColor.setBackground(Color.ORANGE);
			break;
		case DISCONNECTED:
			statusColor.setBackground(Color.RED);
			break;
		default:
			break;	
		}			
		
		statusField.setText(statusString);
		output.append(toAppend.toString());
	    toAppend.setLength(0);
	    output.setCaretPosition(output.getDocument().getLength()); //Autoscroll
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
		
	    frmBlackjack.repaint();			
	}
}
