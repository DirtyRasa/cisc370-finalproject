package cards;
public class Shoe
{
	/*
		An array of decks

		Instance Variables
			private Deck[] decks			Array of decks
			private int activeDecks			Decks with cards
		Constructor
			Creates a shoe with n number of decks
		Methods
			public Card deal()				Deals one card from the shoe to a player
			public void shuffle()			Takes a deck and calls deck.shuffle()
			public String toString()		Prints out all the cards
		Modification History
	*/
	private Deck[] shoe;
	private int activeDecks;

	public Shoe(int numberOfDecks)
	{
		if(numberOfDecks >= 2 && numberOfDecks <= 8)
		{
			this.shoe = new Deck[numberOfDecks];
			for(int i=0; i<numberOfDecks; i++)
			{
				this.shoe[i] = new Deck();
				this.shoe[i].shuffle();
			}
			activeDecks = this.shoe.length;
		}
		else
			throw new IllegalArgumentException("Shoe.constructor: Invalid amount of decks for the shoe");
		//create 2-8 decks
	}

	public Card deal()
	{
		int randDeck;
		Card cardDealt = null;

		randDeck = (int)(Math.random()*(this.shoe.length));

		if(cardDealt == null)
		{
			for(int i = randDeck; cardDealt == null; i=(i+1)%this.activeDecks)
			{
				if(!this.shoe[i].allCardsDealt())
					cardDealt = this.shoe[i].deal();
			}
		}

		return cardDealt;
	}

	public void shuffle()
	{
		for(int i=0; i<this.shoe.length; i++)
		{
			this.shoe[i].shuffle();
		}
	}

	public String toString()
	{
		String result = "";
		for(int i=0; i<this.shoe.length; i++)
		{
			result = result + this.shoe[i].toString();
		}
		return result;
	}
}