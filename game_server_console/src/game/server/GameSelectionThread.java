package game.server;

import communication.Communication;
import blackjack.server.BlackjackHandshakeThread;

public class GameSelectionThread extends Thread{
	GameServer _gs;
	User _user;

	public GameSelectionThread(GameServer gs, User user) {
		_gs = gs;
		_user = user;
	}
	
	public void run(){
		boolean done = false;
		try{
			while(!done){
				Communication.sendMessage(_user.getOutput(), "\nWhich game would you like to play?");
				Communication.sendQuestion(_user.getOutput(), Games.getGameList());
				int hold = -1;
				try{
					hold = Integer.parseInt(_user.getInput().readLine());
				} catch (Exception e) {
					Communication.sendMessage(_user.getOutput(), "Invalid input. Please try again.");
					done = false;
				}
				if(Games.BLACKJACK.ordinal()==hold){
					BlackjackHandshakeThread blackjackHandshakeThread = new BlackjackHandshakeThread(_gs.getBlackjackTable(), _user);
					blackjackHandshakeThread.start();
					done = true;
				}
				else if(Games.QUIT.ordinal()==hold){
					Communication.sendMessage(_user.getOutput(), "Thank you for playing. Have a nice day!");
					Communication.sendMessage(_user.getOutput(), "end");
					_user.getOutput().close();
					done = true;
				}
				else{
					Communication.sendMessage(_user.getOutput(), "Invalid input. Please try again.");
					done = false;
				}				
			}
		}
		catch(Exception e){throw new RuntimeException();}
	}
}
