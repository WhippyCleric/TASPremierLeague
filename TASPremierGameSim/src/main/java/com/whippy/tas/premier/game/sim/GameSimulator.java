package com.whippy.tas.premier.game.sim;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.whippy.tas.premier.beans.Player;
import com.whippy.tas.premier.beans.Position;
import com.whippy.tas.premier.beans.Stats;
import com.whippy.tas.premier.beans.Team;
import com.whippy.tas.premier.game.builders.PlayerBuilder;
import com.whippy.tas.premier.game.builders.TeamBuilder;
import com.whippy.tas.premier.game.report.MatchReport;
import com.whippy.tas.premier.util.Utils;

public class GameSimulator {

	public static void main(String[] args) throws IOException{
		List<String> linesOfPlayers = Utils.getNames("TAS Premier League Player List - players.csv", GameSimulator.class);
		List<Player> players = new ArrayList<Player>();
		
		for (String playerLine : linesOfPlayers) {
			List<String> attributes = Arrays.asList(playerLine.split(","));
			if(!attributes.get(0).equals("Name")){
				players.add(PlayerBuilder.buildPlayer(attributes));
			}
		}
		TeamBuilder teamBuilder = new TeamBuilder(players);
		Team team1 = teamBuilder.buildTeam("team1");
		Team team2 = teamBuilder.buildTeam("team2");
		MatchReport matchReport = new MatchReport(team1, team2);
		System.out.println(matchReport);
	}
}
