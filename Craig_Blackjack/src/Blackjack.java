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