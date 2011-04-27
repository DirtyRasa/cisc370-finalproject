package game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import response.*;
import dal.*;

public class GameConnectionThread extends Thread{
	
	Socket _client;
	DataAccessLayer _dal;
	
	public GameConnectionThread(Socket mySocket){
		_client = mySocket;
		_dal = new DataAccessLayer();
	}
	
	public void run(){
		try{
			boolean done = false;
			
			InputStreamReader isr = new InputStreamReader(_client.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			PrintWriter out = new PrintWriter(_client.getOutputStream());
			
				
			out.println("***** Welcome to the Game Server *****");
			out.flush();
			
			do
			{
				out.println("Do you already have an account? (Y/N) ");
				out.flush();
				try{
					done = Response.binaryEval(in.readLine()) ? Login(in, out) : Register(in, out);
					if(done){
						out.println("Thanks.");
						out.flush();
					}
				}
				catch (ResponseException ex){
					out.println(ex.getMessage());
					out.flush();
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
	public boolean Register(BufferedReader in, PrintWriter out){
		String userName = null;
		String password1 = null;
		String password2 = null;
		String eMail = null;
		
		boolean done = false;
		try {
			out.println("***** Registration *****");
			
			//To-do: Add constraints on userName. i.e. more than 3 letters less than 20?
			while(!done){
				out.println("\nPlease enter a user name: ");
				out.flush();
				userName = in.readLine();
				if(_dal.isAlreadyUser(userName)){
					out.println("Already a user by the name of: " + userName);
					out.println("Please try again.\n");
					out.flush();
				}
				else
					done = true;
			}
			
			done = false;
			
			//To-do: Add constraints on password. i.e. must contain number etc...			
			while (!done) {
				out.println("\nPlease enter a password: ");
				out.flush();
				password1 = in.readLine();
				out.println("\nPlease re-enter the password: ");
				out.flush();
				password2 = in.readLine();
				
				if(!password1.equals(password2)){
					out.println("Passwords were not the same");
					out.println("Please try again.\n");
					out.flush();
				}
				else
					done = true;
			}
			
			done = false;
			
			//To-do: Add constraints on eMail. i.e. must be valid. (how to check validity?)
			while (!done) {
				out.println("\nPlease enter an email: ");
				out.flush();
				eMail = in.readLine();
				
				done = true;
			}
			
			if(!_dal.register(userName, password1, eMail)){
				out.println("Error!");
				out.flush();
				return false;
			}			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unused")
	public boolean Login(BufferedReader in, PrintWriter out){
		String userName = null;
		String password = null;
		
		try {
			out.println("***** Login *****");
			for(int i=0; i < 3; i++){
				out.println("\nPlease enter your user name: ");
				out.flush();
				userName = in.readLine();
				
				out.println("\nPlease enter your password: ");
				out.flush();
				password = in.readLine();
				
				if(!_dal.login(userName, password))
					return false;
				else break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
