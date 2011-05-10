package blackjack.server;
@SuppressWarnings("serial")
public class TableFullException extends RuntimeException
{
	public TableFullException() {super();}
	public TableFullException(String message){super(message);}
}