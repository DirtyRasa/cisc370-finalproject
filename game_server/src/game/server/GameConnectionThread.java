package game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameConnectionThread extends Thread{
	
	GameServer _gs;
	
	public GameConnectionThread(GameServer gs){
		_gs = gs;
	}
	
	public void run(){
		try{
			ServerSocket socket = new ServerSocket(5000);
			System.out.println("Game server started and listening on port 5000");	
			
			Socket client = null;
			GameHandshakeThread gameHandshakeThread;
			
			while(!Thread.currentThread().isInterrupted()){
				client = socket.accept();
				
				System.out.println("Received ping from " + socket.getInetAddress());
				
				gameHandshakeThread = new GameHandshakeThread(client, _gs);
				gameHandshakeThread.start();
			}
			
		} catch (IOException e) {
			System.exit(0);
			//e.printStackTrace();
		}
	}
}
