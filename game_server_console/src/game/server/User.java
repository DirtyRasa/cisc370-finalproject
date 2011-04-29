package game.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User {
	private Socket _client;
	PrintWriter _out;
	BufferedReader _in;
	String _name;

	public User(String name, Socket client, PrintWriter out, BufferedReader in)
	{
		_client = client;
		_out = out;
		_in = in;
		_name = name;
	}

	public Socket getSocket()
	{
		return _client;
	}

	public PrintWriter getOutput()
	{
		return _out;
	}

	public BufferedReader getInput()
	{
		return _in;
	}
	
	public String getName()
	{
		return _name;
	}
}
