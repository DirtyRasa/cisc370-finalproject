package game.server.c;

public enum Response {
	YES, Y, NO, N, QUIT, BYE;
	
	static boolean binaryEval(String str) throws ResponseException{
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
	
	static boolean quit(String str) throws ResponseException{
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
}
