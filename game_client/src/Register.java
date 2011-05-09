



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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Register extends JFrame {
	private JTextField textFieldUsername;
	private JTextField textFieldEmail;
	private JPasswordField passwordField_2;
	private JPasswordField passwordField_1;

	public Socket _client;
	public PrintWriter _out;
	public BufferedReader _in;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register frame = new Register(null, null, null);
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
	public Register(Socket client, PrintWriter out, BufferedReader in) {
		_client = client;
		_out = out;
		_in = in;
		setTitle("Register");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 361, 215);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 80, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		GridBagConstraints gbc_rigidArea = new GridBagConstraints();
		gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea.gridx = 1;
		gbc_rigidArea.gridy = 0;
		getContentPane().add(rigidArea, gbc_rigidArea);
		
		JLabel lblNewLabel = new JLabel("Username:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 1;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		textFieldUsername = new JTextField();
		GridBagConstraints gbc_textFieldUsername = new GridBagConstraints();
		gbc_textFieldUsername.gridwidth = 2;
		gbc_textFieldUsername.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldUsername.gridx = 3;
		gbc_textFieldUsername.gridy = 1;
		getContentPane().add(textFieldUsername, gbc_textFieldUsername);
		textFieldUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 2;
		gbc_lblPassword.gridy = 2;
		getContentPane().add(lblPassword, gbc_lblPassword);
		
		passwordField_1 = new JPasswordField();
		GridBagConstraints gbc_passwordField_1 = new GridBagConstraints();
		gbc_passwordField_1.gridwidth = 2;
		gbc_passwordField_1.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField_1.gridx = 3;
		gbc_passwordField_1.gridy = 2;
		getContentPane().add(passwordField_1, gbc_passwordField_1);
		
		JLabel lblRetypePassword = new JLabel("Re-Type Password:");
		GridBagConstraints gbc_lblRetypePassword = new GridBagConstraints();
		gbc_lblRetypePassword.anchor = GridBagConstraints.EAST;
		gbc_lblRetypePassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblRetypePassword.gridx = 2;
		gbc_lblRetypePassword.gridy = 3;
		getContentPane().add(lblRetypePassword, gbc_lblRetypePassword);
		
		passwordField_2 = new JPasswordField();
		GridBagConstraints gbc_passwordField_2 = new GridBagConstraints();
		gbc_passwordField_2.gridwidth = 2;
		gbc_passwordField_2.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField_2.gridx = 3;
		gbc_passwordField_2.gridy = 3;
		getContentPane().add(passwordField_2, gbc_passwordField_2);
		
		JLabel lblEmail = new JLabel("E-Mail");
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.EAST;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 2;
		gbc_lblEmail.gridy = 4;
		getContentPane().add(lblEmail, gbc_lblEmail);
		
		textFieldEmail = new JTextField();
		GridBagConstraints gbc_textFieldEmail = new GridBagConstraints();
		gbc_textFieldEmail.gridwidth = 2;
		gbc_textFieldEmail.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldEmail.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldEmail.gridx = 3;
		gbc_textFieldEmail.gridy = 4;
		getContentPane().add(textFieldEmail, gbc_textFieldEmail);
		textFieldEmail.setColumns(10);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				register();
			}
		});
		GridBagConstraints gbc_btnRegister = new GridBagConstraints();
		gbc_btnRegister.anchor = GridBagConstraints.EAST;
		gbc_btnRegister.insets = new Insets(0, 0, 5, 5);
		gbc_btnRegister.gridx = 3;
		gbc_btnRegister.gridy = 5;
		getContentPane().add(btnRegister, gbc_btnRegister);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancel();
			}
		});
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.anchor = GridBagConstraints.EAST;
		gbc_btnCancel.insets = new Insets(0, 0, 5, 5);
		gbc_btnCancel.gridx = 4;
		gbc_btnCancel.gridy = 5;
		getContentPane().add(btnCancel, gbc_btnCancel);
	}
	
	private void cancel(){
		send("User Canceled");
		try {
			LogIn frame = new LogIn(_client, _out, _in);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeWindow();
	}
	
	private void register(){
		String username = textFieldUsername.getText();
		String password1 = new String(passwordField_1.getPassword());
		String password2 = new String(passwordField_2.getPassword());
		String email = textFieldEmail.getText();
		
		if(!username.matches("^[a-zA-Z0-9_-]{3,20}$")){
			JOptionPane.showMessageDialog(this, "The username '" + username +"' contains illegal characters",
					"Illegal characters", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//TODO Add constraints on password
		if(!password1.equals(password2)){
			JOptionPane.showMessageDialog(this, "Passwords do not match",
					"Already logged on", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//TODO check email validity.
		
		send(username);
		send(password1);
		send(email);
		
		try {
			String hold = _in.readLine();
			if(hold.equalsIgnoreCase("already"))
				JOptionPane.showMessageDialog(this, "A user with the name '"+ username + "' already exists.",
						"Already Exists", JOptionPane.ERROR_MESSAGE);
			else if(hold.equalsIgnoreCase("error"))
				JOptionPane.showMessageDialog(this, "Error!",
						"Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		closeWindow();
	}
	
	private void closeWindow(){
		setVisible(false);
		dispose();
	}
	
	private void send(String msg){
		_out.println(msg);
		_out.flush();
	}
}
