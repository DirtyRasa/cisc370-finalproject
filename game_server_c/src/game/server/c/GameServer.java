package game.server.c;

public class GameServer {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		GameConnectionThread gameConnectionThread = new GameConnectionThread();
		gameConnectionThread.start();
		
		while (true) {		
			try {
				gameConnectionThread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
