package blackjack.server;
import java.io.*;
import cards.*;

public class Dealer
{
	/*
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
	private Hand _hand;
	private Shoe _shoe;

	public Dealer(Shoe shoe)
	{
		//super("Dealer");
		//this.hand = new Hand();
		if(shoe == null)
			throw new IllegalArgumentException("Dealer.constructor: Shoe object is null");
		else
			_shoe = shoe;
		
		_hand = new Hand();
		_shoe.shuffle();
	}

	public Hand getHand() { return _hand; }
	
	public void resetHand(){
		_hand.resetHand();
	}
	
	public void addCard(Card card){
		_hand.addCard(card);
	}
	
	public void deal(BlackjackPlayer player){
		Card card = _shoe.deal();
		player.addCard(card);
	}
	
	public void dealSelf(){
		Card card = _shoe.deal();
		addCard(card);
	}

	public void hitPlayer(BlackjackPlayer player) throws IOException
	{
		this.deal(player);
	}

	public boolean hitMe()
	{
		boolean flag;
		int[] score;

		flag = false;
		score = _hand.getValues();

		if(score[1] < 17)
			flag = true;
		return flag;
	}

	public int winLoseOrPush(BlackjackPlayer player)
	{
		//0 = push
		//0 < win
		//0 > lose
		Hand playerHand;
		int flag = 0;
		int[] dealerScore;
		int[] playerScore;
		int dScore = 0;
		int pScore = 22;

		playerHand = player.getHand();
		dealerScore = _hand.getValues();
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

	public void shuffle(){this._shoe.shuffle();}
	
	public boolean is21()
	{
		boolean flag;
		int[] score = _hand.getValues();
		flag = false;
		for(int i=0; i<score.length; i++)
			if(score[i] == 21)
				flag = true;

		return flag;
	}

	public String toString()
	{
		String result = "";
		result = "Dealer " + _hand;
		return result;
	}
}