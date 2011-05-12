package blackjack.server;

import game.server.User;

import communication.Communication;

public class BlackjackHandshakeThread extends Thread{
	Blackjack _blackjack;
	
	BlackjackPlayer _player;
	
	public BlackjackHandshakeThread(Blackjack blackjack, User user) {
		_blackjack = blackjack;
		//TODO Downcasting... _player = (BlackjackPlayer) user;
		_player = new BlackjackPlayer(user.getSocket(),user.getOutput(), user.getInput());
		_player.setName(user.getName());
		_player.setMoney(user.getMoney());
	}
	
	public void run(){
		try{
			Communication.sendMessage(_player, "\nPlease wait for the current hand to finish.");

			try
			{
				_blackjack.addPlayer(_player);
				System.out.println("Player " + _player.getName() +" added.");
			}
			catch(Exception e){throw new TableFullException("Table is currently full");}
		}
		catch(Exception e){throw new RuntimeException();}
	}
}
