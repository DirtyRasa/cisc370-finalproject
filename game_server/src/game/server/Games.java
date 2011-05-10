package game.server;

public enum Games {
	QUIT,
	BLACKJACK;
	
	public static String getGameList(){
		String list = "";
		int i = 0;
		for(Games g : Games.values()){
			list += g.ordinal() + ": "+ g.name() + "\n";
			i++;
		}
		return list;
	}
}
