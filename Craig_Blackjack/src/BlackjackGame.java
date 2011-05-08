import java.io.*;
import java.net.*;

public class BlackjackGame
{
	/*
		Craig Martin
		QMCS 281
		Prof Jarvis
		April 25, 2008
	*/
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		int[] results;
		String hold;
		Blackjack game;
		Player[] players = {new Player("Michael"),
					   		new Player("Jessica")};
		game = new Blackjack(4, 6);

		for(int i=0; i<players.length; i++)
		{
				game.addPlayerToGame(players[i]);
				game.activatePlayer(players[i]);
		}

		hold = "yes";
		while(hold.equalsIgnoreCase("yes") || hold.equalsIgnoreCase("y"))
		{
			game.playGame();
			System.out.println("\n" + game +"\n");

			hold = getStringFromUser("\n\nWould you like to play again?");
		}

		//results = game.getResults();


	}

	public static String getStringFromUser(String prompt)throws IOException
	{
		String hold;
		BufferedReader keyboard;

		keyboard = new BufferedReader(new InputStreamReader(System.in));

		System.out.print(prompt);
		hold = keyboard.readLine();
		return hold;
	}// getStringFromUser
}