import java.io.*;
import java.net.*;

public class Player implements Serializable
{
	/*
		Craig Martin
		March 10, 2008

		Player that plays a game of Blackjack

		Instance Variables
			private Hand hand			holds the cards for the player
			private String name			holds the name of the player
		Constructor
			Creates a player hand
			Initializes the player name
		Methods
			public String getName()				returns the name of the player
			public Hand getHand()				returns the hand of the player
			public void addCard(Card card)		adds a card to the hand
			public boolean is21()				checks if the hand is equal to 21
			public boolean isBusted()			checks if the hand is > 21
			public boolean hitMe()				checks if the player wants and can be hit
			public String toString()			prints out the hand of the player
		Modification History
			April 25, 2008
				Added:
					public void resetHand()		resets the hand of the player for a new round of Blackjack

	*/
	private Hand hand;
	private String name;

	public Player(String name)
	{
		this.hand = new Hand();
		this.name = name;
	}

	public String getName(){return this.name;}

	public Hand getHand()
	{
		return this.hand;
	}

	public void addCard(Card card)
	{
		this.hand.addCard(card);
	}

	public void resetHand()
	{
		this.hand = new Hand();
	}

	public boolean is21()
	{
		boolean flag;
		int[] score = this.hand.getValues();
		flag = false;
		for(int i=0; i<score.length; i++)
			if(score[i] == 21)
				flag = true;

		return flag;
	}
	public boolean isBusted()
	{
		boolean flag;
		int[] score = this.hand.getValues();
		flag = true;
		for(int i=0; i<score.length; i++)
			if(score[i] < 22)
				flag = false;

		return flag;
	}

	public boolean hitMe() throws IOException, ClassNotFoundException
	{
		boolean flag;
		String hold;
		BufferedReader keyboard;

		keyboard = new BufferedReader(new InputStreamReader(System.in));
		flag = true;

		if(this.isBusted() || this.is21())
			flag = false;
		if(flag)
		{
			System.out.print("\n\nYou have: \t" + getHand());
			System.out.print("\nWould you like to hit? (y/n) ");
			hold = keyboard.readLine();

			if(hold.equalsIgnoreCase("yes") || hold.equalsIgnoreCase("y"))
				flag = true;
			if(hold.equalsIgnoreCase("no") || hold.equalsIgnoreCase("n"))
				flag = false;
		}

		return flag;
	}

	public String toString()
	{
		String result = "";
		result = this.name + " " + this.hand;
		return result;
	}
}