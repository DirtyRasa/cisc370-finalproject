package communication;

import game.server.User;

public class Communication {	
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
		user.getOutput().println("BANK $" + bank + "0");
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
	
	public static void sendGame(User user, String game){
		user.getOutput().println("GAME " + game);
		user.getOutput().flush();
	}
	
	public static void sendHands(User user, String hands){
		user.getOutput().println("HANDS " + hands);
		user.getOutput().flush();
	}
	
	public static void sendResults(User user, String results){
		user.getOutput().println("RESULTS " + results);
		user.getOutput().flush();
	}
	
	public static void sendDealer(User user){
		user.getOutput().println("DEALER");
		user.getOutput().flush();
	}
	
	public static void sendChat(User user, String chat){
		user.getOutput().println("CHAT " + chat);
		user.getOutput().flush();
	}
	
	public static void sendTable(User user, String table){
		user.getOutput().println("TABLE You are currently sitting at table " + table);
		user.getOutput().flush();
	}
}
