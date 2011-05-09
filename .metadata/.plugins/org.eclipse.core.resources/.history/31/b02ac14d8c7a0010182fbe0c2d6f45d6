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
	
	
	public GameServer()
	{
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
	public static void main(String[] args)
	{
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

	public void returnToGameSelectionThread(User user)
	{
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
		
		try {
			userName = user.getInput().readLine();
			if(!userName.matches("^[a-zA-Z0-9_-]{3,20}$")){
				Communication.sendMessage(user, "REGISTER User name '" + userName + "' contains illegal characters, is too short, or too long. Please try again. \r\n");
				return null;
			}
			else if(_dal.doesUserExist(userName)){
				Communication.sendMessage(user, "REGISTER Already a user by the name of: " + userName + "\nPlease try again.\n");
				return null;
			}
			//TODO Add constraints on password. i.e. must contain number etc...
			password1 = user.getInput().readLine();
			password2 = user.getInput().readLine();
			
			if(!password1.equals(password2)){
				Communication.sendMessage(user, "REGISTER Passwords were not the same\nPlease try again.\n");
				return null;
			}
			
			//TODO Add constraints on eMail. i.e. must be valid. (how to check validity?)
			eMail = user.getInput().readLine();
			
			if(!_dal.register(userName, password1, eMail)){
				Communication.sendMessage(user, "REGISTER ");
				return null;
			}			
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
		
		Communication.sendMessage(user, "REGISTER success");
		
		try {
			user.setMoney(_dal.getMoney(userName));
			user.setWins(_dal.getWins(userName));
			user.setLosses(_dal.getLosses(userName));
			user.setPushes(_dal.getPushes(userName));
			user.setTotal(_dal.getTotal(userName));
		} catch (Exception e) {	} //Should never get here.
		
		user.setName(userName);
		
		_users.add(user);
		
		return user;
	}
	
	public synchronized User login(User user){
		String userName = null;
		String password = null;
		
		try {
				userName = user.getInput().readLine();
				
				password = user.getInput().readLine();
				
				for(User u : _users){
					if(u.getName().equalsIgnoreCase(userName)){
						Communication.sendMessage(user, "LOGIN The user name '"+ userName +"' is already logged in.\n");
						return null;
					}
				}
				
				if(!_dal.login(userName, password)){
					Communication.sendMessage(user, "LOGIN Invalid username or password combination. Please try again.\n");
					return null;
				}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		Communication.sendMessage(user, "LOGIN success");
		
		user.setName(userName);
		
		try {
			user.setMoney(_dal.getMoney(userName));
			user.setWins(_dal.getWins(userName));
			user.setLosses(_dal.getLosses(userName));
			user.setPushes(_dal.getPushes(userName));
			user.setTotal(_dal.getTotal(userName));
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
	public synchronized User updateWins(User user){
		int currentWins = user.getWins();
		currentWins++;
		try {
			_dal.setWins(user.getName(), currentWins);
			user.setWins(currentWins);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public synchronized User updateLosses(User user){
		int currentLosses = user.getLosses();
		currentLosses++;
		try {
			_dal.setLosses(user.getName(), currentLosses);
			user.setLosses(currentLosses);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public synchronized User updatePushes(User user){
		int currentPushes = user.getPushes();
		currentPushes++;
		try {
			_dal.setPushes(user.getName(), currentPushes);
			user.setPushes(currentPushes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public synchronized User updateTotal(User user){
		int currentTotal = user.getTotal();
		currentTotal++;
		try {
			_dal.setTotal(user.getName(), currentTotal);
			user.setTotal(currentTotal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
}
