package cards;

@SuppressWarnings("serial")
public class Ace extends Card{
	/*
	Special kind of card

	Instance Variables
		private String suit			Holds the suit for the card
	Constructor
		Initializes the instance variables
	Methods
		int[] getValue				returns 1 and 11
	Modification History
*/
public Ace(String suit)
{
	super(1, suit);
}

public int[] getValue()
{
	int[] value = {1, 11};
	return value;
}
}
