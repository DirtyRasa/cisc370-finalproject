

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class GameClient extends JFrame implements Runnable{

	private static GameClient clientFrame;
	private JPanel contentPane;
	private JTextField input;
	private JTextArea output;

	public static String _hostIP = "localhost";
	public static int _port = 80;
	public static Socket _client = null;
	public static PrintWriter _out = null;
	public static BufferedReader _in = null;
	
	private static boolean isDone = false;
	
	public static StringBuffer toAppend = new StringBuffer("");
	public static StringBuffer toSend = new StringBuffer("");
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					_client = new Socket(_hostIP, _port);
					_in = new BufferedReader(new InputStreamReader(_client.getInputStream()));
					_out = new PrintWriter(_client.getOutputStream(), true);
										
					login();
					
					clientFrame = new GameClient();
					clientFrame.setVisible(true);
					
					String hold;
					
					/*while(true)
					{
						if(toSend.length() != 0){
							_out.print(toSend); _out.flush();
							toSend.setLength(0);
						}
						if(_in.ready()){
							hold = _in.readLine();
							
							if(hold.equals("end"))
								break;
							else
								appendToOutput(hold);
						}
					}*/
					System.out.println("\nClient has ended");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void login(){
		LogIn frame = null;
		try {
			frame = new LogIn(_client, _out, _in);
			frame.setVisible(true);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		//frame.setVisible(false);
		//frame.dispose();
	}
	
	/**
	 * Create the frame.
	 */
	public GameClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 693, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		output = new JTextArea();
		GridBagConstraints gbc_output = new GridBagConstraints();
		gbc_output.gridwidth = 2;
		gbc_output.insets = new Insets(0, 0, 5, 0);
		gbc_output.fill = GridBagConstraints.BOTH;
		gbc_output.gridx = 0;
		gbc_output.gridy = 0;
		contentPane.add(output, gbc_output);
		
		input = new JTextField();
		GridBagConstraints gbc_input = new GridBagConstraints();
		gbc_input.insets = new Insets(0, 0, 0, 5);
		gbc_input.fill = GridBagConstraints.HORIZONTAL;
		gbc_input.gridx = 0;
		gbc_input.gridy = 1;
		contentPane.add(input, gbc_input);
		input.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendString(input.getText());
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 1;
		contentPane.add(btnSend, gbc_btnSend);
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
	 
	@Override
	public void run() {
		output.append(toAppend.toString());
	    toAppend.setLength(0);
		
	    clientFrame.repaint();
	}
}
