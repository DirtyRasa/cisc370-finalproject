import java.io.*;
import java.net.*;

public class RemotePlayer extends Player implements Serializable
{
	/*
		Craig Martin
		April 25, 2008

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
	private String name;
	private Socket client;
	PrintWriter out;
	BufferedReader in;

	public RemotePlayer(String name, Socket client, PrintWriter out, BufferedReader in)throws IOException, ClassNotFoundException
	{
		super(name);
		this.name = name;
		this.client = client;
		this.out = out;
		this.in = in;
	}

	public Socket getSocket()
	{
		return this.client;
	}

	public PrintWriter getOutput()
	{
		return this.out;
	}

	public BufferedReader getInput()
	{
		return this.in;
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
				this.out.println("\n\nYou have: " + getHand());
				this.out.println("\nWould you like to hit (y/n)?");
				this.out.flush();
				hold = in.readLine();

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
					this.out.println("No fuck you asshole." +
					"\nI told you to enter fucking YES or god damn fucking NO." +
					"\nDUMBASS");
					done = false;
				}
				else
				{
					this.out.println("Please enter yes or no");
					done = false;
				}
			}
		}

		return flag;
	}
}