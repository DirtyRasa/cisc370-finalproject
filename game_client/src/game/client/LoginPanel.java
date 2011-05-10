package game.client;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.JPasswordField;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {
	private JTextField textFieldUsername;
	private JPasswordField passwordField;

	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblUsername = new JLabel("Username:");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.gridx = 2;
		gbc_lblUsername.gridy = 2;
		add(lblUsername, gbc_lblUsername);
		
		textFieldUsername = new JTextField();
		GridBagConstraints gbc_textFieldUsername = new GridBagConstraints();
		gbc_textFieldUsername.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldUsername.gridx = 3;
		gbc_textFieldUsername.gridy = 2;
		add(textFieldUsername, gbc_textFieldUsername);
		textFieldUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 0, 5);
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.gridx = 2;
		gbc_lblPassword.gridy = 3;
		add(lblPassword, gbc_lblPassword);
		
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 0, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 3;
		gbc_passwordField.gridy = 3;
		add(passwordField, gbc_passwordField);

		textFieldUsername.requestFocus();
	}
	
	public String getUsername(){return textFieldUsername.getText();}
	
	public String getPassword(){return new String(passwordField.getPassword());}
}
