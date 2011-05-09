package game.client;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JSeparator;
import javax.swing.border.BevelBorder;


public class GameClient implements Runnable{
	public final static GameClient _gameClient = new GameClient();
	
	public final static int NULL = 0;
	public final static int DISCONNECTED = 1;
	public final static int DISCONNECTING = 2;
	public final static int BEGIN_CONNECT = 3;
	public final static int CONNECTED = 4;	
	
	public final static String statusMessages[] = {
		" Error! Could not connect!", " Disconnected",
		" Disconnecting...", " Connecting...", " Connected"
	};
	
	public static JFrame frmBlackjack;
	public static JTextField input;
	public static JTextArea output;
	
	public static String _hostIP = "localhost";
	public static int _port = 80;
	public static Socket _client = null;
	public static PrintWriter _out = null;
	public static BufferedReader _in = null;
	
	public static StringBuffer toAppend = new StringBuffer("");
	public static StringBuffer toSend = new StringBuffer("");
	
	public static int connectionStatus = DISCONNECTED;
	public static String statusString = statusMessages[connectionStatus];
	private static JTextField statusColor;
	private static JLabel statusField;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length > 0)
			_hostIP = args[0];
		
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
					}
					
					if(_in.ready()){
						hold = _in.readLine();
						if(hold != null && hold.length() != 0){
							if(hold.equals("end"))
								changeStatus(DISCONNECTING, true);
							else if(hold.startsWith("REGISTER")){
								if(!hold.contains("success")){
									JOptionPane.showMessageDialog(frmBlackjack, 
										hold.substring(8, hold.length()), 
										"Register Error", JOptionPane.ERROR_MESSAGE, null);
									changeStatus(DISCONNECTED, true);
								}
								else
									changeStatus(NULL, true);
							}
							else if(hold.startsWith("LOGIN")){
								if(!hold.contains("success")){
									JOptionPane.showMessageDialog(frmBlackjack, 
										hold.substring(5, hold.length()), 
										"Warning", JOptionPane.ERROR_MESSAGE, null);
									changeStatus(DISCONNECTED, true);
								}
								else
									changeStatus(NULL, true);
							}
							else{
								appendToOutput(hold + "\n");
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

	/**
	 * Create the application.
	 */
	/*public GameClient() {
		initialize();
	}*/

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
	 * Initialize the contents of the frame.
	 */
	private static void initialize() {
		frmBlackjack = new JFrame();
		frmBlackjack.getContentPane().setBackground(Color.LIGHT_GRAY);
		frmBlackjack.setTitle("Blackjack");
		frmBlackjack.setBounds(100, 100, 800, 600);
		frmBlackjack.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{30, 719, 0, 0};
		gridBagLayout.rowHeights = new int[]{30, 416, 40, 0, 0, 20, 0, 5, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		frmBlackjack.getContentPane().setLayout(gridBagLayout);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 1;
		gbc_verticalStrut.gridy = 0;
		frmBlackjack.getContentPane().add(verticalStrut, gbc_verticalStrut);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut_1.gridx = 0;
		gbc_horizontalStrut_1.gridy = 1;
		frmBlackjack.getContentPane().add(horizontalStrut_1, gbc_horizontalStrut_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		frmBlackjack.getContentPane().add(scrollPane, gbc_scrollPane);
		
		output = new JTextArea();
		output.setText("Please login or register to connect to the game server.\r\nFile -> Login OR File -> Register\r\n\r\n\r\n\r\n\r\n");
		output.setEditable(false);
		output.setLineWrap(true);
		scrollPane.setViewportView(output);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut.gridx = 2;
		gbc_horizontalStrut.gridy = 1;
		frmBlackjack.getContentPane().add(horizontalStrut, gbc_horizontalStrut);
		
		input = new JTextField();
		input.setText("<Enter a message here>");
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = input.getText();
				if(!s.equals("") && connectionStatus == CONNECTED){
					appendToOutput("Outgoing: " + s + "\n");
					input.selectAll();
					sendString(s);
				}
			}
		});
		GridBagConstraints gbc_input = new GridBagConstraints();
		gbc_input.insets = new Insets(0, 0, 5, 5);
		gbc_input.fill = GridBagConstraints.HORIZONTAL;
		gbc_input.gridx = 1;
		gbc_input.gridy = 2;
		frmBlackjack.getContentPane().add(input, gbc_input);
		input.setColumns(10);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_2 = new GridBagConstraints();
		gbc_verticalStrut_2.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_2.gridx = 1;
		gbc_verticalStrut_2.gridy = 3;
		frmBlackjack.getContentPane().add(verticalStrut_2, gbc_verticalStrut_2);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 4;
		frmBlackjack.getContentPane().add(panel, gbc_panel);
		
		statusColor = new JTextField();
		statusColor.setBackground(Color.RED);
		panel.add(statusColor);
		statusColor.setColumns(2);
		
		statusField = new JLabel("Disconnected");
		statusField.setFont(new Font("Tahoma", Font.BOLD, 15));
		statusField.setForeground(Color.BLACK);
		panel.add(statusField);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_1.gridx = 1;
		gbc_verticalStrut_1.gridy = 5;
		frmBlackjack.getContentPane().add(verticalStrut_1, gbc_verticalStrut_1);
		
		JMenuBar menuBar = new JMenuBar();
		frmBlackjack.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLogin = new JMenuItem("Login");
		mntmLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		mnFile.add(mntmLogin);
		
		JMenuItem mntmRegister = new JMenuItem("Register");
		mntmRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				register();
			}
		});
		mnFile.add(mntmRegister);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendString("LOGOUT");
				cleanUp();
				changeStatus(DISCONNECTED, true);
			}
		});
		mnFile.add(mntmLogout);
		frmBlackjack.setVisible(true);
	}

	private static void connect(){
		connectionStatus = BEGIN_CONNECT;
		_gameClient.run();
	}
	
	private static void login(){
		try {
			Object[] options = {"Login", "Cancel"};
			LoginPanel loginPanel = new LoginPanel();
			int n = JOptionPane.showOptionDialog(frmBlackjack, loginPanel, 
					"Login", JOptionPane.YES_NO_OPTION, 
					JOptionPane.PLAIN_MESSAGE, null, options, null);
			
			if(n == JOptionPane.YES_OPTION){
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
					"Register", JOptionPane.YES_NO_OPTION, 
					JOptionPane.PLAIN_MESSAGE, null, options, null);
			
			if(n == JOptionPane.YES_OPTION){
				String username = registerPanel.getUsername();
				String pass1 = registerPanel.getPassword1();
				String pass2 = registerPanel.getPassword2();
				String email = registerPanel.getEmail();
				
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
	
	private static void appendToOutput(String s) {
		synchronized (toAppend) {
		   toAppend.append(s);
		}
	}
	
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
		
	    frmBlackjack.repaint();			
	}
}