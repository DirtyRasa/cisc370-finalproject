package game.server;

import java.io.IOException;

import blackjack.server.Blackjack;

public class GameServer {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		try {
			Blackjack blackjackTable1 = new Blackjack(3, 6);
			GameConnectionThread gameConnectionThread = new GameConnectionThread(blackjackTable1);
			gameConnectionThread.start();
						
			System.out.println("Game server started and listening on port 80");			

			System.out.println("Waiting for players...");
			
			while(!Thread.currentThread().isInterrupted()) {
				try {
					gameConnectionThread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				blackjackTable1.playGame();				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
