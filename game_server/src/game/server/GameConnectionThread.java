package game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import communication.*;

import dal.*;

public class GameConnectionThread extends Thread{
	
	Socket _client;
	DataAccessLayer _dal;
	Communication _com;
	
	public GameConnectionThread(Socket mySocket){
		_client = mySocket;
		_dal = new DataAccessLayer();
	}
	
	public void run(){
		try{
			boolean done = false;
			
			InputStreamReader isr = new InputStreamReader(_client.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			_com = new Communication(new PrintWriter(_client.getOutputStream()));
				
			Communication.sendMessage("***** Welcome to the Game Server *****");
			
			do
			{
				Communication.sendQuestion("Do you already have an account? (Y/N) ");
				try{
					done = Response.binaryEval(in.readLine()) ? Login(in) : Register(in);
					if(done){
						Communication.sendMessage("Thanks.");
					}
				}
				catch (ResponseException ex){
					Communication.sendMessage(ex.getMessage());
					done = false;
				}
			}while(!done);
			in.readLine();
			_client.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//TO-DO: Add Throw Exception for database error
	public boolean Register(BufferedReader in){
		String userName = null;
		String password1 = null;
		String password2 = null;
		String eMail = null;
		
		boolean done = false;
		try {
			Communication.sendMessage("***** Registration *****");
			//To-do: Add constraints on userName. i.e. more than 3 letters less than 20?
			while(!done){
				Communication.sendQuestion("\nPlease enter a user name: ");
				userName = in.readLine();
				if(_dal.isAlreadyUser(userName)){
					Communication.sendMessage("Already a user by the name of: " + userName + "\nPlease try again.\n");
				}
				else
					done = true;
			}
			
			done = false;
			
			//To-do: Add constraints on password. i.e. must contain number etc...			
			while (!done) {
				Communication.sendQuestion("\nPlease enter a password: ");
				password1 = in.readLine();
				Communication.sendQuestion("\nPlease re-enter the password: ");
				password2 = in.readLine();
				
				if(!password1.equals(password2)){
					Communication.sendMessage("Passwords were not the same\nPlease try again.\n");
				}
				else
					done = true;
			}
			
			done = false;
			
			//To-do: Add constraints on eMail. i.e. must be valid. (how to check validity?)
			while (!done) {
				Communication.sendQuestion("\nPlease enter an email: ");
				eMail = in.readLine();
				
				done = true;
			}
			
			if(!_dal.register(userName, password1, eMail)){
				Communication.sendMessage("Error!");
				return false;
			}			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unused")
	public boolean Login(BufferedReader in){
		String userName = null;
		String password = null;
		
		try {
			Communication.sendMessage("***** Login *****");
			for(int i=0; i < 3; i++){
				Communication.sendQuestion("\nPlease enter your user name: ");
				userName = in.readLine();
				
				Communication.sendQuestion("\nPlease enter your password: ");
				password = in.readLine();
				
				if(!_dal.login(userName, password)){
					Communication.sendMessage("\nInvalid username or password combination. Please try again.\n");
					return false;
				}
				else break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
