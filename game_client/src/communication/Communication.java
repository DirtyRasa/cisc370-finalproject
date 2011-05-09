package communication;

import java.io.PrintWriter;

public class Communication {
	static PrintWriter _out;
	
	public Communication(PrintWriter out){
		_out = out;
	}
	public void receive(String msg){
		
	}
	
	public static void sendMessage(String msg){
		_out.println(msg);
		_out.flush();
	}
	
	public static void sendQuestion(String question){
		_out.println(question);
		_out.flush();
		//_out.println("<QUESTION>");
		//_out.flush();
	}
}
