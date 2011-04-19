package blackjack.server.c;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BlackjackHandshakeThread extends Thread{
	private Socket _client = null;
	Blackjack _blackjack;
	
	public BlackjackHandshakeThread(Socket client, Blackjack blackjack) {
		_client = client;
		_blackjack = blackjack;
	}
	
	public void run(){
		String input = null, name = null;
		boolean done = false;
		try{
			PrintWriter out = new PrintWriter(_client.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(_client.getInputStream()));

			while(!done)
			{
				out.println("\nWould you like to play Blackjack (y/n)?");
				out.flush();
				input = in.readLine();

				if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"))
				{
					out.println("\nWhat is your name?");
					out.flush();
					name = in.readLine();
					RemotePlayer player = new RemotePlayer(name, _client, out, in);

					done=true;

					out.println("\nPlease wait for the current hand to finish.");

					try
					{
						_blackjack.addPlayer(player);
					}
					catch(Exception e){throw new TableFullException("Table is currently full");}
				}
				else if(input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
				{
					out.println("end");
					done = true;
				}
				else
				{
					out.println("\nPlease enter yes or no");
					done = false;
				}
			}
		}
		catch(Exception e){throw new RuntimeException();}
	}
}
