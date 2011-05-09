package game.client;



import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LogIn extends JFrame {
	private JTextField textFieldUsername;
	private JPasswordField passwordField;
	
	public Socket _client = null;
	public PrintWriter _out = null;
	public BufferedReader _in = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Socket client = new Socket(_hostIP, _port);
					//BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					//PrintWriter out = new PrintWriter(client.getOutputStream(), true);
					LogIn frame = new LogIn(null, null, null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LogIn(Socket client, PrintWriter out, BufferedReader in) {
		_client = client;
		_out = out;
		_in = in;
		setResizable(false);
		setTitle("Blackjack");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 138, 128, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea = new GridBagConstraints();
		gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea.gridx = 0;
		gbc_rigidArea.gridy = 0;
		getContentPane().add(rigidArea, gbc_rigidArea);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_1 = new GridBagConstraints();
		gbc_rigidArea_1.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_1.gridx = 1;
		gbc_rigidArea_1.gridy = 1;
		getContentPane().add(rigidArea_1, gbc_rigidArea_1);
		
		Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_2 = new GridBagConstraints();
		gbc_rigidArea_2.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_2.gridx = 2;
		gbc_rigidArea_2.gridy = 2;
		getContentPane().add(rigidArea_2, gbc_rigidArea_2);
		
		Component rigidArea_3 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_3 = new GridBagConstraints();
		gbc_rigidArea_3.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_3.gridx = 4;
		gbc_rigidArea_3.gridy = 3;
		getContentPane().add(rigidArea_3, gbc_rigidArea_3);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setDisplayedMnemonic('U');
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 5;
		gbc_lblUsername.gridy = 3;
		getContentPane().add(lblUsername, gbc_lblUsername);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setToolTipText("");
		lblUsername.setLabelFor(textFieldUsername);
		GridBagConstraints gbc_textFieldUsername = new GridBagConstraints();
		gbc_textFieldUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldUsername.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldUsername.gridx = 6;
		gbc_textFieldUsername.gridy = 3;
		getContentPane().add(textFieldUsername, gbc_textFieldUsername);
		textFieldUsername.setColumns(1);
		
		Component rigidArea_4 = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea_4 = new GridBagConstraints();
		gbc_rigidArea_4.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea_4.gridx = 3;
		gbc_rigidArea_4.gridy = 4;
		getContentPane().add(rigidArea_4, gbc_rigidArea_4);
		
		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 5;
		gbc_lblPassword.gridy = 4;
		getContentPane().add(lblPassword, gbc_lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setToolTipText("Password is case sensitive.");
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 6;
		gbc_passwordField.gridy = 4;
		getContentPane().add(passwordField, gbc_passwordField);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.anchor = GridBagConstraints.EAST;
		gbc_btnLogin.insets = new Insets(0, 0, 5, 5);
		gbc_btnLogin.gridx = 6;
		gbc_btnLogin.gridy = 5;
		getContentPane().add(btnLogin, gbc_btnLogin);
		
		JLabel lblDontHaveAn = new JLabel("Don't have an account? ");
		GridBagConstraints gbc_lblDontHaveAn = new GridBagConstraints();
		gbc_lblDontHaveAn.anchor = GridBagConstraints.EAST;
		gbc_lblDontHaveAn.gridwidth = 5;
		gbc_lblDontHaveAn.insets = new Insets(0, 0, 0, 5);
		gbc_lblDontHaveAn.gridx = 1;
		gbc_lblDontHaveAn.gridy = 8;
		getContentPane().add(lblDontHaveAn, gbc_lblDontHaveAn);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				register();
			}
		});
		GridBagConstraints gbc_btnRegister = new GridBagConstraints();
		gbc_btnRegister.anchor = GridBagConstraints.EAST;
		gbc_btnRegister.insets = new Insets(0, 0, 0, 5);
		gbc_btnRegister.gridx = 6;
		gbc_btnRegister.gridy = 8;
		getContentPane().add(btnRegister, gbc_btnRegister);
	}
	
	private void login(){
		send("yes");
		String username = textFieldUsername.getText();
		String password = new String(passwordField.getPassword());
		textFieldUsername.setText("");
		passwordField.setText("");
		send(username);
		send(password);
		try {
			String hold = _in.readLine();
			if(hold.equalsIgnoreCase("already"))
				JOptionPane.showMessageDialog(this, "A user with the name '"+ username + "' is already logged on.",
						"Already logged on", JOptionPane.ERROR_MESSAGE);
			else if(hold.equalsIgnoreCase("invalid"))
				JOptionPane.showMessageDialog(this, "Invalid username or password combination",
						"Invalid Username/Password", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeWindow();
	}
	
	private void register(){
		send("no");
		try {
			Register frame = new Register(_client, _out, _in);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeWindow();
	}
	
	private void send(String msg){
		_out.println(msg);
		_out.flush();
	}
	
	private void closeWindow(){
		setVisible(false);
		dispose();
	}
}
