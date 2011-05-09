package game.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import communication.*;

public class GameHandshakeThread extends Thread
{
	
	GameServer _gs;
	Socket _client;
	
	public GameHandshakeThread(Socket mySocket, GameServer gs)
	{
		_gs = gs;
		_client = mySocket;
	}
	
	public void run(){
		User user = null;
		try{
			boolean done = false;
			
			BufferedReader in = new BufferedReader(new InputStreamReader(_client.getInputStream()));
			PrintWriter out = new PrintWriter(_client.getOutputStream());
			
			user = new User(_client, out, in);
			
			System.out.println("User created. Sending welcome message");
			
			Communication.sendMessage(user, "***** Welcome to the Game Server *****");
			
			do
			{
				Communication.sendQuestion(user, "Do you already have an account? (Y/N) ");
				try{
					user = Response.binaryEval(in.readLine()) ? _gs.login(user) : _gs.register(user);
					if(user != null)
						done = true;
					else
						user = new User(_client, out, in); 
					if(done)
						Communication.sendMessage(user, "Thank you, press enter to continue.");
					/*
					int response = Response.trinaryEval(user.getInputWithTimeout());
					
					switch(response)
					{
					case -1:
						_gs.register(user);
						break;
					case 0:
						_gs.logout(user);
						break;
					case 1:
						_gs.login(user);
						break;
					}					
					
					if(user != null)
						done = true;
					if(done)
						Communication.sendMessage(user, "Thank you, press enter to continue.");*/
				}
				catch (ResponseException ex){
					//TODO In case user is null; Just create a new user object.
					Communication.sendMessage(user, ex.getMessage());
					done = false;
				}
				
			}while(!done);
			Communication.sendQuestion(user, "");
			in.readLine();
			
			GameSelectionThread gameSelectionThread = new GameSelectionThread(_gs, user);
			gameSelectionThread.start();			
		} catch (Exception e){
			if(user != null){
			 _gs.logout(user);
			}
		}
	}
}
