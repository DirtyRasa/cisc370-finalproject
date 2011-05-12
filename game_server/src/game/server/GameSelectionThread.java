package game.server;

import communication.Communication;
import blackjack.server.Blackjack;
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
				Communication.sendGame(_user, Games.getGameList());
				int hold = -1;
				try{
					hold = Integer.parseInt(_user.getUserInput());
				}catch (InputException e){
					_gs.logout(_user);
				}
				catch (Exception e) {
					e.printStackTrace();
					Communication.sendMessage(_user, "Invalid input. Please try again.");
					done = false;
				}
				if(Games.BLACKJACK.ordinal()==hold){
					Blackjack[] bjTables = _gs.getBJTables();
					String list = "";
					for(int i=0; i<bjTables.length; i++)
						list += "Blackjack Table " + (i+1) + "<>";
					
					Communication.sendGame(_user, list);
					
					hold = -1;
					try{
						hold = Integer.parseInt(_user.getUserInput());
					}catch (InputException e){
						_gs.logout(_user);
					}
					if(hold >= 0){
						Communication.sendBank(_user, ""+_user.getMoney());
						Communication.sendStats(_user, _user.getStats());
						BlackjackHandshakeThread blackjackHandshakeThread = new BlackjackHandshakeThread(bjTables[hold], _user);
						blackjackHandshakeThread.start();
					}
					else{
						Communication.sendMessage(_user, "Thank you for playing. Have a nice day!");
						Communication.sendMessage(_user, "end");
						_gs.logout(_user);
					}
					done = true;
				}
				else if(Games.QUIT.ordinal() == hold || hold < 0){
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
