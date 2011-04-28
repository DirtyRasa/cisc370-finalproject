package blackjack.server;
import java.io.*;
import cards.*;

@SuppressWarnings("serial")
public class Dealer extends Player
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
	private Shoe _shoe;

	public Dealer(Shoe shoe)
	{
		super("Dealer");
		//this.hand = new Hand();
		if(shoe == null)
			throw new IllegalArgumentException("Dealer.constructor: Shoe object is null");
		else
			_shoe = shoe;
	}

	public void deal(Player player)
	{
		Card card;
		card = _shoe.deal();
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