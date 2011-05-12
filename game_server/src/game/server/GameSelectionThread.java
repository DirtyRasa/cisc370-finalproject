package game.server;

import communication.Communication;
import blackjack.server.Blackjack;
import blackjack.server.BlackjackPlayer;

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
						Communication.sendBank(_user, _user.getMoney() +"");
						Communication.sendStats(_user, _user.getStats());
						Communication.sendTable(_user, hold+1 + "");
						
						BlackjackPlayer player = new BlackjackPlayer(_user.getSocket(),_user.getOutput(), _user.getInput());
						player.setName(_user.getName());
						player.setMoney(_user.getMoney());
						player.setWins(_user.getWins());
						player.setLosses(_user.getLosses());
						player.setPushes(_user.getPushes());
						player.setTotal(_user.getTotal());
						
						Communication.sendWait(player, "Please wait for the current hand to finish.");
						
						_gs.getBJTables()[hold].addPlayer(player);
						
						System.out.println("Player " + player.getName() +" added.");
					}
					else{
						Communication.sendMessage(_user, "\r\nThank you for playing. Have a nice day!");
						Communication.sendMessage(_user, "end");
						_gs.logout(_user);
					}
					done = true;
				}
				else if(Games.QUIT.ordinal() == hold || hold < 0){
					Communication.sendMessage(_user, "\r\nThank you for playing. Have a nice day!");
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
