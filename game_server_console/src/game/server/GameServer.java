package game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(80);
			System.out.println("Game server started and listening on port 80");
			while(!Thread.currentThread().isInterrupted()) {
				Socket socket = server.accept();
				System.out.println("Received ping from " + socket.getInetAddress());
				GameConnectionThread gameConnectionThread = new GameConnectionThread(socket);
				gameConnectionThread.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
