package game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;

//import dal.DataAccessLayer;

public class User{
	private Socket _client;
	private PrintWriter _out;
	private BufferedReader _in;
	private String _userName;
	private double _money;
	private int _wins;
	private int _losses;
	private int _pushes;
	private int _total;
	private int _bet;
	
	//private DataAccessLayer _dal;

	public User(Socket client, PrintWriter out, BufferedReader in)
	{
		//_dal = new DataAccessLayer();
		_client = client;
		_out = out;
		_in = in;
		_money = 0;
		_wins = 0;
		_losses = 0;
		_pushes = 0;
		_total = 0;
		_bet = 0;
	}

	public Socket getSocket()  { return _client; }

	public PrintWriter getOutput()  { return _out; }

	public BufferedReader getInput()  { return _in; }
	
	public String getInputWithTimeout(int timeLimit)
	{
		String input = "quit";
		long startTime = Calendar.getInstance().getTime().getTime();	
		
		try {
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
	
	public String getName()  { return _userName; }
	
	public void setName(String name){
		_userName = name;
	}
	
	public double getMoney() { return _money; }
	
	public void setMoney(double money){
		_money = money;
	}
	
	public double getBet() { return _bet; }
	
	public void setBet(int bet){
		_bet = bet;
	}
	
	/*
	public boolean addMoney(double add)
	{
		_money += add;
		try {
			_dal.setMoney(_userName, _money);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//TODO Should we allow -money? probably not Handle elsewhere?
	public boolean subtractMoney(double subtract)
	{
		_money -= subtract;
		try {
			_dal.setMoney(_userName, _money);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}*/
	public int getWins() { return _wins; }
	
	public void setWins(int wins){
		_wins = wins;
	}
	
	public int getLosses() { return _losses; }
	
	public void setLosses(int losses){
		_losses = losses;
	}
	
	public int getPushes() { return _pushes; }
	
	public void setPushes(int pushes){
		_pushes = pushes;
	}
	
	public int getTotal() { return _total; }
	
	public void setTotal(int total){
		_total = total;
	}
}
