import java.net.*;
import java.io.*;

public class BlackjackServer extends Blackjack
{
	/*
		Craig Martin
		April 25, 2008

		Sets up a server for the Blackjack Game

		Instance Variables
				private Thread pingServerThread			Thread for the ping server
				private int port						port number
				private RemotePlayer[] players			Array of RemotePlayers
				private Dealer dealer					Dealer
				private Shoe shoe						Shoe for the game
				private boolean[] activePlayer			Boolean for active players
				private int[] results					Results for Win/Lose/Push
		Constructor
			Creates threads and initializes instance variables
		Methods
			public void playGame()						Plays a Blackjack game with Clients
			public String printResults()				Method that will print out the results for W/L/P
			public void addPlayer(RemotePlayer player)	Adds players to the RemotePlayer array
			public void dealFirstRound()				Deals first round of cards to players
		Modification History
	*/
	private Thread pingServerThread;
	private int port;
	private RemotePlayer[] players;
	private Dealer dealer;
	private Shoe shoe;
	private boolean[] activePlayer;
	private int[] results;

	public BlackjackServer(int shoeSize, int maxPlayers, int port) throws IOException, ClassNotFoundException
	{
		super(shoeSize, maxPlayers);

		boolean isPlayers;
		isPlayers = false;

		this.port = port;
		this.players = new RemotePlayer[maxPlayers];
		if(shoeSize >= 2 && shoeSize <= 8)
			this.shoe = new Shoe(shoeSize);
		else
			throw new IllegalArgumentException("Blackjack.constructor: Invalid show size");

		if(maxPlayers >= 1 && maxPlayers <= 6)
		{
			this.players = new RemotePlayer[maxPlayers];
			this.results = new int[maxPlayers];
			this.activePlayer = new boolean[maxPlayers];
			for(int i=0; i<maxPlayers; i++)
				this.activePlayer[i] = false;
		}

		this.dealer = new Dealer(this.shoe);
		this.pingServerThread = new Thread(new PingServer());
		this.pingServerThread.start();

		for(int i=0; i<this.players.length; i++)
			this.activePlayer[i]=false;

		System.out.println("Waiting for players...");

		while(true)
		{
			try{this.pingServerThread.sleep(5000);}
			catch(Exception e){}
			playGame();
		}

	}
	public void playGame() throws IOException, ClassNotFoundException
	{
		boolean flag;
		boolean allBusted;
		boolean atLeastOneActive;
		boolean done;
		String input;
		Hand dealerHand;
		Card[] dealerCards;
		allBusted = true;
		atLeastOneActive = false;
		done = false;

		for(int i=0; i<this.players.length; i++)
		{
			if(this.players[i] != null)
				atLeastOneActive = true;
		}

		while(atLeastOneActive)
		{
			atLeastOneActive = false;
			flag = true;

			//For loop to check if players wanted to play current round of BJ was ehre

			for(int i=0; i<this.players.length; i++)
			{
				this.activePlayer[i] = false;
				if(this.players[i] != null)
				{

					for(int j=0; j<this.players.length; j++)
					{
						if(this.players[j] != null && j!=i)
						{
							//Prints message to other clients that are not currently playing
							this.players[j].getOutput().println("\nWaiting for other players to make a decision...");
							this.players[j].getOutput().flush();
						}
					}

					done = false;

					while(!done)
					{
						this.players[i].getOutput().println("\nWould you like to play this round of Blackjack (y/n)?");
						input = this.players[i].getInput().readLine();

						if(input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"))
						{
							this.activePlayer[i] = true;
							this.players[i].resetHand();
							this.results = new int[6];
							done = true;
						}
						else if(input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n"))
						{
							this.players[i].getOutput().println("end");
							this.players[i].getSocket().close();
							this.players[i] = null;
							this.activePlayer[i] = false;
							done = true;
						}
						else
						{
							this.players[i].getOutput().println("\nPlease enter yes or no");
							done = false;
						}
					}
				}
			}


			System.out.println("\nActive players this round: ");
			for(int i=0; i<players.length; i++)
			{
				if(this.players[i]!=null)
				{
					if(this.activePlayer[i] == true)
					{
						System.out.println(this.players[i].getName());
					}
				}
			}


			for(int i=0; i<this.players.length; i++)
				if(this.activePlayer[i])
					atLeastOneActive = true;
			if(atLeastOneActive)
			{
				this.dealer.resetHand();

				dealFirstRound();

				//dealerHand = this.dealer.getHand();
				//dealerCards = dealerHand.getCards();

				//if(this.dealer.is21())
				/*{
					for(int i=0; i<this.players.length; i++)
						if(this.activePlayer[i])
							this.results[i] = this.dealer.winLoseOrPush(this.players[i]);
				//}*/
				//else
				//{
					for(int i=0; i<this.players.length; i++)
					{
						if(this.activePlayer[i] && this.players[i].is21())
							this.results[i] = 1;
					}

					for(int i=0; i<this.players.length; i++)
					{
						if(this.activePlayer[i])
						{
							//Prints dealers first card only and hides the second card
							//this.players[i].getOutput().println("\n\n\n\n\n\n\n\n\n\n\n\n");
							//this.players[i].getOutput().println("_____________________________________________");
							//this.players[i].getOutput().println("\tDealer\t\t" + dealerCards[0] + "\n\t\t\t*******");

							//Prints players names and cards
							for(int j=0; j<this.players.length; j++)
							{
								if(this.players[j] != null && this.activePlayer[j])
								{
									this.players[i].getOutput().println("\n\t" + this.players[j]);
									this.players[i].getOutput().flush();
								}
							}
							this.players[i].getOutput().println("_____________________________________________");
						}
					}

					for(int i=0; i<this.players.length; i++)
					{
						if(this.players[i] == null || !this.activePlayer[i])
							flag = false;

						for(int j=0; j<this.players.length; j++)
						{
							if(this.players[j] != null && j!=i && this.activePlayer[j] && this.activePlayer[i])
							{
								//Prints message to other clients that are not currently playing
								this.players[j].getOutput().println("\nWaiting for other players to make a decision...");
								this.players[j].getOutput().flush();
							}
						}

						while(flag)
						{




							if(this.activePlayer[i])
								flag = this.players[i].hitMe();

							if(flag && this.activePlayer[i])
								this.dealer.hitPlayer(this.players[i]);
							if(this.activePlayer[i] && this.players[i].isBusted())
							{
								this.players[i].getOutput().println("\n\t   ****");
								this.players[i].getOutput().println("\tYou busted");
								this.players[i].getOutput().println("\t   ****\n\n");
								this.players[i].getOutput().flush();
								flag = false;
							}
							if(this.activePlayer[i] && this.players[i].is21())
							{
								this.players[i].getOutput().println("\n\t   ****");
								this.players[i].getOutput().println("\tYou have 21");
								this.players[i].getOutput().println("\t   ****\n\n");
								this.players[i].getOutput().flush();
								flag = false;
							}
						}
						flag = true;
					}

					for(int i=0; i<this.players.length; i++)
					{
						if(activePlayer[i] && !this.players[i].isBusted())
							allBusted = false;
					}
					while(this.dealer.hitMe() & !allBusted)
					{
						this.dealer.hitPlayer(this.dealer);
					}
					for(int i=0; i<this.players.length; i++)
						if(this.activePlayer[i] && this.results[i] < 1)
							this.results[i] = this.dealer.winLoseOrPush(this.players[i]);
				}

				for(int i=0; i<this.players.length; i++)
				{
					if(activePlayer[i])
					{
						this.players[i].getOutput().println(printResults());
						this.players[i].getOutput().flush();
						this.players[i].getOutput().println("\n\n");
					}
				}
			//}
		}

	}

	public String printResults()
	{
		String result = "";


		result = result + "\n____________ RESULTS ____________";
		result = result + "\n\n\t" + this.dealer.toString() + "\n\n";
			for(int i=0; i<this.players.length; i++)
			{
				if(activePlayer[i])
				{
					result = result + "\t----------\n\n";
					result = result + "\t" + this.players[i].toString() + "\n";
					if(results[i] == 0)
						result = result + "\n\t***Push***\n\n";
					if(results[i] > 0)
						result = result + "\n\t***Win***\n\n";
					if(results[i] < 0)
						result = result + "\n\t***Lose***\n\n";
				}
			}
		result = result + "_________________________________";
		return result;
	}

	public void addPlayer(RemotePlayer player)
	{
		for(int i=0; i<this.players.length; i++)
		{
			if(this.players[i] == null)
			{
				this.players[i] = player;
				i = this.players.length;
			}
		}
	}

	public void deletePlayer(RemotePlayer player)
	{
		for(int i=0; i<this.players.length; i++)
		{
			if(this.activePlayer[i])
			{
				if(this.players[i].equals(player))
				{
					this.players[i] = null;
				}
			}
		}
	}

	public void dealFirstRound()
	{
		for(int j=0; j<2; j++)
		{
			for(int i=0; i<this.players.length; i++)
				if(this.activePlayer[i])
					this.dealer.deal(this.players[i]);

			this.dealer.deal(this.dealer);
		}
	}

	class PingServer implements Runnable
	{
		/*
			Craig Martin
			April 25, 2008

			Receives connections from clients

			Instance Variables
			Constructor
			Methods
				public void run()			Starts a thread/connects clients to server/starts handshake thread
			Modification History
		*/
		public void run()
		{
			try
			{
				ServerSocket pingServer;
				Socket client = null;
				Thread handshakeThread;
				pingServer = new ServerSocket(80);


				while(!Thread.currentThread().isInterrupted())
				{
					client = pingServer.accept();

					System.out.println("Received ping from " + client.getInetAddress());

					handshakeThread = new Thread(new Handshake(client));
					handshakeThread.start();
				}
			}
			catch(Exception e){throw new RuntimeException();}
		}
	}

	class Handshake implements Runnable
	{
		/*
			Craig Martin
			April 25, 2008

			Asks the client if they want to login to the server

			Instance Variables
				private Socket client			the client thats connecting to the server
			Constructor
				Initializes the client variable
			Methods
				public void run()		Gets clients login info and adds them to the player array
			Modification History
		*/
		private Socket client;

		public Handshake(Socket client)
		{
			this.client = client;
		}
		public void run()
		{
			String name;
			String input;
			boolean done;
			done = false;

			try
			{
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

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
						RemotePlayer player = new RemotePlayer(name, this.client, out, in);

						done=true;

						out.println("\nPlease wait for the current hand to finish.");

						try
						{
							addPlayer(player);
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
}