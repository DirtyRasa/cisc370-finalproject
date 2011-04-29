package communication;

import java.io.PrintWriter;

public class Communication {
	public void receive(String msg){
		
	}
	
	public static void sendMessage(PrintWriter out, String msg){
		out.println(msg);
		out.flush();
	}
	
	public static void sendQuestion(PrintWriter out, String question){
		out.println(question);
		out.flush();
		out.println("<QUESTION>");
		out.flush();
	}
	
	public static void getPassword(PrintWriter out, String question){
		out.println(question);
		out.flush();
		out.println("<PASSWORD>");
		out.flush();
	}
}
