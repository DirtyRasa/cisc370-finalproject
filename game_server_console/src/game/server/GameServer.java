package game.server;

import java.io.IOException;

import blackjack.server.Blackjack;

public class GameServer {
	private Blackjack _blackjackTable1;
	private static GameServer _gs;
	
	public GameServer(){
		try {
			_blackjackTable1 = new Blackjack(this, 3, 6);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		try {
			_gs = new GameServer();
			GameConnectionThread gameConnectionThread = new GameConnectionThread(_gs);
			gameConnectionThread.start();		

			System.out.println("Waiting for players...");
			
			while(!Thread.currentThread().isInterrupted()) {
				try {
					gameConnectionThread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				_gs._blackjackTable1.playGame();				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void returnToGameSelectionThread(User user){
		GameSelectionThread gameSelectionThread = new GameSelectionThread(this, user);
		gameSelectionThread.start();
	}
	
	public Blackjack getBlackjackTable() { return _blackjackTable1;}
}
