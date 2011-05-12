package communication;

public enum Response {
	YES, Y, NO, N, QUIT, BYE;
	
	public static boolean binaryEval(String str) throws ResponseException{
		try{
			switch(valueOf(str.toUpperCase())){
				case YES:
				case Y:
					return true;
				case NO:
				case N:
					return false;
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
		return Double.parseDouble(str);
	}
}

