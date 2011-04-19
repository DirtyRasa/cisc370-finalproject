package game.server.c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GameConnectionThread extends Thread{
	public void run(){
		try{
			ServerSocket socket = new ServerSocket(80);
			Socket client = null;
			boolean response = false;
			boolean done = false;
			
			while(!Thread.currentThread().isInterrupted()){
				client = socket.accept();
				
				InputStreamReader isr = new InputStreamReader(client.getInputStream());
				BufferedReader in = new BufferedReader(isr);
				PrintWriter out = new PrintWriter(client.getOutputStream());
				
				out.println("***** Welcome to the Game Server *****");
				out.flush();
				
				do
				{
					out.println("Do you already have an account? (Y/N) ");
					out.flush();
					try{
						done = Response.eval(in.readLine()) ? Login(in, out) : Register(in, out);
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
				client.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//TO-DO: Add Throw Exception for database error
	public boolean Register(BufferedReader in, PrintWriter out){
		String response = null;
		out.println("***** Registration *****");
		out.println("Please enter a user name: ");
		out.flush();
		try {
			response = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println("Please enter a password: ");
		out.flush();
		try {
			response = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean Login(BufferedReader in, PrintWriter out){
		String response = null;
		out.println("***** Login *****");
		out.println("Please enter your user name: ");
		out.flush();
		try {
			response = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println("Please enter your password: ");
		out.flush();
		try {
			response = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
