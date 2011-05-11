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
	
	public final static String statusMessages[] = {
		" Error! Could not connect!", " Disconnected",
		" Disconnecting...", " Connecting...", " Connected"
	};
	public static String _hostIP = "localhost";
	   								//"140.209.123.186"; //OSS-LL12_01
	   								//"140.209.122.249"; //Prof
									//"140.209.226.160"; //Josh
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
						lblMsg.setText("");
					}
					
					if(_in.ready()){
						hold = _in.readLine();
						if(hold != null && hold.length() != 0){
							if(hold.equals("end"))
								changeStatus(DISCONNECTING, true);
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
								menuList(hold.substring(4));
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
							else if(hold.startsWith("ERROR")){
								JOptionPane.showMessageDialog(frmBlackjack, 
										hold.substring(5), 
										"Error", JOptionPane.ERROR_MESSAGE, null);
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
		frmBlackjack.setTitle("Game Client");
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
		
		JLabel blackjackTable = new JLabel("");
		blackjackTable.setHorizontalAlignment(SwingConstants.CENTER);
		blackjackTable.setBackground(UIManager.getColor("Button.background"));
		blackjackTable.setIcon(new ImageIcon(GameClient.class.getResource("/images/BJtable.jpg")));
		blackjackTable.setBounds(0, 0, 968, 505);
		tablePanel.add(blackjackTable);
		
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
			}
		});
		
		lblBet = new JLabel("$50.00");
		GridBagConstraints gbc_lblBet = new GridBagConstraints();
		gbc_lblBet.insets = new Insets(0, 0, 5, 5);
		gbc_lblBet.gridx = 2;
		gbc_lblBet.gridy = 3;
		panelUserInput.add(lblBet, gbc_lblBet);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(10);
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
		
		btnHit = new JButton("Hit/Yes");
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
		
		btnStand = new JButton("Stand/No");
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
		statusField.setBounds(63, 7, 392, 14);
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
		
		lblMoneyValue = new JLabel("$0");
		lblMoneyValue.setBounds(768, 7, 179, 14);
		panel_1.add(lblMoneyValue);
		
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
				sendString(menuPanel.getSeletedIndex()+"");
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
	    output.setCaretPosition(output.getDocument().getLength()); //Autoscroll
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
		
	    frmBlackjack.repaint();			
	}
}
