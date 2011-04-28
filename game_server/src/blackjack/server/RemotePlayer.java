package blackjack.server;
import java.io.*;
import java.net.*;

@SuppressWarnings("serial")
public class RemotePlayer extends Player implements Serializable
{
	/*
		Special kind of player for Client-Server

		Instance Variables
			private String name			Holds the name of the player
			private Socket client		Holds the socket for the player
			PrintWriter out				Output for the player
			BufferedReader in			Input for the player
		Constructor
			Initializes all instance variables
		Methods
			public Socket getSocket()			returns the socket of the user
			public PrintWriter getOutput()		Enables the user to send Strings
			public BufferedReader getInput()	Enables the user to receive Strings
			public boolean hitMe()				Asks the user if they want to hit
		Modification History
	*/
	private Socket _client;
	PrintWriter _out;
	BufferedReader _in;

	public RemotePlayer(String name, Socket client, PrintWriter out, BufferedReader in)throws IOException, ClassNotFoundException
	{
		super(name);
		_client = client;
		_out = out;
		_in = in;
	}

	public Socket getSocket()
	{
		return _client;
	}

	public PrintWriter getOutput()
	{
		return _out;
	}

	public BufferedReader getInput()
	{
		return _in;
	}

	public boolean hitMe() throws IOException, ClassNotFoundException
	{
		boolean flag;
		String hold;
		boolean done;

		done = false;
		flag = true;

		if(this.isBusted() || this.is21())
			flag = false;
		if(flag)
		{
			while(!done)
			{
				_out.println("\n\nYou have: " + getHand());
				_out.println("\nWould you like to hit (y/n)?");
				_out.flush();
				hold = _in.readLine();

				if(hold.equalsIgnoreCase("yes") || hold.equalsIgnoreCase("y"))
				{
					flag = true;
					done = true;
				}
				else if(hold.equalsIgnoreCase("no") || hold.equalsIgnoreCase("n"))
				{
					flag = false;
					done = true;
				}
				else if(hold.equalsIgnoreCase("Fuck you"))
				{
					_out.println("No fuck you asshole." +
					"\nI told you to enter fucking YES or god damn fucking NO." +
					"\nDUMBASS");
					done = false;
				}
				else
				{
					_out.println("Please enter yes or no");
					done = false;
				}
			}
		}

		return flag;
	}
}