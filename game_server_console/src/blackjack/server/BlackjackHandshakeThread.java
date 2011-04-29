package blackjack.server;

import game.server.User;

import communication.Communication;
import communication.Response;
import communication.ResponseException;

public class BlackjackHandshakeThread extends Thread{
	Blackjack _blackjack;
	
	User _user;
	
	public BlackjackHandshakeThread(Blackjack blackjack, User user) {
		_blackjack = blackjack;
		_user = user;
	}
	
	public void run(){
		boolean done = false;
		try{
			while(!done){
				Communication.sendQuestion(_user.getOutput(), "\nWould you like to play Blackjack (y/n)?");
				try{
					if(Response.binaryEval(_user.getInput().readLine()))
					{	
						RemotePlayer player = new RemotePlayer(_user.getName(), _user.getSocket(), _user.getOutput(), _user.getInput());
						
						done=true;
	
						Communication.sendMessage(_user.getOutput(), "\nPlease wait for the current hand to finish.");
	
						try
						{
							_blackjack.addPlayer(player);
							System.out.println("Player " + player.getName() +" added.");
						}
						catch(Exception e){throw new TableFullException("Table is currently full");}
					}
					else
					{
						Communication.sendMessage(_user.getOutput(), "end");
						done = true;
					}
				}catch (ResponseException ex){
					Communication.sendMessage(_user.getOutput(), ex.getMessage());
					done = false;
				}
			}
		}
		catch(Exception e){throw new RuntimeException();}
	}
}
