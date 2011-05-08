package game.server;

import java.io.IOException;

import communication.Communication;
import dal.DataAccessLayer;

import blackjack.server.Blackjack;

public class GameServer {
	private Blackjack _blackjackTable1;
	private static GameServer _gs;
	private static DataAccessLayer _dal;
	
	public GameServer(){
		try {
			_blackjackTable1 = new Blackjack(this, 3, 6);
			_dal = new DataAccessLayer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		try {
			_gs = new GameServer();
			GameConnectionThread gameConnectionThread = new GameConnectionThread(_gs);
			gameConnectionThread.start();		

			System.out.println("Waiting for players...");
			
			while(!Thread.currentThread().isInterrupted()) {
				try {
					gameConnectionThread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				_gs._blackjackTable1.playGame();				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void returnToGameSelectionThread(User user){
		GameSelectionThread gameSelectionThread = new GameSelectionThread(this, user);
		gameSelectionThread.start();
	}
	
	public Blackjack getBlackjackTable() { return _blackjackTable1;}
	
	//TODO Add Throw Exception for database error
	public User Register(User user){
		String userName = null;
		String password1 = null;
		String password2 = null;
		String eMail = null;
		
		boolean done = false;
		try {
			Communication.sendMessage(user, "***** Registration *****");
			while(!done){
				Communication.sendQuestion(user, "\nPlease enter a user name: ");
				userName = user.getInput().readLine();
				if(userName.matches("^[a-zA-Z0-9_-]{3,20}$"))
					Communication.sendMessage(user, "User name '" + userName + "' contains illegal characters, is too short, or too long. Please try again. \r\n");
				else if(_dal.doesUserExist(userName)){
					Communication.sendMessage(user, "Already a user by the name of: " + userName + "\nPlease try again.\n");
				}
				else
					done = true;
			}
			
			done = false;
			
			//TODO Add constraints on password. i.e. must contain number etc...			
			while (!done) {
				Communication.getPassword(user, "\nPlease enter a password: ");
				password1 = user.getInput().readLine();
				Communication.getPassword(user, "\nPlease re-enter the password: ");
				password2 = user.getInput().readLine();
				
				if(!password1.equals(password2)){
					Communication.sendMessage(user, "Passwords were not the same\nPlease try again.\n");
				}
				else
					done = true;
			}
			
			done = false;
			
			//TODO Add constraints on eMail. i.e. must be valid. (how to check validity?)
			while (!done) {
				Communication.sendQuestion(user, "\nPlease enter an email: ");
				eMail = user.getInput().readLine();
				done = true;
			}
			
			if(!_dal.register(userName, password1, eMail)){
				Communication.sendMessage(user, "Error!");
				return null;
			}			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			user.setMoney(_dal.getMoney(userName));
		} catch (Exception e) {	} //Should never get here.
		
		return user;
	}
	
	@SuppressWarnings("unused")
	public User Login(User user){
		String userName = null;
		String password = null;
		
		try {
			Communication.sendMessage(user, "***** Login *****");
			for(int i=0; i < 3; i++){ //3 attempts
				Communication.sendQuestion(user, "\nPlease enter your user name: ");
				userName = user.getInput().readLine();
				
				Communication.getPassword(user, "\nPlease enter your password: ");
				password = user.getInput().readLine();
				
				if(!_dal.login(userName, password)){
					Communication.sendMessage(user, "\nInvalid username or password combination. Please try again.\n");
					return null;
				}
				else break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		user.setName(userName);
		
		try {
			user.setMoney(_dal.getMoney(userName));
		} catch (Exception e) {	} //Should never get here.
		
		return user;
	}
	
	public User updateMoney(User user, double money){
		double currentMoney = user.getMoney();
		currentMoney += money;
		try {
			_dal.setMoney(user.getName(), currentMoney);
			user.setMoney(currentMoney);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
}
