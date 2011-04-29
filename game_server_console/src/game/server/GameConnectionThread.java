package game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import blackjack.server.Blackjack;

public class GameConnectionThread extends Thread{
	
	Blackjack _blackjack;
	
	public GameConnectionThread(Blackjack blackjack){
		_blackjack = blackjack;
	}
	
	public void run(){
		try{
			ServerSocket socket = new ServerSocket(80);
			Socket client = null;
			GameHandshakeThread gameHandshakeThread;
			
			while(!Thread.currentThread().isInterrupted()){
				client = socket.accept();
				System.out.println("Received ping from " + socket.getInetAddress());
				gameHandshakeThread = new GameHandshakeThread(client, _blackjack);
				gameHandshakeThread.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
