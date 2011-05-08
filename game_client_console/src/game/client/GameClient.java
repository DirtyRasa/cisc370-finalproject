package game.client;
import java.net.*;
import java.io.*;

public class GameClient
{
	public static void main(String[] args)throws UnknownHostException, IOException, ClassNotFoundException
	{
		Socket client;
		String ipNumber;
		String input = "";
		String hold = "";
		ipNumber =  //"localhost";
					"140.209.124.171";
		client = new Socket(ipNumber, 80);

		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));


		while(true)
		{
			input = in.readLine();
			if(input.equals("end"))
				break;
			
			if(input.equals("<QUESTION>"))
			{
				hold = getStringFromUser();
				out.println(hold);
				out.flush();
			}
			else if(input.equals("<PASSWORD>"))
			{
				//hold = getPassword().toString();
				hold = getStringFromUser();
				out.println(hold);
				out.flush();
			}
			else
				System.out.println(input);
		}
		System.out.println("\nClient has ended");
	}

	private static String getStringFromUser()throws IOException
	{
		String hold;
		BufferedReader keyboard;

		keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		hold = keyboard.readLine();
		return hold;
	}// getStringFromUser
	
	/*private static char[] getPassword() {
		Console console = System.console();
        return console.readPassword();
	}*/
}