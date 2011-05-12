package communication;

public enum Response {
	YES, Y, NO, N, QUIT, BYE, CHAT;
	
	public static boolean binaryEval(String str) throws ResponseException{
		try{
			switch(valueOf(str.toUpperCase())){
				case YES:
				case Y:
					return true;
				case NO:
				case N:
					return false;
				case QUIT:
				case BYE:
					throw new ResponseException("QUIT");
				case CHAT:
					throw new ResponseException("CHAT");
				default:
					throw new ResponseException("Not a valid response. Please try again.");
			}
		}
		catch (Exception ex){
			throw new ResponseException("Invalid response. Please try again");
		}
	}
	
	public static int trinaryEval(String str) throws ResponseException
	{
		try{
			switch(valueOf(str.toUpperCase())){
				case YES:
				case Y:
					return 1;
				case NO:
				case N:
					return -1;
				case BYE:
				case QUIT:
					return 0;
				case CHAT:
					throw new ResponseException("CHAT");
				default:
					throw new ResponseException("Not a valid response. Please try again.");
			}
		}
		catch (Exception ex){
			throw new ResponseException("Invalid response. Please try again");
		}
	}
	
	public static boolean quit(String str) throws ResponseException{
		try{
			switch(valueOf(str.toUpperCase())){
				case YES:
				case Y:
					return true;
				case NO:
				case N:
					return false;	
				case QUIT:
				case BYE:
					return true;
				default:
					throw new ResponseException("Not a valid response. Please try again.");
			}
		}
		catch (Exception ex){
			throw new ResponseException("Invalid response. Please try again");
		}
	}
	public static double bet(String str) throws ResponseException{
		try{
			return Double.parseDouble(str);
		}
		catch (Exception ex){
			if(str.startsWith("CHAT"))
				throw new ResponseException("CHAT");
			throw new ResponseException("Invalid response. Please try again");
		}
		
	}
}

