package communication;

import game.server.User;

public class Communication {
	public void receive(String msg){
		
	}
	
	public static void sendMessage(User user, String msg){
		user.getOutput().print(msg);
		user.getOutput().flush();
	}
	
	public static void sendQuestion(User user, String question){
		user.getOutput().print(question);
		user.getOutput().flush();
		user.getOutput().print("<QUESTION>");
		user.getOutput().flush();
	}
	
	public static void getPassword(User user, String question){
		user.getOutput().print(question);
		user.getOutput().flush();
		user.getOutput().print("<PASSWORD>");
		user.getOutput().flush();
	}
}
