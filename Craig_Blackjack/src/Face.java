public class Face extends Card
{
	/*
		Craig Martin
		March 10, 2008

		Special kind of card

		Instance Variables
			private String suit			Holds suit of the card
		Constructor
			calls the Card constructor
		Methods
			int[] getValue			returns 10 for all Face cards
		Modification History
	*/
	private String suit;

	public Face(int rank, String suit)
	{
		super(rank, suit);
	}

	public int[] getValue()
	{
		int[] value = {10, 10};
		return value;
	}
}