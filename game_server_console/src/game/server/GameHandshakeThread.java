package game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import communication.*;
//import dal.*;

public class GameHandshakeThread extends Thread{
	
	GameServer _gs;
	Socket _client;
	//DataAccessLayer _dal;
	
	public GameHandshakeThread(Socket mySocket, GameServer gs){
		_gs = gs;
		_client = mySocket;
		//_dal = new DataAccessLayer();
	}
	
	public void run(){
		try{
			boolean done = false;
			
			BufferedReader in = new BufferedReader(new InputStreamReader(_client.getInputStream()));
			PrintWriter out = new PrintWriter(_client.getOutputStream());
			
			User user = new User(_client, out, in);
			
			Communication.sendMessage(user, "***** Welcome to the Game Server *****");
			
			do
			{
				Communication.sendQuestion(user, "Do you already have an account? (Y/N) ");
				try{
					user = Response.binaryEval(in.readLine()) ? _gs.Login(user) : _gs.Register(user);
					if(user != null)
						done = true;
					if(done){
						Communication.sendMessage(user, "Thank you, press enter to continue.");
					}
				}
				catch (ResponseException ex){
					//In case user is null; Just create a new user object.
					Communication.sendMessage(new User(_client, out, in), ex.getMessage());
					done = false;
				}
			}while(!done);
			Communication.sendQuestion(user, "");
			in.readLine();
			
			GameSelectionThread gameSelectionThread = new GameSelectionThread(_gs, user);
			gameSelectionThread.start();			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/*
	//TODO Add Throw Exception for database error
	public boolean Register(BufferedReader in, PrintWriter out){
		String userName = null;
		String password1 = null;
		String password2 = null;
		String eMail = null;
		
		boolean done = false;
		try {
			Communication.sendMessage(out, "***** Registration *****");
			while(!done){
				Communication.sendQuestion(out, "\nPlease enter a user name: ");
				userName = in.readLine();
				if(userName.matches("^[a-zA-Z0-9_-]{3,20}$"))
					Communication.sendMessage(out, "User name '" + userName + "' contains illegal characters, is too short, or too long. Please try again. \r\n");
				else if(_dal.doesUserExist(userName)){
					Communication.sendMessage(out, "Already a user by the name of: " + userName + "\nPlease try again.\n");
				}
				else
					done = true;
			}
			
			done = false;
			
			//TODO Add constraints on password. i.e. must contain number etc...			
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
			
			//TODO Add constraints on eMail. i.e. must be valid. (how to check validity?)
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
	}*/
}
