package blackjack.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BlackjackConnectionThread extends Thread{
	
	Blackjack _blackjack;
	
	public BlackjackConnectionThread(Blackjack blackjack){
		_blackjack = blackjack;
	}
	
	public void run(){
		try{
			ServerSocket socket = new ServerSocket(80);
			Socket client = null;
			BlackjackHandshakeThread blackjackHandshakeThread;
			
			while(!Thread.currentThread().isInterrupted()){
				client = socket.accept();
				
				blackjackHandshakeThread = new BlackjackHandshakeThread(client, _blackjack);
				blackjackHandshakeThread.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
