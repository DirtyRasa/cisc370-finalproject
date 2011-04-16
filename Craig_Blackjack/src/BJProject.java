public class TableFullException extends RuntimeException
{
	/*
		Craig Martin
		QMCS 281
		Prof Jarvis
		April 25, 2008
	*/
	public TableFullException() {super();}
	public TableFullException(String message){super(message);}
}

public class Ace extends Card
{
	/*
		Craig Martin
		March 10, 2008

		Special kind of card

		Instance Variables
			private String suit			Holds the suit for the card
		Constructor
			Initializes the instance variables
		Methods
			int[] getValue				returns 1 and 11
		Modification History
	*/
	private String suit;

	public Ace(String suit)
	{
		super(1, suit);
	}

	public int[] getValue()
	{
		int[] value = {1, 11};
		return value;
	}
}

import java.io.*;

public class Blackjack
{
	/*
		Craig Martin
		March 10, 2008

		Core of the Blackjack game

		Instance Variables
			private Dealer dealer				Dealer object
			private Player[] players			Array of players from 1-6
			private Shoe shoe					Shoe consisting of 2-8 decks
			private int[] results				Holds the W/L/P info for each player
			private boolean[] activePlayer		Shows if the player is active or not
		Constructor
			creates dealer, n players, and a shoe; Checks input for validity
		Methods
			public void playGame()								Plays a game of Blackjack
			public void addPlayerToGame(Player player)			Adds a player to the game
			public boolean activatePlayer(Player player)		Activates the player so they can play
			public boolean deactivatePlayer(Player player)		Deactivates the player so they can't play
			public boolean deletePlayer(Player player)			Removes the player from the game
			public void dealFirstRound()						Deals 2 cards to each player and the dealer
			public String toString()							Prints out the results
		Modification History
			April 25, 2008
				Added:
					public boolean[] getActivePlayers()			returns active players
	*/

	private Dealer dealer;
	private Player[] players;  // has nulls where there are no players @ that seat
	private Shoe shoe;
	private int[] results;
	private boolean[] activePlayer; //active|inactive

	public Blackjack(int shoeSize, int maxPlayers) throws IOException
	{
		if(shoeSize >= 2 && shoeSize <= 8)
			this.shoe = new Shoe(shoeSize);
		else
			throw new IllegalArgumentException("Blackjack.constructor: Invalid show size");

		this.dealer = new Dealer(this.shoe);

		if(maxPlayers >= 1 && maxPlayers <= 6)
		{
			this.players = new Player[maxPlayers];
			this.results = new int[maxPlayers];
			this.activePlayer = new boolean[maxPlayers];
			for(int i=0; i<maxPlayers; i++)
				this.activePlayer[i] = false;
		}
		else throw new IllegalArgumentException("Blackjack.constructor: Invalid number of players");
		this.shoe.shuffle();
	}

	public void playGame() throws IOException, ClassNotFoundException
	{
		boolean flag;
		boolean allBusted;
		Hand dealerHand;
		Card[] dealerCards;
		allBusted = true;
		flag = true;

		for(int i=0; i<this.players.length; i++)
		{
			if(this.activePlayer[i])
			{
				this.players[i].resetHand();
				this.dealer.resetHand();
				this.results = new int[6];
			}
		}


		this.dealFirstRound();

		dealerHand = this.dealer.getHand();
		dealerCards = dealerHand.getCards();

		if(this.dealer.is21())
		{
			for(int i=0; i<this.players.length; i++)
				if(this.activePlayer[i])
					this.results[i] = this.dealer.winLoseOrPush(this.players[i]);
		}
		else
		{
			for(int i=0; i<this.players.length; i++)
				if(this.activePlayer[i] && this.players[i].is21())
					this.results[i] = 1;

			System.out.println("Dealer | " + dealerCards[0] + "\n");

			for(int i=0; i<this.players.length; i++)
			{
				if(activePlayer[i])
				{
					while(flag)
					{
							System.out.println(this.players[i]);
							flag = this.players[i].hitMe();

							if(flag)
								this.dealer.hitPlayer(this.players[i]);
							if(this.players[i].isBusted())
							{
								System.out.println("You busted");
								flag = false;
							}
							if(this.players[i].is21())
							{
								System.out.println("You have 21");
								flag = false;
							}
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
	}

	public boolean[] getActivePlayers()
	{
		return this.activePlayer;
	}

	public void addPlayerToGame(Player player)
	{
		boolean isTableFull;
		isTableFull = true;

		for(int i=0; i<this.players.length; i++)
		{
			if(this.players[i] == null)
			{
				this.players[i] = player;
				isTableFull = false;
				i=this.players.length;
			}
		}
		if(isTableFull)
			throw new TableFullException("Blackjack; addPlayerToGame: The table is full, can not add anymore players!");
	}

	public boolean activatePlayer(Player player)
	{
		boolean flag;
		flag = false;
		for(int i=0; i<this.players.length; i++)
		{
			if(this.players[i] != null && this.players[i].equals(player))
			{
				this.activePlayer[i] = true;
				flag = true;
			}
		}
		return flag;

	}

	public boolean deactivatePlayer(Player player)
	{
		boolean flag;
		flag = false;
		for(int i=0; i<this.players.length; i++)
		{
			if(this.players[i].equals(player) && this.players[i] != null)
			{
				this.activePlayer[i] = false;
				flag = true;
			}
		}
		return flag;
	}

	public boolean deletePlayer(Player player)
	{
		boolean flag;
		flag = false;
		for(int i=0; i<this.players.length; i++)
		{
			if(this.players[i].equals(player))
			{
				this.players[i] = null;
				flag = true;
			}
		}
		return flag;
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

	public String toString()
	{
		String result = "";
		for(int i=0; i<this.players.length; i++)
		{
			if(activePlayer[i])
			{
				result = result + this.players[i].toString() + "\n";
				if(results[i] == 0)
					result = result + "***Push***\n\n";
				if(results[i] > 0)
					result = result + "***Win***\n\n";
				if(results[i] < 0)
					result = result + "***Lose***\n\n";
			}
		}

		result = result + "Dealer:\n" + this.dealer.toString();
		return result;
	}
}

import java.net.*;
import java.io.*;

public class BlackjackClient
{
	/*
		Craig Martin
		QMCS 281
		Prof Jarvis
		April 25, 2008
	*/
	public static void main(String[] args)throws UnknownHostException, IOException, ClassNotFoundException
	{
		Socket client;
		String ipNumber;
		String input = "";
		String hold = "";
		ipNumber = "140.209.122.7";
		client = new Socket(ipNumber, 80);

		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));


		while(!input.equals("end"))
		{
			input = in.readLine();
			if(input.endsWith("?"))
			{
				hold = getStringFromUser(input);
				out.println(hold);
				out.flush();
			}
			else
				System.out.println(input);
		}
		System.out.println("Client has ended");
	}

	public static String getStringFromUser(String prompt)throws IOException
	{
		String hold;
		BufferedReader keyboard;

		keyboard = new BufferedReader(new InputStreamReader(System.in));

		System.out.print(prompt);
		hold = keyboard.readLine();
		return hold;
	}// getStringFromUser
}

import java.io.*;
import java.net.*;

public class BlackjackGame
{
	/*
		Craig Martin
		QMCS 281
		Prof Jarvis
		April 25, 2008
	*/
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		int[] results;
		String hold;
		Blackjack game;
		Player[] players = {new Player("Michael"),
					   		new Player("Jessica")};
		game = new Blackjack(4, 6);

		for(int i=0; i<players.length; i++)
		{
				game.addPlayerToGame(players[i]);
				game.activatePlayer(players[i]);
		}

		hold = "yes";
		while(hold.equalsIgnoreCase("yes") || hold.equalsIgnoreCase("y"))
		{
			game.playGame();
			System.out.println("\n" + game +"\n");

			hold = getStringFromUser("\n\nWould you like to play again?");
		}

		//results = game.getResults();


	}

	public static String getStringFromUser(String prompt)throws IOException
	{
		String hold;
		BufferedReader keyboard;

		keyboard = new BufferedReader(new InputStreamReader(System.in));

		System.out.print(prompt);
		hold = keyboard.readLine();
		return hold;
	}// getStringFromUser
}

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

		System.out.println("Waiting for players...");
		while(!isPlayers)
		{
			for(int i=0; i<this.players.length; i++)
				if(this.players[i] != null)
					isPlayers = true;
		}
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
		String input;
		Hand dealerHand;
		Card[] dealerCards;
		allBusted = true;
		atLeastOneActive = true;

		while(atLeastOneActive)
		{
			atLeastOneActive = false;
			flag = true;
			for(int i=0; i<this.players.length; i++)
			{
				this.activePlayer[i] = false;
				if(this.players[i] != null)
				{
					this.players[i].getOutput().println("Would you like to play this round of Blackjack (y/n)?");
					input = this.players[i].getInput().readLine();
					if(input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"))
					{
						this.activePlayer[i] = true;
						this.players[i].resetHand();
						this.results = new int[6];
					}
					else
					{
						this.players[i].getOutput().println("end");
						this.players[i].getSocket().close();
						this.players[i] = null;
						this.activePlayer[i] = false;
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

				dealerHand = this.dealer.getHand();
				dealerCards = dealerHand.getCards();

				if(this.dealer.is21())
				{
					for(int i=0; i<this.players.length; i++)
						if(this.activePlayer[i])
							this.results[i] = this.dealer.winLoseOrPush(this.players[i]);
				}
				else
				{
					for(int i=0; i<this.players.length; i++)
					{
						if(this.activePlayer[i] && this.players[i].is21())
							this.results[i] = 1;
					}

					for(int i=0; i<this.players.length; i++)
					{
						if(this.activePlayer[i])
							this.players[i].getOutput().println("\n\n\n\n\n\n\n\n\n\n\n\nDealer | " + dealerCards[0] + "\n");
					}

					for(int i=0; i<this.players.length; i++)
					{
						if(this.players[i] == null)
							flag = false;
						while(flag)
						{
							for(int j=0; j<this.players.length; j++)
							{
								if(this.activePlayer[j] && this.activePlayer[i])
								{
									this.players[i].getOutput().println("\n" + this.players[j]);
									this.players[i].getOutput().flush();
								}
							}
							if(this.activePlayer[i])
								flag = this.players[i].hitMe();

							if(flag && this.activePlayer[i])
								this.dealer.hitPlayer(this.players[i]);
							if(this.activePlayer[i] && this.players[i].isBusted())
							{
								this.players[i].getOutput().println("\nYou busted");
								this.players[i].getOutput().flush();
								flag = false;
							}
							if(this.activePlayer[i] && this.players[i].is21())
							{
								this.players[i].getOutput().println("\nYou have 21");
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
			}
		}

	}

	public String printResults()
	{
		String result = "";
			for(int i=0; i<this.players.length; i++)
			{
				if(activePlayer[i])
				{
					result = result + this.players[i].toString() + "\n";
					if(results[i] == 0)
						result = result + "***Push***\n\n";
					if(results[i] > 0)
						result = result + "***Win***\n\n";
					if(results[i] < 0)
						result = result + "***Lose***\n\n";
				}
			}

			result = result + "Dealer:\n" + this.dealer.toString();
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

					System.out.println("Received ping form " + client.getInetAddress());

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
			try
			{
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

				out.println("Would you like to play Blackjack (y/n)?");
				out.flush();
				input = in.readLine();

				if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"))
				{
					out.println("What is your name?");
					out.flush();
					name = in.readLine();
					RemotePlayer player = new RemotePlayer(name, this.client, out, in);
					try
					{
						addPlayer(player);
					}
					catch(Exception e){throw new TableFullException("Table is currently full");}
				}
			}
			catch(Exception e){throw new RuntimeException();}
		}
	}
}

import java.net.*;
import java.io.*;

public class BlackjackServerTest
{
	/*
		Craig Martin
		QMCS 281
		Prof Jarvis
		April 25, 2008
	*/
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{

		BlackjackServer bjServer = new BlackjackServer(3, 6, 80);
	}
}

import java.net.*;
import java.io.*;

public class Card implements Serializable
{
	/*
		Craig Martin
		March 25, 2008

		Class will simulate one playing card

		Instance Variables
			int rank			Holds a value from 1-13
			String suit			Holds the suit of the card
			boolean flag		boolean for testing

		Constructor
			Creates a card with a suit and a rank. Checks if rank and suit are valid.

		Methods
			public int getRank()			returns the rank of the card
			public String getSuit()			returns the suit of the card

	*/
	private int rank;
	private String suit;
	private boolean flag;

	public Card(int rank, String suit)
	{
		this.rank = rank;
		this.suit = suit;
		if(this.rank > 13 || this.rank < 1)
			throw new IllegalArgumentException("PlayingCard; Invalid rank");
		//if(!isHeart() && !isDiamond() && !isSpade() && !isClub())
		//	throw new IllegalArgumentException("PlayingCard; Invalid suit");
	}

	public int getRank()
	{
		return this.rank;
	}

	public int[] getValue()
	{
		int[] value = {this.getRank(), this.getRank()};

		return value;
	}

	public String getSuit() {return this.suit;}
	public String getName()
	{
		String[] name = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
		return name[this.rank-1];
	}

	public String toString()
	{
		return getName() + " of " + getSuit();
	}
}

import java.io.*;

public class Dealer extends Player
{
	/*
		Craig Martin
		March 10, 2008

		Special kind of player that knows the rules of the game

		Instance Variables
		Constructor
			Creates a dealer hand and tests if shoe is valid
		Methods
			public void deal(Player)
			hitPlayer(Player)
			boolean hitMe()
			WinLosePush(Player)
		Modification History
	*/
	private Hand hand;
	private Shoe shoe;

	public Dealer(Shoe shoe)
	{
		super("Dealer");
		//this.hand = new Hand();
		if(shoe == null)
			throw new IllegalArgumentException("Dealer.constructor: Shoe object is null");
		else
			this.shoe = shoe;
	}

	public void deal(Player player)
	{
		Card card;
		card = this.shoe.deal();
		player.addCard(card);
	}

	public void hitPlayer(Player player) throws IOException
	{
		//if(player.hitMe())
			this.deal(player);
	}

	public boolean hitMe()
	{
		boolean flag;
		int[] score;
		Hand dealerHand;
		dealerHand = this.getHand();

		flag = false;
		score = dealerHand.getValues();

		if(score[1] < 17)
			flag = true;
		return flag;
	}

	public int winLoseOrPush(Player player)
	{
		//0 = push
		//0 < win
		//0 > lose
		Hand playerHand;
		Hand dealerHand;
		int flag = 0;
		int[] dealerScore;
		int[] playerScore;
		int dScore = 0;
		int pScore = 22;

		playerHand = player.getHand();
		dealerHand = this.getHand();
		dealerScore = dealerHand.getValues();
		playerScore = playerHand.getValues();

		//if(this.has21() && player.has21())
		//	flag = 0;
		//if(this.has21() && !player.has21())
		//	flag = -1;
		for(int i=0; i< dealerScore.length; i++)
		{
			if(dealerScore[i] < 22)
				dScore = dealerScore[i];
			if(playerScore[i] < 22)
				pScore = playerScore[i];
		}

		if(pScore > dScore)
			flag = 1;
		if(pScore == dScore)
			flag = 0;
		if(pScore < dScore)
			flag = -1;
		if(pScore > 21)
			flag = -1;

		return flag;
	}

/*
	public String toString()
	{
		String result;
		Hand dealerHand;
		Card[] cards;
		dealerHand = this.getHand();
		cards = dealerHand.getCards();
		result = "";

		result = result + this.getName() + " " + cards[0] + "| ***";

		return result;

	}*/
}

public class Deck
{
	/*
		Craig Martin
		Februaury 20, 2008

		This class creates a deck of 52 cards

		Instance Variables
			private Card2[] deck		Creates a deck using class Card2
			private int cardsDealt		Keeps track of the cards dealt

		Constructor
			Creates the deck consisting of all 4 suits and all 52 cards

		Methods
			public void shuffle()				Shuffles all the cards in the deck
			public Card deal()					Deals the top card
			public boolean allCardsDealt()		Sees if there are any cards left in the deck
			public String toString()			Prints out all 52 cards

	*/
	private Card[] deck;
	private int cardsDealt;

	public Deck()
	{
		cardsDealt = 0;
		int counter = 0;
		String[] suits = {"Clubs", "Diamonds", "Spades", "Hearts"};

		this.deck = new Card[52];

		for(int i=0; i<suits.length; i++)
		{
			for(int j=1; j<14; j++)
			{
				if(j == 1)
					this.deck[counter] = new Ace(suits[i]);
				if(j>10)
					this.deck[counter] = new Face(j, suits[i]);
				if(j > 1 && j<11)
					this.deck[counter] = new Card(j, suits[i]);
				counter = counter + 1;
			}//for(int j=1; j<14; j++)
		}//for(int i=0; i<suits.length; i++)

	}

	public void shuffle()
	{
		Card temp;
		int spot1;
		int spot2;

		cardsDealt = 0;

		for(int i=0; i<1000; i++)
		{
			spot1 = (int)(Math.random()*(this.deck.length-1));
			spot2 = (int)(Math.random()*(this.deck.length-1));
			temp = this.deck[spot1];
			this.deck[spot1] = this.deck[spot2];
			this.deck[spot2] = temp;
		}
	}

	public Card[] deal(int howManyCards)
	{
		Card[] hand = new Card[howManyCards];

		if(this.deck.length - this.cardsDealt < howManyCards)
			throw new RuntimeException("Deck does not have enough cards");

		for(int i=0; i<hand.length; i++)
		{
			hand[i] = this.deck[this.cardsDealt];
			this.cardsDealt = this.cardsDealt + 1;
		}
		return hand;
	}

	public Card deal()
	{
		//deals 1 card
		Card card;
		if(cardsDealt < 52)
		{
			card = this.deck[cardsDealt];
			this.cardsDealt = this.cardsDealt + 1;
		}
		else
			throw new DeckEmptyException("Out of cards");
		return card;
	}

  	public boolean allCardsDealt()
  	{
		return this.cardsDealt >= 52;
	}

	public String toString()
	{
		String result = "";
		for(int i =0; i<this.deck.length; i++)
		{
			result = result + this.deck[i].toString()+"\n";
		}
		return result;
	}
}

public class DeckEmptyException extends RuntimeException
{
	/*
		Craig Martin
		QMCS 281
		Prof Jarvis
		April 25, 2008
	*/
	public DeckEmptyException(){super();}
	public DeckEmptyException(String message){super(message);}
}

public class Face extends Card
{
	/*
		Craig Martin
		March 10, 2008

		Special kind of card

		Instance Variables
			private String suit			Holds suit of the card
		Constructor
			calls the Card constructor
		Methods
			int[] getValue			returns 10 for all Face cards
		Modification History
	*/
	private String suit;

	public Face(int rank, String suit)
	{
		super(rank, suit);
	}

	public int[] getValue()
	{
		int[] value = {10, 10};
		return value;
	}
}

import java.net.*;
import java.io.*;

public class Hand implements Serializable
{
	/*
		Craig Martin
		March 10, 2008

		Holds the cards for each player

		Instance Variables
			private Card[] cards			Card array
		Constructor
			Initializes card array
		Methods
			addCard(Card)					adds a card to the hand
			int[] getValues()				gets the values of the hand
			public Card[] getCards()		returns the card array
		Modification History
		April 25, 2008
			Added:
				public void resetHand()		resets the hand for a new round of Blackjack
	*/
	private Card[] cards;

	public Hand()
	{
		this.cards = new Card[0];
		//create cards
	}

	public void addCard(Card card)
	{
		Card[] temp;
		temp = new Card[this.cards.length+1];
		for(int i=0; i<this.cards.length; i++)
			temp[i] = this.cards[i];
		temp[this.cards.length] = card;
		this.cards = temp;
	}

	public void resetHand()
	{
		this.cards = new Card[0];
	}

	public int[] getValues()
	{
		//score[0] = min value; score[1] = max value
		int[] score = new int[2];
		int[] temp;
		int aceCounter = 0;

		for(int i=0; i<this.cards.length; i++)
		{
			temp = this.cards[i].getValue();
			if(temp[1] == 11)
				aceCounter = aceCounter + 1;
		}

		for(int i=0; i<this.cards.length; i++)
		{
			temp = this.cards[i].getValue();
			score[0] = score[0] + temp[0];
			if(aceCounter > 1 && temp[1] == 11)
			{
				score[1] = score[1] + temp[0];
				aceCounter = aceCounter - 1;
			}
			else
				score[1] = score[1] + temp[1];
		}
		return score;
	}

	public Card[] getCards()
	{
		return this.cards;
	}

	public String toString()
	{
		String result= "";
		int[] values;
		values = this.getValues();
		for(int i=0; i<values.length; i++)
		{
			if(values[0] == values[values.length-1])
			{
				result = result + values[0];
				i=values.length;
			}
			else
			{
				result = result + values[i];
				if(i<values.length-1)
					result = result + ",";
			}
		}
		result = result + "|";
		for(int i=0; i<this.cards.length;i++)
		{
			if(this.cards[i] != null)
				result = result + this.cards[i];
			if(i<this.cards.length-1)
				result = result + "|";
		}
		return result;
	}
}

import java.io.*;
import java.net.*;

public class Player implements Serializable
{
	/*
		Craig Martin
		March 10, 2008

		Player that plays a game of Blackjack

		Instance Variables
			private Hand hand			holds the cards for the player
			private String name			holds the name of the player
		Constructor
			Creates a player hand
			Initializes the player name
		Methods
			public String getName()				returns the name of the player
			public Hand getHand()				returns the hand of the player
			public void addCard(Card card)		adds a card to the hand
			public boolean is21()				checks if the hand is equal to 21
			public boolean isBusted()			checks if the hand is > 21
			public boolean hitMe()				checks if the player wants and can be hit
			public String toString()			prints out the hand of the player
		Modification History
			April 25, 2008
				Added:
					public void resetHand()		resets the hand of the player for a new round of Blackjack

	*/
	private Hand hand;
	private String name;

	public Player(String name)
	{
		this.hand = new Hand();
		this.name = name;
	}

	public String getName(){return this.name;}

	public Hand getHand()
	{
		return this.hand;
	}

	public void addCard(Card card)
	{
		this.hand.addCard(card);
	}

	public void resetHand()
	{
		this.hand = new Hand();
	}

	public boolean is21()
	{
		boolean flag;
		int[] score = this.hand.getValues();
		flag = false;
		for(int i=0; i<score.length; i++)
			if(score[i] == 21)
				flag = true;

		return flag;
	}
	public boolean isBusted()
	{
		boolean flag;
		int[] score = this.hand.getValues();
		flag = true;
		for(int i=0; i<score.length; i++)
			if(score[i] < 22)
				flag = false;

		return flag;
	}

	public boolean hitMe() throws IOException, ClassNotFoundException
	{
		boolean flag;
		String hold;
		BufferedReader keyboard;

		keyboard = new BufferedReader(new InputStreamReader(System.in));
		flag = true;

		if(this.isBusted() || this.is21())
			flag = false;
		if(flag)
		{
			System.out.print("\n\nYou have: " + getHand());
			System.out.print("Would you like to hit? (y/n) ");
			hold = keyboard.readLine();

			if(hold.equalsIgnoreCase("yes") || hold.equalsIgnoreCase("y"))
				flag = true;
			if(hold.equalsIgnoreCase("no") || hold.equalsIgnoreCase("n"))
				flag = false;
		}

		return flag;
	}

	public String toString()
	{
		String result = "";
		result = this.name + " " + this.hand;
		return result;
	}
}

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

		flag = true;

		if(this.isBusted() || this.is21())
			flag = false;
		if(flag)
		{
			this.out.println("\n\nYou have: " + getHand());
			this.out.println("Would you like to hit (y/n)?");
			this.out.flush();
			hold = in.readLine();

			if(hold.equalsIgnoreCase("yes") || hold.equalsIgnoreCase("y"))
				flag = true;
			if(hold.equalsIgnoreCase("no") || hold.equalsIgnoreCase("n"))
				flag = false;
		}

		return flag;
	}
}

public class Shoe
{
	/*
		Craig Martin
		March 10, 2008

		An array of decks

		Instance Variables
			private Deck[] decks			Array of decks
			private int activeDecks			Decks with cards
		Constructor
			Creates a shoe with n number of decks
		Methods
			public Card deal()				Deals one card from the shoe to a player
			public void shuffle()			Takes a deck and calls deck.shuffle()
			public String toString()		Prints out all the cards
		Modification History
	*/
	private Deck[] shoe;
	private int activeDecks;

	public Shoe(int numberOfDecks)
	{
		if(numberOfDecks >= 2 && numberOfDecks <= 8)
		{
			this.shoe = new Deck[numberOfDecks];
			for(int i=0; i<numberOfDecks; i++)
			{
				this.shoe[i] = new Deck();
				this.shoe[i].shuffle();
			}
			activeDecks = this.shoe.length;
		}
		else
			throw new IllegalArgumentException("Shoe.constructor: Invalid amount of decks for the shoe");
		//create 2-8 decks
	}

	public Card deal()
	{
		int randDeck;
		Card cardDealt = null;

		randDeck = (int)(Math.random()*(this.shoe.length));

		if(cardDealt == null)
		{
			for(int i = randDeck; cardDealt == null; i=(i+1)%this.activeDecks)
			{
				if(!this.shoe[i].allCardsDealt())
					cardDealt = this.shoe[i].deal();
			}
		}

		return cardDealt;
	}

	public void shuffle()
	{
		for(int i=0; i<this.shoe.length; i++)
		{
			this.shoe[i].shuffle();
		}
	}

	public String toString()
	{
		String result = "";
		for(int i=0; i<this.shoe.length; i++)
		{
			result = result + this.shoe[i].toString();
		}
		return result;
	}
}

public class ShoeEmptyException extends RuntimeException
{
	/*
		Craig Martin
		QMCS 281
		Prof Jarvis
		April 25, 2008
	*/
	public ShoeEmptyException() {super();}
	public ShoeEmptyException(String message){super(message);}
}