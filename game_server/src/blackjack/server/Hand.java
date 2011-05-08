package blackjack.server;

import java.io.*;
import cards.*;

@SuppressWarnings("serial")
public class Hand implements Serializable
{
	private Card[] _cards;

	public Hand()
	{
		_cards = new Card[0];
		//create cards
	}

	public void addCard(Card card)
	{
		Card[] temp;
		temp = new Card[_cards.length+1];
		for(int i=0; i<_cards.length; i++)
			temp[i] = _cards[i];
		temp[_cards.length] = card;
		_cards = temp;
	}

	public void resetHand()
	{
		_cards = new Card[0];
	}

	public int[] getValues()
	{
		//score[0] = min value; score[1] = max value
		int[] score = new int[2];
		int[] temp;
		int aceCounter = 0;

		for(int i=0; i<_cards.length; i++)
		{
			temp = _cards[i].getValue();
			if(temp[1] == 11)
				aceCounter = aceCounter + 1;
		}

		for(int i=0; i<_cards.length; i++)
		{
			temp = _cards[i].getValue();
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
		return _cards;
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
		for(int i=0; i<_cards.length;i++)
		{
			if(_cards[i] != null)
				result = result + _cards[i];
			if(i<_cards.length-1)
				result = result + "\n\t\t\t";
		}
		return result;
	}
}