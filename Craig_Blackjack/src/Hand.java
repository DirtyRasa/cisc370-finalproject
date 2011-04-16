import java.net.*;
import java.io.*;

public class Hand implements Serializable
{
	/*
		Craig Martin
		March 10, 2008

		Holds the cards for each player

		Instance Variables
			private Card[] cards			Card array
		Constructor
			Initializes card array
		Methods
			addCard(Card)					adds a card to the hand
			int[] getValues()				gets the values of the hand
			public Card[] getCards()		returns the card array
		Modification History
		April 25, 2008
			Added:
				public void resetHand()		resets the hand for a new round of Blackjack
	*/
	private Card[] cards;

	public Hand()
	{
		this.cards = new Card[0];
		//create cards
	}

	public void addCard(Card card)
	{
		Card[] temp;
		temp = new Card[this.cards.length+1];
		for(int i=0; i<this.cards.length; i++)
			temp[i] = this.cards[i];
		temp[this.cards.length] = card;
		this.cards = temp;
	}

	public void resetHand()
	{
		this.cards = new Card[0];
	}

	public int[] getValues()
	{
		//score[0] = min value; score[1] = max value
		int[] score = new int[2];
		int[] temp;
		int aceCounter = 0;

		for(int i=0; i<this.cards.length; i++)
		{
			temp = this.cards[i].getValue();
			if(temp[1] == 11)
				aceCounter = aceCounter + 1;
		}

		for(int i=0; i<this.cards.length; i++)
		{
			temp = this.cards[i].getValue();
			score[0] = score[0] + temp[0];
			if(aceCounter > 1 && temp[1] == 11)
			{
				score[1] = score[1] + temp[0];
				aceCounter = aceCounter - 1;
			}
			else
				score[1] = score[1] + temp[1];
		}
		return score;
	}

	public Card[] getCards()
	{
		return this.cards;
	}

	public String toString()
	{
		String result= "\t\t";
		int[] values;
		values = this.getValues();
		for(int i=0; i<values.length; i++)
		{
			if(values[0] == values[values.length-1])
			{
				result = result + values[0];
				i=values.length;
			}
			else
			{
				result = result + values[i];
				if(i<values.length-1)
					result = result + " or ";
			}
		}
		result = result + "\n\t\t\t";
		for(int i=0; i<this.cards.length;i++)
		{
			if(this.cards[i] != null)
				result = result + this.cards[i];
			if(i<this.cards.length-1)
				result = result + "\n\t\t\t";
		}
		return result;
	}
}