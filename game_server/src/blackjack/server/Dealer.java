package blackjack.server;
import java.io.*;
import cards.*;

public class Dealer extends BlackjackPlayer
{
	private Shoe _shoe;

	public Dealer(Shoe shoe)
	{
		super(null, null, null);
		if(shoe == null)
			throw new IllegalArgumentException("Dealer.constructor: Shoe object is null");
		else
			_shoe = shoe;
		
		setName("Dealer");
		_shoe.shuffle();
	}
	
	public void deal(BlackjackPlayer player){
		Card card = _shoe.deal();
		player.addCard(card);
	}
	
	public void dealSelf(){
		Card card = _shoe.deal();
		this.addCard(card);
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
		score = getHand().getValues();

		if(score[0] < 17 || this.getHand().getCards().length != 5)
			flag = true;
		return flag;
	}

	public int winLoseOrPush(BlackjackPlayer player)
	{
		// 0 = push
		// 1 =  win
		//-1 = lose
		int flag = 0;
		int[] dealerScore;
		int[] playerScore;
		int dScore = 22;
		int pScore = 22;
		dealerScore = getHand().getValues();
		playerScore = player.getHand().getValues();

		for(int i=0; i < 2; i++){
			if(dealerScore[i] < 22)
				dScore = dealerScore[i];
			if(playerScore[i] < 22)
				pScore = playerScore[i];
		}

		
		if(pScore > 21)
			flag = -1;
		else if(player.getHand().getCards().length == 5)
			flag = 1;
		else if(this.getHand().getCards().length == 5)
			flag = -1;
		else if(dScore > 21 && pScore < 22)
			flag = 1;
		else if(pScore == dScore)
			flag = 0;
		else if(pScore > dScore)
			flag = 1;
		else
			flag = -1;

		return flag;
	}
	
	public boolean shuffleCheck(int playerQuantity) 
	{
		if((playerQuantity + 1) * 5 >= _shoe.remainingCards())
		{
			_shoe.shuffle();
			return true;
		}
		return false;
	}

	public void shuffle(){this._shoe.shuffle();}
	
	public String toSpecialString(){
		return getBet() + "=" + toString();
	}
}