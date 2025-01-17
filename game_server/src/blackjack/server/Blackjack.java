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

public class Blackjack extends Thread
{
	private GameServer _gs;
	
	private List<BlackjackPlayer> _players = new ArrayList<BlackjackPlayer>();
	private List<BlackjackPlayer> _toRemove = new ArrayList<BlackjackPlayer>();
	private List<BlackjackPlayer> _toAdd = new ArrayList<BlackjackPlayer>();
	
	private Dealer _dealer;

	private Shoe _shoe;
	private double _bet;
	private int _betsTaken;
	private int _tableNumber;
	private static final int maxPlayers = 6;
	
	public Blackjack(GameServer gs, int shoeSize, int tableNumber) throws IOException, ClassNotFoundException
	{
		_gs = gs;
		_tableNumber = tableNumber;
		if(shoeSize >= 2 && shoeSize <= 8)
			_shoe = new Shoe(shoeSize);
		else
			throw new IllegalArgumentException("Blackjack.constructor: Invalid shoe size");

		_dealer = new Dealer(_shoe);
	}
	
	@SuppressWarnings("static-access")
	public void playGame() throws IOException, ClassNotFoundException
	{
		boolean flag;
		boolean allBusted;
		boolean allBlackjack;
		boolean atLeastOneActive;
		Hand dealerHand;
		Card[] dealerCards;
		allBusted = true;
		allBlackjack = true;
		atLeastOneActive = false;
		
		addToPlayers();
		
		if(_players.size() > 0)
			atLeastOneActive = true;
		else{
			try {
				this.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		while(atLeastOneActive)
		{
			atLeastOneActive = false;
			flag = true;
			
			addToPlayers();
			for(BlackjackPlayer player : _players)
				player.resetHand();
				
			System.out.println("\nActive players on table '"+ _tableNumber +"' this round: ");
			for(BlackjackPlayer player : _players){
				System.out.println(player.getName());
				atLeastOneActive = true;
			}
			
			if(atLeastOneActive)
			{
				this._dealer.resetHand();
				takeBets();

				removePlayers();
				
				for(BlackjackPlayer player : _players){
					Communication.sendHands(player, "CLEAR");
					player.setbet21(false);
					player.setPlayerHit(false);
				}
				
				dealFirstRound();

				dealerHand = this._dealer.getHand();
				dealerCards = dealerHand.getCards();

				if(!this._dealer.is21()){	
					updateTableToAllUsers("dealer=0="+dealerCards[0] + "<>back/");
					allBusted = true;	
					allBlackjack = true;
					String tempResults = "";
					for(BlackjackPlayer player : _players){						
						for(BlackjackPlayer player2 : _players)
							if(!player.equals(player2))
								//Prints message to other clients that are not currently playing
								Communication.sendWait(player2,"Waiting for other players to make a decision...");
						
						while(flag){
							try {
								flag = player.hitMe();
							} catch (InputException e) {
								flag = false;
								_toRemove.add(player);
								if(e.getMessage().equals("quit"))
									_gs.returnToGameSelectionThread(player);
								else
									_gs.logout(player);
							}

							if(flag){
								this._dealer.hitPlayer(player);
								player.setPlayerHit(true);
								updateTableToAllUsers("dealer=0="+dealerCards[0] + "<>back/");
							}
								
							if(player.isBusted())
							{
								tempResults += "Busted<>";
								allBlackjack = false;
								flag = false;
							}
							else if(player.is21()&& !player.getPlayerHit()) //Blackjack
							{
								tempResults += "BJ! +$"+(1.5*player.getBet())+"0<>";
								flag = false;
								allBusted = false;
								player.setbet21(true);
							}
							else if(player.is21()){
								tempResults += " <>";
								flag = false;
								allBusted = false;
								allBlackjack = false;
							}
							else if(!flag){
								tempResults += " <>";
								allBusted = false;
								allBlackjack = false;
							}
						}
						flag = true;
						for(BlackjackPlayer player2 : _players) //Update all players of Bust/BJ
							Communication.sendResults(player2, tempResults);
					}
					
					removePlayers();
					updateTableToAllUsers(this._dealer.toString());
					
					if(!allBusted && !allBlackjack){
						for(BlackjackPlayer player : _players)
							Communication.sendDealer(player);
						while(this._dealer.hitMe()){
							this._dealer.dealSelf();
							updateTableToAllUsers(this._dealer.toString());
						}
					}
				}
				else
					updateTableToAllUsers("dealer=Blackjack="+dealerCards[0] + "<>" + dealerCards[1]+"/");
				
				//TODO Update stats after check.
				for(BlackjackPlayer player : _players){
					if(player.getbet21())
						player.setResult(1);
					else
						player.setResult(this._dealer.winLoseOrPush(player));
				}
				
				updateMoneyStats();
				
				for(BlackjackPlayer player : _players)
					Communication.sendResults(player,printResults());
			}
		}
	}

	public void run()
	{
		try {
			while(true)
			{
				playGame();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String printResults()
	{
		String result = "";
		for(BlackjackPlayer player : _players){
			if(player.getResult() == 0){
				result = result + "Push<>";
			}
				
			else if(player.getResult() > 0){
				if(player.getbet21())
					result = result + "BJ! +$"+(1.5*player.getBet())+"0<>";
				else{
					if(player.getDoubleDown())
						result = result + "Won +$"+player.getBet()*2+"0<>";
					else
						result = result + "Won +$"+player.getBet()+"0<>";
				}
			}
				
			else if(player.getResult() < 0){
				if(player.getDoubleDown())
					result = result + "Lost -$"+player.getBet()*2+"0<>";
				else
					result = result + "Lost -$"+player.getBet()+"0<>";
			}
		}
		return result;
	}
	
	public synchronized void addToPlayers(){
		_players.addAll(_toAdd);
		for(BlackjackPlayer player : _players){
			for(BlackjackPlayer chat : _toAdd){
				if(player.equals(chat))
					Communication.sendChat(player, "You joined the table.");
				else
					Communication.sendChat(player, chat.getName() + " has joined the table.");
			}
		}
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
		if(this._dealer.shuffleCheck(_players.size()))// shuffles the shoe if there are not so many as 10 cards per player
			for(BlackjackPlayer player : _players)
				Communication.sendPop(player, "Shuffling...");
			
		for(int j=0; j<2; j++)
		{
			for(BlackjackPlayer player : _players)
				this._dealer.deal(player);

			this._dealer.dealSelf();
		}
	}

	public void removePlayers(){
		_players.removeAll(_toRemove);
		for(BlackjackPlayer players : _players)
			for(BlackjackPlayer chat : _toRemove)
				Communication.sendChat(players, chat.getName() + " has left the table.");
		_toRemove.clear();
	}
	
	public void updateTableToAllUsers(String dealer){ //TODO
		for(BlackjackPlayer player : _players){
			String hands = "";
			hands += dealer;
			
			//Prints ALL players' names and cards
			for(BlackjackPlayer player2 : _players){
				hands += player2.toSpecialString();
			}
			Communication.sendHands(player, hands);
		}
	}
	
	public void updateMoneyStats(){
		for(BlackjackPlayer player : _players){
			if(player.getResult() == 0){
				_gs.updatePushes(player);
				_gs.updateMoney(player,0);
			}
			else if(player.getResult() > 0){
				_gs.updateWins(player);
				if(player.getbet21() == true)
					_gs.updateMoney(player,(1.5*player.getBet()));
				else if(player.getDoubleDown())
					_gs.updateMoney(player,(2*player.getBet()));
				else
					_gs.updateMoney(player,(player.getBet()));
			}
				
			else if(player.getResult() < 0){
				_gs.updateLosses(player);
				if(player.getDoubleDown())
					_gs.updateMoney(player,(-2*player.getBet()));
				else
					_gs.updateMoney(player,(-1*player.getBet()));
			}
			_gs.updateTotal(player);
			Communication.sendBank(player, player.getMoney() +"");
			Communication.sendStats(player, player.getStats() +"");
		}
	}
	
	@SuppressWarnings("static-access")
	public void takeBets()
	{
		_betsTaken = 0;
		
		for(BlackjackPlayer playerToCheck : _players)
		{
			BetTaker clientChecker = new BetTaker(playerToCheck);
			clientChecker.start();
		}
		
		while(getBetsTaken() < _players.size())
		{
			//for some reason this only works when this loop is printing something
			System.out.print("");
			try {
				this.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void startChat(BlackjackPlayer user){
		BlackjackChat chat = new BlackjackChat(user);
		chat.start();
	}
	
	public synchronized void hasBet()
	{
		_betsTaken++;
	}
	
	public synchronized int getBetsTaken()	{return _betsTaken;}
	
	public class BetTaker extends Thread
	{
		BlackjackPlayer client;
		
		public BetTaker(BlackjackPlayer client)
		{
			this.client = client;
		}
		
		public void run() 
		{
			boolean doneBet = false;
			
			Communication.sendBank(this.client, this.client.getMoney() + "");
			
			while(!doneBet)
			{
				try {
					Communication.sendBet(this.client,"What would you like to wager?");
					String hold= this.client.getInputWithTimeout(20);
					if(!hold.startsWith("CHAT")){
						if(!hold.equals("quit")){
							_bet = Response.bet(hold);
		
							if(_bet > this.client.getMoney() || _bet < 0){
								Communication.sendError(this.client,"You do not have that much to wager");
							}
							else{
								this.client.setBet(_bet);
								doneBet = true;
							}
						}
						else{
							doneBet = true;
							_toRemove.add(this.client);
							_gs.returnToGameSelectionThread(this.client);
						}
					}
				}catch (ResponseException e) {
					Communication.sendMessage(this.client, e.getMessage());
				} catch (InputException e) {
					Communication.sendPop(this.client, "You were kicked for inactivity");
					_toRemove.add(this.client);
					_gs.logout(this.client);
				}
			}
			hasBet();
		}//run
	}//Subclass ClientChecker
	
	public class BlackjackChat extends Thread
	{
		BlackjackPlayer client;
		
		public BlackjackChat(BlackjackPlayer client)
		{
			this.client = client;
		}
		
		public void run() 
		{
			boolean done = false;
			do{
				String msg = "";
				try {
					msg = this.client.getUserInput();
					if(msg.startsWith("CHAT"))
						sendToAll(msg.substring(4));
					else if(msg.equals("*L0gM30ut*") || msg.equals("quit"))
						done = true;
				} catch (InputException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(!done);
		}
		
		public void sendToAll(String msg){
			for(BlackjackPlayer user : _players){
				if(this.client.equals(user))
					Communication.sendMessage(user, "You: " + msg);
				else
					Communication.sendMessage(user, user.getName() + ": " + msg);
			}
		}
	}
}
