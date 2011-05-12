package game.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import blackjack.server.Blackjack;

import communication.Communication;

import dal.DataAccessLayer;

public class GameServer {
	private Blackjack[] _bjTables = new Blackjack[3];
	private static final GameServer _gs = new GameServer();;
	private static DataAccessLayer _dal;
	private static List<User> _users = new ArrayList<User>();
	
	
	private GameServer()
	{
		try {
			for(int i=0; i< _bjTables.length;i++)
				_bjTables[i] = new Blackjack(this, 5, i+1);
			_dal = new DataAccessLayer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static GameServer getInstance(){
		return _gs;
	}
	
	@SuppressWarnings("static-access")
	public static void main(String[] args)
	{
		GameConnectionThread gameConnectionThread = new GameConnectionThread(_gs);
		gameConnectionThread.start();		

		System.out.println("Waiting for players...");
		
		for(int i=0; i< _gs._bjTables.length;i++)
			_gs._bjTables[i].start();
		
		while(!Thread.currentThread().isInterrupted()) {				
			try {
				gameConnectionThread.sleep(5000);
				for(int i=0; i< _gs._bjTables.length;i++)
					_gs._bjTables[i].sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
						
		}
	}

	public void returnToGameSelectionThread(User user)
	{
		GameSelectionThread gameSelectionThread = new GameSelectionThread(this, user);
		gameSelectionThread.start();
	}
	
	public Blackjack[] getBJTables() {return _bjTables;}
	
	//TODO Add Throw Exception for database error
	public synchronized User register(User user){
		String userName = null;
		String password1 = null;
		String password2 = null;
		String eMail = null;
		
		try {
			userName = user.getUserInput();
			if(!userName.matches("^[a-zA-Z0-9_-]{3,20}$")){
				Communication.sendRegister(user, "User name '" + userName + "' contains illegal characters, is too short, or too long. Please try again.");
				return null;
			}
			else if(_dal.doesUserExist(userName)){
				Communication.sendRegister(user, "Already a user by the name of: " + userName + "\nPlease try again.");
				return null;
			}
			//TODO Add constraints on password. i.e. must contain number etc...
			password1 = user.getUserInput();
			password2 = user.getUserInput();
			
			if(!password1.equals(password2)){
				Communication.sendRegister(user, "Passwords were not the same\nPlease try again.");
				return null;
			}
			
			//TODO Add constraints on eMail. i.e. must be valid. (how to check validity?)
			eMail = user.getUserInput();
			
			if(!_dal.register(userName, password1, eMail)){
				Communication.sendRegister(user, "REGISTER ");
				return null;
			}
		} catch (InputException e) {
			return null;
		}
		
		Communication.sendRegister(user, "success");
		
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
	
	public synchronized User login(User user) throws IOException{
		String userName = null;
		String password = null;
		
		try {
				userName = user.getUserInput();
				
				password = user.getUserInput();
				
				for(User u : _users){
					if(u.getName().equalsIgnoreCase(userName)){
						Communication.sendLogin(user, "The user name '"+ userName +"' is already logged in.");
						return null;
					}
				}
				
				if(!_dal.login(userName, password)){
					Communication.sendLogin(user, "Invalid username or password combination. Please try again.");
					return null;
				}
		} catch (InputException e) {
			return null;
		}
		
		Communication.sendLogin(user, "success");
		
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
		if(toRemove != null){
			System.out.println(toRemove.getName() + " logged out.");
			Communication.sendMessage(toRemove, "You have been disconnected...");
			Communication.sendMessage(toRemove, "end");
			_users.remove(toRemove);
		}
			
		
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
