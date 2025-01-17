package blackjack.server;

import game.server.GameServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cards.Card;
import cards.Shoe;

import communication.Communication;
import communication.Response;
import communication.ResponseException;

public class Blackjack {
	private GameServer _gs;
	
	private List<BlackjackPlayer> _players = new ArrayList<BlackjackPlayer>();
	private List<BlackjackPlayer> _toRemove = new ArrayList<BlackjackPlayer>();
	private Dealer _dealer;
	private Shoe _shoe;
	
	private static final int maxPlayers = 6;
	
	public Blackjack(GameServer gs, int shoeSize) throws IOException, ClassNotFoundException
	{
		_gs = gs;
		
		if(shoeSize >= 2 && shoeSize <= 8)
			_shoe = new Shoe(shoeSize);
		else
			throw new IllegalArgumentException("Blackjack.constructor: Invalid shoe size");

		_dealer = new Dealer(_shoe);
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
			
		if(_players.size() > 0)
			atLeastOneActive = true;
		
		while(atLeastOneActive)
		{
			atLeastOneActive = false;
			flag = true;
			//For loop to check if players wanted to play current round of BJ was here
			int i = 0;
			
			for(BlackjackPlayer player : _players){
				player.setIsActive(false);
				
				for(BlackjackPlayer player2 : _players)
					if(!player2.equals(player))
						//Prints message to other clients that are not currently playing
						Communication.sendMessage(player2,"\nWaiting for other players to make a decision...");

				done = false;

				while(!done)
				{
					Communication.sendQuestion(player,"\nWould you like to play this round of Blackjack (y/n)?");
					
					try{
						/*if(Response.binaryEval(player.getInput().readLine()))
						{
							player.setIsActive(true);
							player.resetHand();
							done = true;
						}
						else
						{
							Communication.sendQuestion(player,"\nWould you like to go back to the game selection? (y/n)?");
							try{
								if(Response.binaryEval(player.getInput().readLine()))
								{
									_toRemove.add(player);
									_gs.returnToGameSelectionThread(player);
								}
								else
								{
									player.setIsActive(false);
								}
							}catch (ResponseException ex){
								Communication.sendMessage(player, ex.getMessage());
								done = false;
							}
							done = true;
						}*/
						
						switch(Response.trinaryEval(player.getInputWithTimeout(30)))
						{
						case -1://no
							Communication.sendQuestion(player,"\nWould you like to go back to the game selection? (y/n)?");
							switch(Response.trinaryEval(player.getInputWithTimeout(30)))
							{
							case -1://no
								player.setIsActive(false);
								break;
							case 0://quit
								_gs.logout(player);
								break;
							case 1://yes
								_toRemove.add(player);
								_gs.returnToGameSelectionThread(player);
								break;
							}
							break;
						case 0://quit
							_gs.logout(player);
							break;
						case 1://yes
							player.setIsActive(true);
							player.resetHand();
							done = true;
							break;
						}
					}catch (ResponseException ex){
						Communication.sendMessage(player, ex.getMessage());
						done = false;
					}
				}
				i++;
			}			
			
			System.out.println("\nActive players this round: ");
			
			_players.removeAll(_toRemove);
			_toRemove.clear();
			
			for(BlackjackPlayer player : _players){
				if(player.isActive())
				{
					System.out.println(player.getName());
					atLeastOneActive = true;
				}
			}
			
			if(atLeastOneActive)
			{
				this._dealer.resetHand();

				dealFirstRound();

				dealerHand = this._dealer.getHand();
				dealerCards = dealerHand.getCards();

				if(!this._dealer.is21()){					
					//for(BlackjackPlayer player : _players){
					//	if(player.isActive() && player.is21())
					//		player.setResult(1);
					//}
					
					for(BlackjackPlayer player : _players){
						if(player.isActive())
						{
							//Prints dealers first card only and hides the second card
							Communication.sendMessage(player,"\n\n\n\n\n\n\n\n\n\n\n\n");
							Communication.sendMessage(player,"_____________________________________________");
							Communication.sendMessage(player,"\tDealer\t\t" + dealerCards[0] + "\n\t\t\t*******");

							//Prints players names and cards
							for(BlackjackPlayer player2 : _players)
								if(player2.isActive())
									Communication.sendMessage(player2,"\n\t" + player2.toString());
							
							Communication.sendMessage(player,"_____________________________________________");
						}
					}
					
					for(BlackjackPlayer player : _players){
						if(!player.isActive())
							flag = false;
						
						for(BlackjackPlayer player2 : _players)
							if(!player.equals(player2) && player.isActive() && player2.isActive())
								//Prints message to other clients that are not currently playing
								Communication.sendMessage(player2,"\nWaiting for other players to make a decision...");

						allBusted = true;
						while(flag)
						{
							if(player.isActive()){
								flag = player.hitMe();

								if(flag)
									this._dealer.hitPlayer(player);
								if(player.isBusted())
								{
									Communication.sendMessage(player,"\n\t   ****"+
																	 "\tYou busted"+
																	 "\t   ****\n\n");
									flag = false;
								}
								else if(player.is21())
								{
									Communication.sendMessage(player,"\n\t   ****"+
																 	 "\tYou have 21"+
																	 "\t   ****\n\n");
									flag = false;
									allBusted = false;
								}
								else
									allBusted = false;
							}
						}
						flag = true;
					}
					
					if(!allBusted)
						while(this._dealer.hitMe())
							this._dealer.dealSelf();					
				}
				
				//TODO Update stats after check.
				for(BlackjackPlayer player : _players)
					if(player.isActive())
						player.setResult(this._dealer.winLoseOrPush(player));
				
				for(BlackjackPlayer player : _players)
					if(player.isActive())
						Communication.sendMessage(player,printResults()+"\n\n");
			}
		}
	}

	public String printResults()
	{
		String result = "";


		result = result + "\n____________ RESULTS ____________";
		result = result + "\n\n\t" + this._dealer.toString() + "\n\n";
		for(BlackjackPlayer player : _players){
			if(player.isActive())
			{
				result = result + "\t----------\n\n";
				result = result + "\t" + player.toString() + "\n";
				if(player.getResult() == 0){
					result = result + "\n\t***Push***\n\n";
					_gs.updatePushes(player);
				}
					
				if(player.getResult() > 0){
					result = result + "\n\t***Win***\n\n";
					_gs.updateWins(player);
				}
					
				if(player.getResult() < 0){
					result = result + "\n\t***Lose***\n\n";
					_gs.updateLosses(player);
				}
				_gs.updateTotal(player);
					
			}
		}
		result = result + "_________________________________";
		return result;
	}
	
	public void addPlayer(BlackjackPlayer player){
		if(_players.size() <= maxPlayers)
			_players.add(player);
		else{
			Communication.sendMessage(player, "The current table is full.");
			_gs.returnToGameSelectionThread(player);
		}
	}

	public void dealFirstRound()
	{
		for(int j=0; j<2; j++)
		{
			for(BlackjackPlayer player : _players)
				if(player.isActive())
					this._dealer.deal(player);

			this._dealer.dealSelf();
		}
	}
}
