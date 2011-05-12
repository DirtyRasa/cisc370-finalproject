package game.server;

public enum Games {
	BLACKJACK,
	QUIT;
	
	public static String getGameList(){
		String list = "";
		for(Games g : Games.values()){
			//list += g.ordinal() + ": "+ g.name() + "<>";
			list += g.name() + "<>";
		}
		return list;
	}
}
