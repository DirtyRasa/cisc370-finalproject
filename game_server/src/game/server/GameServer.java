package game.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import blackjack.server.Blackjack;

import communication.Communication;

import dal.DataAccessLayer;

public class GameServer {
	private Blackjack _blackjackTable1;
	private static GameServer _gs;
	private static DataAccessLayer _dal;
	private static List<User> _users = new ArrayList<User>();
	
	
	public GameServer(){
		try {
			_blackjackTable1 = new Blackjack(this, 3);
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
	public synchronized User register(User user){
		String userName = null;
		String password1 = null;
		String password2 = null;
		String eMail = null;
		
		boolean done = false;
		try {
			userName = user.getInput().readLine();
			if(_dal.doesUserExist(userName))
				Communication.sendMessage(user, "already");
					
			password1 = user.getInput().readLine();
			password2 = user.getInput().readLine();
			
			eMail = user.getInput().readLine();
			
			if(!_dal.register(userName, password1, eMail)){
				Communication.sendMessage(user, "error");
				return null;
			}			
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
		
		try {
			user.setMoney(_dal.getMoney(userName));
		} catch (Exception e) {	} //Should never get here.
		
		_users.add(user);
		
		return user;
	}
	
	@SuppressWarnings("unused")
	public synchronized User login(User user){
		String userName = null;
		String password = null;
		
		try {
			//Communication.sendMessage(user, "***** Login *****");
			for(int i=0; i < 3; i++){ //3 attempts
				//Communication.sendQuestion(user, "\nPlease enter your user name: ");
				userName = user.getInput().readLine();
				
				//Communication.getPassword(user, "\nPlease enter your password: ");
				password = user.getInput().readLine();
				
				for(User u : _users){
					if(u.getName().equalsIgnoreCase(userName)){
						Communication.sendMessage(user, "already");
						return null;
					}						
				}
				
				if(!_dal.login(userName, password)){
					Communication.sendMessage(user, "invalid");
					return null;
				}
				else break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		Communication.sendMessage(user, "success");
		
		user.setName(userName);
		
		try {
			user.setMoney(_dal.getMoney(userName));
		} catch (Exception e) {	} //Should never get here.
		
		_users.add(user);
		
		return user;
	}
	
	public synchronized void logout(User user){
		User toRemove = null;
		
		for(User u : _users){
			if(u.getName().equalsIgnoreCase(user.getName())){
				toRemove = u;
				break;
			}
		}
		if(toRemove != null)
			_users.remove(toRemove);
		
		try {
			user.getSocket().close();
		} catch (IOException e) {}
	}
	
	public synchronized User updateMoney(User user, double money){
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
