package game.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Canvas;
import java.awt.ScrollPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JScrollBar;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class GameClientTest {

	private JFrame frmGameClient;
	private JTextField textField;
	private JLabel lblBet;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameClientTest window = new GameClientTest();
					window.frmGameClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameClientTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGameClient = new JFrame();
		frmGameClient.setTitle("Game Client");
		frmGameClient.setBounds(100, 100, 687, 514);
		frmGameClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 440, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 240, 124, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		frmGameClient.getContentPane().setLayout(gridBagLayout);
		
		Canvas blackjackTable = new Canvas();
		GridBagConstraints gbc_blackjackTable = new GridBagConstraints();
		gbc_blackjackTable.gridwidth = 2;
		gbc_blackjackTable.insets = new Insets(0, 0, 5, 0);
		gbc_blackjackTable.gridx = 1;
		gbc_blackjackTable.gridy = 1;
		frmGameClient.getContentPane().add(blackjackTable, gbc_blackjackTable);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 2;
		frmGameClient.getContentPane().add(scrollPane, gbc_scrollPane);
		
		JTextArea ChatOutput = new JTextArea();
		ChatOutput.setEditable(false);
		ChatOutput.setLineWrap(true);
		scrollPane.setViewportView(ChatOutput);
		
		JPanel panelUserInput = new JPanel();
		GridBagConstraints gbc_panelUserInput = new GridBagConstraints();
		gbc_panelUserInput.insets = new Insets(0, 0, 5, 0);
		gbc_panelUserInput.gridheight = 2;
		gbc_panelUserInput.fill = GridBagConstraints.BOTH;
		gbc_panelUserInput.gridx = 2;
		gbc_panelUserInput.gridy = 2;
		frmGameClient.getContentPane().add(panelUserInput, gbc_panelUserInput);
		GridBagLayout gbl_panelUserInput = new GridBagLayout();
		gbl_panelUserInput.columnWidths = new int[]{22, 50, 40, 21, 64, 0, 0};
		gbl_panelUserInput.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelUserInput.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelUserInput.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelUserInput.setLayout(gbl_panelUserInput);
		
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
		
		JButton btnHit = new JButton("Hit");
		GridBagConstraints gbc_btnHit = new GridBagConstraints();
		gbc_btnHit.insets = new Insets(0, 0, 5, 5);
		gbc_btnHit.gridx = 2;
		gbc_btnHit.gridy = 3;
		panelUserInput.add(btnHit, gbc_btnHit);
		
		JButton btnNewButton = new JButton("Stand");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 3;
		panelUserInput.add(btnNewButton, gbc_btnNewButton);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 3;
		frmGameClient.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 4;
		frmGameClient.getContentPane().add(panel_1, gbc_panel_1);
	}

}
