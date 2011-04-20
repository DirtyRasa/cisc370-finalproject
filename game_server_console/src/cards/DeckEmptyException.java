package cards;
@SuppressWarnings("serial")
public class DeckEmptyException extends RuntimeException
{
	public DeckEmptyException(){super();}
	public DeckEmptyException(String message){super(message);}
}