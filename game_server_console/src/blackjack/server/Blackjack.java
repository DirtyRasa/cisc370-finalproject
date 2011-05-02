package blackjack.server;

import cards.*;
import game.server.GameServer;
import game.server.User;
import java.io.IOException;
import communication.Communication;
import communication.Response;
import communication.ResponseException;

public class Blackjack {
	private GameServer _gs;
	
	private RemotePlayer[] _players;
	private Dealer _dealer;
	private Shoe _shoe;
	private boolean[] _activePlayer;
	private int[] _results;
	
	public Blackjack(GameServer gs, int shoeSize, int maxPlayers) throws IOException, ClassNotFoundException
	{
		_gs = gs;
		
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
							Communication.sendMessage(this._players[j].getOutput(),"\nWaiting for other players to make a decision...");
						}
					}

					done = false;

					while(!done)
					{
						Communication.sendQuestion(this._players[i].getOutput(),"\nWould you like to play this round of Blackjack (y/n)?");
						
						try{
							if(Response.binaryEval(this._players[i].getInput().readLine()))
							{
								this._activePlayer[i] = true;
								this._players[i].resetHand();
								this._results = new int[6];
								done = true;
							}
							else
							{
								_gs.returnToGameSelectionThread(new User(this._players[i].getName(), this._players[i].getSocket(), this._players[i].getOutput(), this._players[i].getInput()));
								//Communication.sendMessage(this._players[i].getOutput(),"end");
								//this._players[i].getSocket().close();
								this._players[i] = null;
								this._activePlayer[i] = false;
								done = true;
							}
						}catch (ResponseException ex){
							Communication.sendMessage(this._players[i].getOutput(), ex.getMessage());
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
							Communication.sendMessage(this._players[i].getOutput(),"\n\n\n\n\n\n\n\n\n\n\n\n");
							Communication.sendMessage(this._players[i].getOutput(),"_____________________________________________");
							Communication.sendMessage(this._players[i].getOutput(),"\tDealer\t\t" + dealerCards[0] + "\n\t\t\t*******");

							//Prints players names and cards
							for(int j=0; j<this._players.length; j++)
							{
								if(this._players[j] != null && this._activePlayer[j])
								{
									Communication.sendMessage(this._players[i].getOutput(),"\n\t" + this._players[j]);
								}
							}
							Communication.sendMessage(this._players[i].getOutput(),"_____________________________________________");
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
								Communication.sendMessage(this._players[j].getOutput(),"\nWaiting for other players to make a decision...");
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
								Communication.sendMessage(this._players[i].getOutput(),"\n\t   ****"+
																						"\tYou busted"+
																						"\t   ****\n\n");
								flag = false;
							}
							if(this._activePlayer[i] && this._players[i].is21())
							{
								Communication.sendMessage(this._players[i].getOutput(),"\n\t   ****"+
																						"\tYou have 21"+
																						"\t   ****\n\n");
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
						Communication.sendMessage(this._players[i].getOutput(),printResults()+"\n\n");
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
