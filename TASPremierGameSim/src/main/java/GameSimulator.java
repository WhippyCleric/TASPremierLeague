import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameSimulator {

	public static void main(String[] args) throws IOException{
		List<String> linesOfPlayers = Utils.getNames("TAS Premier League Player List - players.csv");
		List<Player> players = new ArrayList<Player>();
		for (String playerLine : linesOfPlayers) {
			List<String> attributes = Arrays.asList(playerLine.split(","));
			if(!attributes.get(0).equals("Name")){
				Player player = new Player();
				player.setName(attributes.get(0));
				player.setValue(Integer.valueOf(attributes.get(1)));
				Stats stats = new Stats();
				
				String[] positionArray = attributes.get(2).split(" ");
				List<Position> positions = new ArrayList<Position>();
				
				for (String position : positionArray) {
					if(position.equals("Goal") || position.equals("Keeper")){
						positions.add(Position.GOALKEEPER);
					}else{						
						positions.add(Position.valueOf(position.toUpperCase()));
					}
				}
				stats.setPositions(positions);
				stats.setAge(Integer.valueOf(attributes.get(3)));
				stats.setShooting(Integer.valueOf(attributes.get(4)));
				stats.setSpeed(Integer.valueOf(attributes.get(5)));
				stats.setPassing(Integer.valueOf(attributes.get(6)));
				stats.setTackling(Integer.valueOf(attributes.get(7)));
				stats.setLeadership(Integer.valueOf(attributes.get(8)));
				stats.setSetPieces(Integer.valueOf(attributes.get(9)));
				stats.setDirtyness(Integer.valueOf(attributes.get(10)));
				stats.setGoalKeeping(Integer.valueOf(attributes.get(11)));
				player.setStats(stats);
				players.add(player);
			}
		}
		new Team("team1", players);
	}
}
