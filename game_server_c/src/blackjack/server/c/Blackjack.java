package blackjack.server.c;

import cards.*;
import java.io.IOException;

public class Blackjack {
	private RemotePlayer[] _players;
	private Dealer _dealer;
	private Shoe _shoe;
	private boolean[] _activePlayer;
	private int[] _results;
	
	public Blackjack(int shoeSize, int maxPlayers) throws IOException, ClassNotFoundException
	{
		_players = new RemotePlayer[maxPlayers];
		if(shoeSize >= 2 && shoeSize <= 8)
			_shoe = new Shoe(shoeSize);
		else
			throw new IllegalArgumentException("Blackjack.constructor: Invalid show size");

		if(maxPlayers >= 1 && maxPlayers <= 6)
		{
			_players = new RemotePlayer[maxPlayers];
			_results = new int[maxPlayers];
			_activePlayer = new boolean[maxPlayers];
			for(int i=0; i<maxPlayers; i++)
				_activePlayer[i] = false;
		}

		_dealer = new Dealer(_shoe);

		for(int i=0; i<_players.length; i++)
			_activePlayer[i]=false;
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

		for(int i=0; i<this._players.length; i++)
		{
			if(this._players[i] != null)
				atLeastOneActive = true;
		}

		while(atLeastOneActive)
		{
			atLeastOneActive = false;
			flag = true;

			//For loop to check if players wanted to play current round of BJ was ehre

			for(int i=0; i<this._players.length; i++)
			{
				this._activePlayer[i] = false;
				if(this._players[i] != null)
				{

					for(int j=0; j<this._players.length; j++)
					{
						if(this._players[j] != null && j!=i)
						{
							//Prints message to other clients that are not currently playing
							this._players[j].getOutput().println("\nWaiting for other players to make a decision...");
							this._players[j].getOutput().flush();
						}
					}

					done = false;

					while(!done)
					{
						this._players[i].getOutput().println("\nWould you like to play this round of Blackjack (y/n)?");
						input = this._players[i].getInput().readLine();

						if(input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"))
						{
							this._activePlayer[i] = true;
							this._players[i].resetHand();
							this._results = new int[6];
							done = true;
						}
						else if(input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n"))
						{
							this._players[i].getOutput().println("end");
							this._players[i].getSocket().close();
							this._players[i] = null;
							this._activePlayer[i] = false;
							done = true;
						}
						else
						{
							this._players[i].getOutput().println("\nPlease enter yes or no");
							done = false;
						}
					}
				}
			}


			System.out.println("\nActive players this round: ");
			for(int i=0; i<_players.length; i++)
			{
				if(this._players[i]!=null)
				{
					if(this._activePlayer[i] == true)
					{
						System.out.println(this._players[i].getName());
					}
				}
			}


			for(int i=0; i<this._players.length; i++)
				if(this._activePlayer[i])
					atLeastOneActive = true;
			if(atLeastOneActive)
			{
				this._dealer.resetHand();

				dealFirstRound();

				dealerHand = this._dealer.getHand();
				dealerCards = dealerHand.getCards();

				if(this._dealer.is21())
				{
					for(int i=0; i<this._players.length; i++)
						if(this._activePlayer[i])
							this._results[i] = this._dealer.winLoseOrPush(this._players[i]);
				}
				else
				{
					for(int i=0; i<this._players.length; i++)
					{
						if(this._activePlayer[i] && this._players[i].is21())
							this._results[i] = 1;
					}

					for(int i=0; i<this._players.length; i++)
					{
						if(this._activePlayer[i])
						{
							//Prints dealers first card only and hides the second card
							this._players[i].getOutput().println("\n\n\n\n\n\n\n\n\n\n\n\n");
							this._players[i].getOutput().println("_____________________________________________");
							this._players[i].getOutput().println("\tDealer\t\t" + dealerCards[0] + "\n\t\t\t*******");

							//Prints players names and cards
							for(int j=0; j<this._players.length; j++)
							{
								if(this._players[j] != null && this._activePlayer[j])
								{
									this._players[i].getOutput().println("\n\t" + this._players[j]);
									this._players[i].getOutput().flush();
								}
							}
							this._players[i].getOutput().println("_____________________________________________");
						}
					}

					for(int i=0; i<this._players.length; i++)
					{
						if(this._players[i] == null || !this._activePlayer[i])
							flag = false;

						for(int j=0; j<this._players.length; j++)
						{
							if(this._players[j] != null && j!=i && this._activePlayer[j] && this._activePlayer[i])
							{
								//Prints message to other clients that are not currently playing
								this._players[j].getOutput().println("\nWaiting for other players to make a decision...");
								this._players[j].getOutput().flush();
							}
						}

						while(flag)
						{




							if(this._activePlayer[i])
								flag = this._players[i].hitMe();

							if(flag && this._activePlayer[i])
								this._dealer.hitPlayer(this._players[i]);
							if(this._activePlayer[i] && this._players[i].isBusted())
							{
								this._players[i].getOutput().println("\n\t   ****");
								this._players[i].getOutput().println("\tYou busted");
								this._players[i].getOutput().println("\t   ****\n\n");
								this._players[i].getOutput().flush();
								flag = false;
							}
							if(this._activePlayer[i] && this._players[i].is21())
							{
								this._players[i].getOutput().println("\n\t   ****");
								this._players[i].getOutput().println("\tYou have 21");
								this._players[i].getOutput().println("\t   ****\n\n");
								this._players[i].getOutput().flush();
								flag = false;
							}
						}
						flag = true;
					}

					for(int i=0; i<this._players.length; i++)
					{
						if(_activePlayer[i] && !this._players[i].isBusted())
							allBusted = false;
					}
					while(this._dealer.hitMe() & !allBusted)
					{
						this._dealer.hitPlayer(this._dealer);
					}
					for(int i=0; i<this._players.length; i++)
						if(this._activePlayer[i] && this._results[i] < 1)
							this._results[i] = this._dealer.winLoseOrPush(this._players[i]);
				}

				for(int i=0; i<this._players.length; i++)
				{
					if(_activePlayer[i])
					{
						this._players[i].getOutput().println(printResults());
						this._players[i].getOutput().flush();
						this._players[i].getOutput().println("\n\n");
					}
				}
			}
		}

	}

	public String printResults()
	{
		String result = "";


		result = result + "\n____________ RESULTS ____________";
		result = result + "\n\n\t" + this._dealer.toString() + "\n\n";
			for(int i=0; i<this._players.length; i++)
			{
				if(_activePlayer[i])
				{
					result = result + "\t----------\n\n";
					result = result + "\t" + this._players[i].toString() + "\n";
					if(_results[i] == 0)
						result = result + "\n\t***Push***\n\n";
					if(_results[i] > 0)
						result = result + "\n\t***Win***\n\n";
					if(_results[i] < 0)
						result = result + "\n\t***Lose***\n\n";
				}
			}
		result = result + "_________________________________";
		return result;
	}

	public void addPlayer(RemotePlayer player)
	{
		for(int i=0; i<this._players.length; i++)
		{
			if(this._players[i] == null)
			{
				this._players[i] = player;
				i = this._players.length;
			}
		}
	}

	public void deletePlayer(RemotePlayer player)
	{
		for(int i=0; i<this._players.length; i++)
		{
			if(this._activePlayer[i])
			{
				if(this._players[i].equals(player))
				{
					this._players[i] = null;
				}
			}
		}
	}

	public void dealFirstRound()
	{
		for(int j=0; j<2; j++)
		{
			for(int i=0; i<this._players.length; i++)
				if(this._activePlayer[i])
					this._dealer.deal(this._players[i]);

			this._dealer.deal(this._dealer);
		}
	}
}
