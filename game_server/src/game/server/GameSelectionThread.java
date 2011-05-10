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
			Communication.sendMessage(_user, "***** Welcome to the Game Server *****\n\n");
			while(!done){
				Communication.sendMessage(_user, "\nWhich game would you like to play?");
				Communication.sendQuestion(_user, Games.getGameList());
				int hold = -1;
				try{
					String test = _user.getInput().readLine();
					System.out.println(test);
					hold = Integer.parseInt(test);
				} catch (Exception e) {
					e.printStackTrace();
					Communication.sendMessage(_user, "Invalid input. Please try again.");
					done = false;
				}
				if(Games.BLACKJACK.ordinal()==hold){
					Communication.sendMessage(_user, "Bank: $"+_user.getMoney());
					Communication.sendMessage(_user, "Record: " + _user.getWins() + "-" + _user.getLosses() + "-" + _user.getPushes());
					BlackjackHandshakeThread blackjackHandshakeThread = new BlackjackHandshakeThread(_gs.getBlackjackTable(), _user);
					blackjackHandshakeThread.start();
					done = true;
				}
				else if(Games.QUIT.ordinal()==hold){
					Communication.sendMessage(_user, "Thank you for playing. Have a nice day!");
					Communication.sendMessage(_user, "end");
					_gs.logout(_user);
					//_user.getOutput().close();
					done = true;
				}
				else{
					Communication.sendMessage(_user, "Invalid input. Please try again.");
					done = false;
				}				
			}
		}
		catch(Exception e){
			_gs.logout(_user);
			throw new RuntimeException();
		}
	}
}
