import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Team {

	

	private String teamName;
	private ArrayList<Player> theTeam;

	public Team(String teamFile, List<Player> allPlayers) throws IOException{
		List<String> names = Utils.getNames(teamFile);
		teamName = teamFile;
		theTeam = new ArrayList<Player>();
		for (Player player : allPlayers) {
			if(names.contains(player.getName())){
				theTeam.add(player);
			}
		}
		
		System.out.println(theTeam);
	}
	
}
