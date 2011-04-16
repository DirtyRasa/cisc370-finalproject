import java.net.*;
import java.io.*;

public class Card implements Serializable
{
	/*
		Craig Martin
		March 25, 2008

		Class will simulate one playing card

		Instance Variables
			int rank			Holds a value from 1-13
			String suit			Holds the suit of the card
			boolean flag		boolean for testing

		Constructor
			Creates a card with a suit and a rank. Checks if rank and suit are valid.

		Methods
			public int getRank()			returns the rank of the card
			public String getSuit()			returns the suit of the card

	*/
	private int rank;
	private String suit;
	private boolean flag;

	public Card(int rank, String suit)
	{
		this.rank = rank;
		this.suit = suit;
		if(this.rank > 13 || this.rank < 1)
			throw new IllegalArgumentException("PlayingCard; Invalid rank");
		//if(!isHeart() && !isDiamond() && !isSpade() && !isClub())
		//	throw new IllegalArgumentException("PlayingCard; Invalid suit");
	}

	public int getRank()
	{
		return this.rank;
	}

	public int[] getValue()
	{
		int[] value = {this.getRank(), this.getRank()};

		return value;
	}

	public String getSuit() {return this.suit;}
	public String getName()
	{
		String[] name = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
		return name[this.rank-1];
	}

	public String toString()
	{
		return getName() + " of " + getSuit();
	}
}