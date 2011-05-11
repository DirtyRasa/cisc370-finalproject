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
import javax.swing.border.EtchedBorder;

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
		gridBagLayout.rowHeights = new int[]{0, 510, 124, 0, 28, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
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
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.RAISED, new Color(255, 255, 0), new Color(255, 255, 0)));
		panel.setBackground(new Color(0, 128, 0));
		panel.setLayout(null);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 1;
		frmBlackjack.getContentPane().add(panel, gbc_panel);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(GameClient.class.getResource("/images/Ace of Spades.jpg")));
		lblNewLabel.setBounds(371, 100, 71, 96);
		panel.add(lblNewLabel);
		
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
		gbl_panelUserInput.columnWidths = new int[]{22, 50, 40, 21, 64, 0, 0};
		gbl_panelUserInput.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelUserInput.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelUserInput.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelUserInput.setLayout(gbl_panelUserInput);
		
		Component verticalStrut_3 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_3 = new GridBagConstraints();
		gbc_verticalStrut_3.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_3.gridx = 2;
		gbc_verticalStrut_3.gridy = 0;
		panelUserInput.add(verticalStrut_3, gbc_verticalStrut_3);
		
		lblBet = new JLabel("$50.00");
		GridBagConstraints gbc_lblBet = new GridBagConstraints();
		gbc_lblBet.insets = new Insets(0, 0, 5, 5);
		gbc_lblBet.gridx = 1;
		gbc_lblBet.gridy = 1;
		panelUserInput.add(lblBet, gbc_lblBet);
		
		final JSlider slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lblBet.setText("$" + slider.getValue());
			}
		});
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(10);
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider.gridwidth = 2;
		gbc_slider.insets = new Insets(0, 0, 5, 5);
		gbc_slider.gridx = 2;
		gbc_slider.gridy = 1;
		panelUserInput.add(slider, gbc_slider);
		
		JButton btnBet = new JButton("Bet");
		btnBet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendString(slider.getValue()+"");
			}
		});
		GridBagConstraints gbc_btnBet = new GridBagConstraints();
		gbc_btnBet.insets = new Insets(0, 0, 5, 5);
		gbc_btnBet.gridx = 4;
		gbc_btnBet.gridy = 1;
		panelUserInput.add(btnBet, gbc_btnBet);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 2;
		gbc_verticalStrut.gridy = 2;
		panelUserInput.add(verticalStrut, gbc_verticalStrut);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_2 = new GridBagConstraints();
		gbc_horizontalStrut_2.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_2.gridx = 5;
		gbc_horizontalStrut_2.gridy = 2;
		panelUserInput.add(horizontalStrut_2, gbc_horizontalStrut_2);
		
		JButton btnHit = new JButton("Hit");
		btnHit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendString("yes");
			}
		});
		GridBagConstraints gbc_btnHit = new GridBagConstraints();
		gbc_btnHit.insets = new Insets(0, 0, 5, 5);
		gbc_btnHit.gridx = 1;
		gbc_btnHit.gridy = 3;
		panelUserInput.add(btnHit, gbc_btnHit);
		
		JButton btnNewButton = new JButton("Stand");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendString("no");
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 3;
		panelUserInput.add(btnNewButton, gbc_btnNewButton);
		
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
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
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
		statusField.setBounds(63, 7, 92, 14);
		panel_1.add(statusField);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblUsername.setBounds(267, 7, 69, 14);
		panel_1.add(lblUsername);
		
		JLabel lblUsernameValue = new JLabel("New label");
		lblUsernameValue.setBounds(346, 7, 46, 14);
		panel_1.add(lblUsernameValue);
		
		JLabel lblStats = new JLabel("Stats:");
		lblStats.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblStats.setBounds(485, 7, 46, 14);
		panel_1.add(lblStats);
		
		JLabel lblStatsValue = new JLabel("New label");
		lblStatsValue.setBounds(532, 7, 46, 14);
		panel_1.add(lblStatsValue);
		
		JLabel lblNewLabel_1 = new JLabel("Money:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_1.setBounds(712, 7, 62, 14);
		panel_1.add(lblNewLabel_1);
		
		JLabel lblMoneyValue = new JLabel("$");
		lblMoneyValue.setBounds(768, 7, 46, 14);
		panel_1.add(lblMoneyValue);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_2 = new GridBagConstraints();
		gbc_verticalStrut_2.insets = new Insets(0, 0, 0, 5);
		gbc_verticalStrut_2.gridx = 1;
		gbc_verticalStrut_2.gridy = 5;
		frmBlackjack.getContentPane().add(verticalStrut_2, gbc_verticalStrut_2);
		
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
