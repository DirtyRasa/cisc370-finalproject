package cards;
@SuppressWarnings("serial")
public class ShoeEmptyException extends RuntimeException
{
	public ShoeEmptyException() {super();}
	public ShoeEmptyException(String message){super(message);}
}