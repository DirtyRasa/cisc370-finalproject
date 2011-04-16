package blackjack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BlackjackConnectionThread extends Thread{
	
	Socket _socket;
	
	public BlackjackConnectionThread(Socket mySocket)
	{
		_socket = mySocket;
	}
	
	public void run(){
		try{
			System.out.println("Connection Established!");
			String clientIP = _socket.getInetAddress().toString();
			int clientPort = _socket.getPort();
			System.out.println("Client IP: " + clientIP);
			System.out.println("Client Port: " + clientPort);
			
			InputStreamReader isr = new InputStreamReader(_socket.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			PrintWriter out = new PrintWriter(_socket.getOutputStream());
			
			String msg = "";
			do	{
				System.out.println("Waiting for incoming messages...");
				msg = in.readLine();
				System.out.println("Received message: " + msg);
				
				out.println(msg);
				out.flush();
			}while(!msg.equals("quit"));					
			
			_socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
