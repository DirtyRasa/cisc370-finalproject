package game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import communication.*;
import dal.*;

public class GameHandshakeThread extends Thread{
	
	GameServer _gs;
	Socket _client;
	DataAccessLayer _dal;
	
	String _username;
	
	public GameHandshakeThread(Socket mySocket, GameServer gs){
		_gs = gs;
		_client = mySocket;
		_dal = new DataAccessLayer();
	}
	
	public void run(){
		try{
			boolean done = false;
			
			BufferedReader in = new BufferedReader(new InputStreamReader(_client.getInputStream()));
			PrintWriter out = new PrintWriter(_client.getOutputStream());
				
			Communication.sendMessage(out, "***** Welcome to the Game Server *****");
			
			do
			{
				Communication.sendQuestion(out, "Do you already have an account? (Y/N) ");
				try{
					done = Response.binaryEval(in.readLine()) ? Login(in, out) : Register(in, out);
					if(done){
						Communication.sendMessage(out, "Thank you, press enter to continue.");
					}
				}
				catch (ResponseException ex){
					Communication.sendMessage(out, ex.getMessage());
					done = false;
				}
			}while(!done);
			Communication.sendQuestion(out, "");
			in.readLine();
			
			User user = new User(_username, _client, out, in);
			
			GameSelectionThread gameSelectionThread = new GameSelectionThread(_gs, user);
			gameSelectionThread.start();
			
			//BlackjackHandshakeThread blackjackHandshakeThread = new BlackjackHandshakeThread(_blackjack, user);
			//blackjackHandshakeThread.start();
			
			//Communication.sendMessage(out, "end");
			//_client.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//TO-DO: Add Throw Exception for database error
	public boolean Register(BufferedReader in, PrintWriter out){
		String userName = null;
		String password1 = null;
		String password2 = null;
		String eMail = null;
		
		boolean done = false;
		try {
			Communication.sendMessage(out, "***** Registration *****");
			//To-do: Add constraints on userName. i.e. more than 3 letters less than 20?
			while(!done){
				Communication.sendQuestion(out, "\nPlease enter a user name: ");
				userName = in.readLine();
				if(_dal.isAlreadyUser(userName)){
					Communication.sendMessage(out, "Already a user by the name of: " + userName + "\nPlease try again.\n");
				}
				else
					done = true;
			}
			
			done = false;
			
			//To-do: Add constraints on password. i.e. must contain number etc...			
			while (!done) {
				Communication.getPassword(out, "\nPlease enter a password: ");
				password1 = in.readLine();
				Communication.getPassword(out, "\nPlease re-enter the password: ");
				password2 = in.readLine();
				
				if(!password1.equals(password2)){
					Communication.sendMessage(out, "Passwords were not the same\nPlease try again.\n");
				}
				else
					done = true;
			}
			
			done = false;
			
			//To-do: Add constraints on eMail. i.e. must be valid. (how to check validity?)
			while (!done) {
				Communication.sendQuestion(out, "\nPlease enter an email: ");
				eMail = in.readLine();
				
				done = true;
			}
			
			if(!_dal.register(userName, password1, eMail)){
				Communication.sendMessage(out, "Error!");
				return false;
			}			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		_username = userName;
		return true;
	}
	
	@SuppressWarnings("unused")
	public boolean Login(BufferedReader in, PrintWriter out){
		String userName = null;
		String password = null;
		
		try {
			Communication.sendMessage(out, "***** Login *****");
			for(int i=0; i < 3; i++){
				Communication.sendQuestion(out, "\nPlease enter your user name: ");
				userName = in.readLine();
				
				Communication.getPassword(out, "\nPlease enter your password: ");
				password = in.readLine();
				
				if(!_dal.login(userName, password)){
					Communication.sendMessage(out, "\nInvalid username or password combination. Please try again.\n");
					return false;
				}
				else break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		_username = userName;
		return true;
	}
}
