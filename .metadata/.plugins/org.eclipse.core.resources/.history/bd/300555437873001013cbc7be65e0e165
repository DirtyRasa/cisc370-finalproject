package cards;
public class Deck
{
	/*
		This class creates a deck of 52 cards

		Instance Variables
			private Card2[] deck		Creates a deck using class Card2
			private int cardsDealt		Keeps track of the cards dealt

		Constructor
			Creates the deck consisting of all 4 suits and all 52 cards

		Methods
			public void shuffle()				Shuffles all the cards in the deck
			public Card deal()					Deals the top card
			public boolean allCardsDealt()		Sees if there are any cards left in the deck
			public String toString()			Prints out all 52 cards

	*/
	protected Card[] deck;
	protected int cardsDealt;

	public Deck()
	{
		cardsDealt = 0;
		int counter = 0;
		String[] suits = {"Clubs", "Diamonds", "Spades", "Hearts"};

		this.deck = new Card[52];

		for(int i=0; i<suits.length; i++)
		{
			for(int j=1; j<14; j++)
			{
				if(j == 1)
					this.deck[counter] = new Ace(suits[i]);
				if(j>10)
					this.deck[counter] = new Face(j, suits[i]);
				if(j > 1 && j<11)
					this.deck[counter] = new Card(j, suits[i]);
				counter = counter + 1;
			}//for(int j=1; j<14; j++)
		}//for(int i=0; i<suits.length; i++)

	}

	public void shuffle()
	{
		Card temp;
		int spot1;
		int spot2;

		cardsDealt = 0;

		for(int i=0; i<1000; i++)
		{
			spot1 = (int)(Math.random()*(this.deck.length-1));
			spot2 = (int)(Math.random()*(this.deck.length-1));
			temp = this.deck[spot1];
			this.deck[spot1] = this.deck[spot2];
			this.deck[spot2] = temp;
		}
	}

	public Card[] deal(int howManyCards)
	{
		Card[] hand = new Card[howManyCards];

		if(this.deck.length - this.cardsDealt < howManyCards)
			throw new RuntimeException("Deck does not have enough cards");

		for(int i=0; i<hand.length; i++)
		{
			hand[i] = this.deck[this.cardsDealt];
			this.cardsDealt = this.cardsDealt + 1;
		}
		return hand;
	}

	public Card deal()
	{
		//deals 1 card
		Card card;
		if(cardsDealt < 52)
		{
			card = this.deck[cardsDealt];
			this.cardsDealt = this.cardsDealt + 1;
		}
		else
			throw new DeckEmptyException("Out of cards");
		return card;
	}

  	public boolean allCardsDealt()
  	{
		return this.cardsDealt >= 52;
	}

	public String toString()
	{
		String result = "";
		for(int i =0; i<this.deck.length; i++)
		{
			result = result + this.deck[i].toString()+"\n";
		}
		return result;
	}
}