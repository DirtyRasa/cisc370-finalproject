package game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import communication.Response;
import communication.ResponseException;

public class TestInputs 
{
	public BufferedReader _in;
	public String s;
	
	public static void main(String[] args) 
	{
		new TestInputs();
	}

	public TestInputs()
	{		
		s = getInputWithTimeout(10);
		
		try {
			System.out.println(Response.trinaryEval(s));
		} catch (ResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getInputWithTimeout(int timeLimit)
	{
		String input = "quit";
		long startTime = Calendar.getInstance().getTime().getTime();
		
		try {
			BufferedReader _in = new BufferedReader(new InputStreamReader(System.in));
			
			while(!_in.ready() && !timeUp(startTime, timeLimit)){;}
			
			if(timeUp(startTime, timeLimit))
				input = "quit";
			else
				input=_in.readLine();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return input;
	}
	
	private boolean timeUp(long startTime, long timeLimit)
	{
		if(Calendar.getInstance().getTime().getTime() - startTime >= timeLimit*1000)
			return true;
		else
			return false;
	}
}
