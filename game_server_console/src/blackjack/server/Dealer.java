package blackjack.server;
import java.io.*;
import cards.*;

public class Dealer extends BlackjackPlayer
{
	private Hand _hand;
	private Shoe _shoe;

	public Dealer(Shoe shoe)
	{
		super(null, null, null);
		if(shoe == null)
			throw new IllegalArgumentException("Dealer.constructor: Shoe object is null");
		else
			_shoe = shoe;
		
		setName("Dealer");
		_hand = new Hand();
		_shoe.shuffle();
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
}