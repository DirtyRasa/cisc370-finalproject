package communication;

import game.server.User;

public class Communication {
	public void receive(String msg){
		
	}
	
	public static void sendMessage(User user, String msg){
		user.getOutput().println(msg);
		user.getOutput().flush();
	}
	
	public static void sendYesNoQuestion(User user, String question){
		user.getOutput().println("YESNO " + question);
		user.getOutput().flush();
	}
	
	public static void sendStats(User user, String stats){
		user.getOutput().println("STATS " + stats);
		user.getOutput().flush();
	}
	
	public static void sendBank(User user, String bank){
		user.getOutput().println("BANK $" + bank);
		user.getOutput().flush();
	}
	
	public static void sendWait(User user, String wait){
		user.getOutput().println("WAIT " + wait);
		user.getOutput().flush();
	}
	
	public static void sendBet(User user, String bet){
		user.getOutput().println("BET " + bet);
		user.getOutput().flush();
	}
	
	public static void sendRegister(User user, String register){
		user.getOutput().println("REGISTER " + register);
		user.getOutput().flush();
	}
	
	public static void sendLogin(User user, String login){
		user.getOutput().println("LOGIN " + login);
		user.getOutput().flush();
	}
	
	public static void sendError(User user, String error){
		user.getOutput().println("ERROR " + error);
		user.getOutput().flush();
	}
	
	public static void sendPop(User user, String pop){
		user.getOutput().println("POP " + pop);
		user.getOutput().flush();
	}
}
