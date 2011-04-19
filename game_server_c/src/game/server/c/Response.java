package game.server.c;

public enum Response {
	YES, Y, NO, N, QUIT, BYE, NOVALUE;
	
	static boolean eval(String str) throws ResponseException{
		try{
			switch(valueOf(str.toUpperCase())){
				case YES:
				case Y:
					return true;
				case NO:
				case N:
					return false;
				case NOVALUE:
				default:
					throw new ResponseException("Not a valid response. Please try again.");
			}
		}
		catch (Exception ex){
			throw new ResponseException("Invalid response. Please try again");
		}
	}
}
