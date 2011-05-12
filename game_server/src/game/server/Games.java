package game.server;

public enum Games {
	QUIT,
	BLACKJACK;
	
	public static String getGameList(){
		String list = "";
		for(Games g : Games.values()){
			//list += g.ordinal() + ": "+ g.name() + "<>";
			list += g.name() + "<>";
		}
		return list;
	}
}
