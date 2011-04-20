package blackjack.server;

import java.io.IOException;

public class BlackjackServer {	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		try {
			Blackjack blackjack = new Blackjack(3, 6);
			BlackjackConnectionThread blackjackConnectionThread = new BlackjackConnectionThread(blackjack);
			blackjackConnectionThread.start();
			
			System.out.println("Waiting for players...");
			
			while (true) {		
				try {
					blackjackConnectionThread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				blackjack.playGame();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
