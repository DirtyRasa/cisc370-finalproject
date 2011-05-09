package game.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Register extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldUsername;
	private JTextField textFieldEmail;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Register dialog = new Register();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Register() {
		setTitle("Register");
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			Component verticalStrut = Box.createVerticalStrut(20);
			GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
			gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
			gbc_verticalStrut.gridx = 0;
			gbc_verticalStrut.gridy = 0;
			contentPanel.add(verticalStrut, gbc_verticalStrut);
		}
		{
			Component verticalStrut = Box.createVerticalStrut(20);
			GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
			gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
			gbc_verticalStrut.gridx = 1;
			gbc_verticalStrut.gridy = 1;
			contentPanel.add(verticalStrut, gbc_verticalStrut);
		}
		{
			JLabel lblUsername = new JLabel("Username:");
			GridBagConstraints gbc_lblUsername = new GridBagConstraints();
			gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
			gbc_lblUsername.anchor = GridBagConstraints.EAST;
			gbc_lblUsername.gridx = 2;
			gbc_lblUsername.gridy = 2;
			contentPanel.add(lblUsername, gbc_lblUsername);
		}
		{
			textFieldUsername = new JTextField();
			GridBagConstraints gbc_textFieldUsername = new GridBagConstraints();
			gbc_textFieldUsername.insets = new Insets(0, 0, 5, 5);
			gbc_textFieldUsername.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldUsername.gridx = 3;
			gbc_textFieldUsername.gridy = 2;
			contentPanel.add(textFieldUsername, gbc_textFieldUsername);
			textFieldUsername.setColumns(10);
		}
		{
			JLabel lblPassword = new JLabel("Password:");
			GridBagConstraints gbc_lblPassword = new GridBagConstraints();
			gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
			gbc_lblPassword.anchor = GridBagConstraints.EAST;
			gbc_lblPassword.gridx = 2;
			gbc_lblPassword.gridy = 3;
			contentPanel.add(lblPassword, gbc_lblPassword);
		}
		{
			passwordField = new JPasswordField();
			GridBagConstraints gbc_passwordField = new GridBagConstraints();
			gbc_passwordField.insets = new Insets(0, 0, 5, 5);
			gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
			gbc_passwordField.gridx = 3;
			gbc_passwordField.gridy = 3;
			contentPanel.add(passwordField, gbc_passwordField);
		}
		{
			JLabel lblRetypePassword = new JLabel("Re-type Password:");
			GridBagConstraints gbc_lblRetypePassword = new GridBagConstraints();
			gbc_lblRetypePassword.insets = new Insets(0, 0, 5, 5);
			gbc_lblRetypePassword.anchor = GridBagConstraints.EAST;
			gbc_lblRetypePassword.gridx = 2;
			gbc_lblRetypePassword.gridy = 4;
			contentPanel.add(lblRetypePassword, gbc_lblRetypePassword);
		}
		{
			passwordField_1 = new JPasswordField();
			GridBagConstraints gbc_passwordField_1 = new GridBagConstraints();
			gbc_passwordField_1.insets = new Insets(0, 0, 5, 5);
			gbc_passwordField_1.fill = GridBagConstraints.HORIZONTAL;
			gbc_passwordField_1.gridx = 3;
			gbc_passwordField_1.gridy = 4;
			contentPanel.add(passwordField_1, gbc_passwordField_1);
		}
		{
			JLabel lblEmail = new JLabel("E-mail:");
			GridBagConstraints gbc_lblEmail = new GridBagConstraints();
			gbc_lblEmail.insets = new Insets(0, 0, 0, 5);
			gbc_lblEmail.anchor = GridBagConstraints.EAST;
			gbc_lblEmail.gridx = 2;
			gbc_lblEmail.gridy = 5;
			contentPanel.add(lblEmail, gbc_lblEmail);
		}
		{
			textFieldEmail = new JTextField();
			GridBagConstraints gbc_textFieldEmail = new GridBagConstraints();
			gbc_textFieldEmail.insets = new Insets(0, 0, 0, 5);
			gbc_textFieldEmail.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldEmail.gridx = 3;
			gbc_textFieldEmail.gridy = 5;
			contentPanel.add(textFieldEmail, gbc_textFieldEmail);
			textFieldEmail.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton registerButton = new JButton("Register");
				registerButton.setActionCommand("REGISTER");
				buttonPane.add(registerButton);
				getRootPane().setDefaultButton(registerButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
