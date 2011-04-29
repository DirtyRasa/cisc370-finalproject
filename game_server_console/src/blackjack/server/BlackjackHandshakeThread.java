package blackjack.server;

import game.server.User;

import communication.Communication;

public class BlackjackHandshakeThread extends Thread{
	Blackjack _blackjack;
	
	User _user;
	
	public BlackjackHandshakeThread(Blackjack blackjack, User user) {
		_blackjack = blackjack;
		_user = user;
	}
	
	public void run(){
		try{
			RemotePlayer player = new RemotePlayer(_user.getName(), _user.getSocket(), _user.getOutput(), _user.getInput());

			Communication.sendMessage(_user.getOutput(), "\nPlease wait for the current hand to finish.");

			try
			{
				_blackjack.addPlayer(player);
				System.out.println("Player " + player.getName() +" added.");
			}
			catch(Exception e){throw new TableFullException("Table is currently full");}
		}
		catch(Exception e){throw new RuntimeException();}
	}
}
