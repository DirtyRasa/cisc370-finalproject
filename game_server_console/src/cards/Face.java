package cards;
@SuppressWarnings("serial")
public class Face extends Card
{
	/*
		Special kind of card

		Instance Variables
			private String suit			Holds suit of the card
		Constructor
			calls the Card constructor
		Methods
			int[] getValue			returns 10 for all Face cards
		Modification History
	*/
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