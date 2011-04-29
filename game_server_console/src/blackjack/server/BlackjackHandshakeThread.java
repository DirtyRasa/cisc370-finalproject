package blackjack.server;

import communication.Communication;
import communication.Response;
import communication.ResponseException;

public class BlackjackHandshakeThread extends Thread{
	Blackjack _blackjack;
	
	RemotePlayer _player;
	
	public BlackjackHandshakeThread(Blackjack blackjack, RemotePlayer player) {
		_blackjack = blackjack;
		_player = player;
	}
	
	public void run(){
		boolean done = false;
		try{
			while(!done){
				Communication.sendQuestion(_player.getOutput(), "\nWould you like to play Blackjack (y/n)?");
				try{
					if(Response.binaryEval(_player.getInput().readLine()))
					{	
						done=true;
	
						Communication.sendMessage(_player.getOutput(), "\nPlease wait for the current hand to finish.");
	
						try
						{
							_blackjack.addPlayer(_player);
						}
						catch(Exception e){throw new TableFullException("Table is currently full");}
					}
					else
					{
						Communication.sendMessage(_player.getOutput(), "end");
						done = true;
					}
				}catch (ResponseException ex){
					Communication.sendMessage(_player.getOutput(), ex.getMessage());
					done = false;
				}
			}
		}
		catch(Exception e){throw new RuntimeException();}
	}
}
