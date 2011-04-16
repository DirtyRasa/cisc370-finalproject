package blackjack;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BlackjackServer {
	public static void main(String[] args) {
		System.out.println("Creating Server Socket...");
		
		try {
			ServerSocket server = new ServerSocket(5000);
			System.out.println(""+server.getInetAddress());
			while (true) {
				System.out.println("Listening on port " + server.getLocalPort());
				Socket socket = server.accept();
				BlackjackConnectionThread connection = new BlackjackConnectionThread(socket);
				connection.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
