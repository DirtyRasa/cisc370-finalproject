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
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class GameClient implements Runnable{
	public final static GameClient _gameClient = new GameClient();
	
	public final static int NULL = 0;
	public final static int DISCONNECTED = 1;
	public final static int CONNECTED = 2;
	public final static int DISCONNECTING = 3;
	public final static int BEGIN_CONNECT = 4;
	
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
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		initialize();
		
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameClient window = new GameClient();
					window.frmBlackjack.setVisible(true);
					
					window.output.append(toAppend.toString());
				    toAppend.setLength(0);
					
				    window.frmBlackjack.repaint();	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
			
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
					boolean isReady = _in.ready();
					System.out.println("isReady: " + isReady);
					if(isReady){
						hold = _in.readLine();
						if(hold != null && hold.length() != 0){
							System.out.println("Read: " + hold);
							if(hold.equals("end"))
								changeStatus(DISCONNECTING, true);
							else{
								appendToOutput(hold);
								changeStatus(NULL, true);
							}
						}
					}
					isReady = false;
				} catch (IOException e) {
					e.printStackTrace();
					cleanUp();
					changeStatus(DISCONNECTED, false);
				}
				break;
			case DISCONNECTING:
				cleanUp();
				connectionStatus = DISCONNECTED;
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
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frmBlackjack.getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 1;
		frmBlackjack.getContentPane().add(panel, gbc_panel);
		
		output = new JTextArea();
		output.setLineWrap(true);
		output.setEditable(false);
		output.setColumns(88);
		output.setRows(22);
		panel.add(output);
		
		input = new JTextField();
		input.addActionListener(new ActionAdapter() {
			public void actionPerformed(ActionEvent e) {
				String s = input.getText();
				if(!s.equals("")){
					appendToOutput("Outgoing: " + s + "\n");
					input.selectAll();
					sendString(s);
				}
			}
		});
		GridBagConstraints gbc_input = new GridBagConstraints();
		gbc_input.insets = new Insets(5, 5, 5, 5);
		gbc_input.fill = GridBagConstraints.HORIZONTAL;
		gbc_input.gridx = 1;
		gbc_input.gridy = 3;
		frmBlackjack.getContentPane().add(input, gbc_input);
		input.setColumns(10);
		
		JMenuBar menuBar = new JMenuBar();
		frmBlackjack.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmConnect = new JMenuItem("Connect");
		mntmConnect.addActionListener(new ActionAdapter() {
			public void actionPerformed(ActionEvent e) {
				connectionStatus = BEGIN_CONNECT;
				_gameClient.run();
			}
		});
		mnFile.add(mntmConnect);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmLogin = new JMenuItem("Login");
		mntmLogin.addActionListener(new ActionAdapter() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mnFile.add(mntmLogin);
		
		JMenuItem mntmRegister = new JMenuItem("Register");
		mntmRegister.addActionListener(new ActionAdapter() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mnFile.add(mntmRegister);
		frmBlackjack.setVisible(true);
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
		
		SwingUtilities.invokeLater(_gameClient);
	}
	
	@Override
	public void run() {
		if(connectionStatus == DISCONNECTED)
			System.exit(0);
		
		output.append(toAppend.toString());
	    toAppend.setLength(0);
		
	    frmBlackjack.repaint();			
	}
}

class ActionAdapter implements ActionListener {
	public void actionPerformed(ActionEvent e) {}
}