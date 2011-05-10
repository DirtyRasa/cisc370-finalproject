package blackjack.server;

import game.server.GameServer;
import game.server.InputException;

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
	private List<BlackjackPlayer> _toAdd = new ArrayList<BlackjackPlayer>();
	
	private Dealer _dealer;
	private Shoe _shoe;
	private double _bet;
	private boolean _bet21;
	
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
		
		addToPlayers();
		
		if(_players.size() > 0)
			atLeastOneActive = true;
		
		while(atLeastOneActive)
		{
			atLeastOneActive = false;
			flag = true;
			//For loop to check if players wanted to play current round of BJ was here
			
			addToPlayers();
			
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
								_toRemove.add(player);
								_gs.logout(player);
								break;
							case 1://yes
								_toRemove.add(player);
								_gs.returnToGameSelectionThread(player);
								break;
							}
							done = true;
							break;
						case 0://quit
							_toRemove.add(player);
							_gs.logout(player);
							
							break;
						case 1://yes
							player.setIsActive(true);
							player.resetHand();
							break;
						}
						done = true;
					}catch (ResponseException ex){
						Communication.sendMessage(player, ex.getMessage());
						done = false;
					} catch (InputException e) {
						_toRemove.add(player);
						_gs.logout(player);
					}
				}
			}			
			
			System.out.println("\nActive players this round: ");
			removePlayers();
			
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
				
				for(BlackjackPlayer player : _players){
					if(player.isActive())
					{
						for(BlackjackPlayer player2 : _players)
							if(!player.equals(player2) && player2.isActive())
								//Prints message to other clients that are not currently playing
								Communication.sendMessage(player2,"\nWaiting for other players to make a decision...");
						
						boolean doneBet = false;
						while(!doneBet){
							try {
								Communication.sendQuestion(player,"\nYou have: $"+player.getMoney()+". Enter an integer value to wager?(min. 0) ");
								String hold= player.getInputWithTimeout(30);	
								if(!hold.equals("quit")){
									_bet = Response.bet(hold);
	
									while(_bet > player.getMoney() || _bet < 0)
									{
										Communication.sendQuestion(player,"\nYou do not have that much to wager or less then minimum, enter new integer value.(min. 0) ");
										_bet = Response.bet(player.getInputWithTimeout(30));
									}
									player.setBet(_bet);
									
								}
								else{
									_toRemove.add(player);
									_gs.returnToGameSelectionThread(player);
								}
								doneBet = true;
							}catch (ResponseException e) {
								Communication.sendMessage(player, e.getMessage());
							} catch (InputException e) {
								_toRemove.add(player);
								_gs.logout(player);
							}
						}
					}
				}

				removePlayers();
				
				for(BlackjackPlayer player : _players){
					if(player.isActive())
					{
						_bet21 = false;
					}
				}
				dealFirstRound();

				dealerHand = this._dealer.getHand();
				dealerCards = dealerHand.getCards();

				if(!this._dealer.is21()){					
					for(BlackjackPlayer player : _players){
						if(player.isActive())
						{
							//Prints dealers first card only and hides the second card
							Communication.sendMessage(player,"\r\n\r\n\r\n");
							Communication.sendMessage(player,"_____________________________________________");
							Communication.sendMessage(player,"\tDealer\t\t" + dealerCards[0] + "\r\n\t\t\t*******");
							
							//Prints ALL players' names and cards
							for(BlackjackPlayer player2 : _players)
								if(player2.isActive())
									Communication.sendMessage(player,"\r\n\t" + player2.toString());
							
							Communication.sendMessage(player,"_____________________________________________");
						}
					}
					
					for(BlackjackPlayer player : _players){
						if(!player.isActive())
							flag = false;
						
						for(BlackjackPlayer player2 : _players)
							if(!player.equals(player2) && player.isActive() && player2.isActive())
								//Prints message to other clients that are not currently playing
								Communication.sendMessage(player2,"\r\nWaiting for other players to make a decision...");

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
									_bet21 = true;
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
				
				updateMoneyStats();
				
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
				}
					
				else if(player.getResult() > 0){
					if(_bet21 == true)
						result = result + "\n\t***Won $"+(1.5*player.getBet())+"***\n\n";
					else
						result = result + "\n\t***Won $"+player.getBet()+"***\n\n";
				}
					
				else if(player.getResult() < 0){
					result = result + "\n\t***Lost $"+player.getBet()+"***\n\n";
				}
					
			}
		}
		result = result + "_________________________________";
		return result;
	}
	
	public void addToPlayers(){
		_players.addAll(_toAdd);
		_toAdd.clear();
	}
	
	public synchronized void addPlayer(BlackjackPlayer player){
		if(_players.size() <= maxPlayers)
			_toAdd.add(player);
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

	public void removePlayers(){
		_players.removeAll(_toRemove);
		_toRemove.clear();
	}
	
	public void updateMoneyStats(){
		for(BlackjackPlayer player : _players){
			if(player.isActive())
			{
				if(player.getResult() == 0){
					_gs.updatePushes(player);
					_gs.updateMoney(player,0);
				}
				else if(player.getResult() > 0){
					_gs.updateWins(player);
					if(_bet21 == true)
						_gs.updateMoney(player,(1.5*player.getBet()));
					else
						_gs.updateMoney(player,(player.getBet()));
				}
					
				else if(player.getResult() < 0){
					_gs.updateLosses(player);
					_gs.updateMoney(player,(-1*player.getBet()));
				}
				_gs.updateTotal(player);
			}
		}
	}
}
