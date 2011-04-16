import java.net.*;
import java.io.*;

public class BlackjackClient
{
	/*
		Craig Martin
		QMCS 281
		Prof Jarvis
		April 25, 2008
	*/
	public static void main(String[] args)throws UnknownHostException, IOException, ClassNotFoundException
	{
		Socket client;
		String ipNumber;
		String input = "";
		String hold = "";
		ipNumber = "140.209.124.149";
		client = new Socket(ipNumber, 80);

		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));


		while(!input.equals("end"))
		{
			input = in.readLine();
			if(input.endsWith("?"))
			{
				hold = getStringFromUser(input);
				out.println(hold);
				out.flush();
			}
			else
				System.out.println(input);
		}
		System.out.println("\nClient has ended");
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