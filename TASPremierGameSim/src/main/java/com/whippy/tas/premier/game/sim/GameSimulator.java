package com.whippy.tas.premier.game.sim;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.event.MenuEvent;

import com.whippy.tas.premier.beans.Player;
import com.whippy.tas.premier.beans.Position;
import com.whippy.tas.premier.beans.Stats;
import com.whippy.tas.premier.beans.Team;
import com.whippy.tas.premier.game.builders.PlayerBuilder;
import com.whippy.tas.premier.game.builders.TeamBuilder;
import com.whippy.tas.premier.game.report.MatchAction;
import com.whippy.tas.premier.game.report.MatchEvent;
import com.whippy.tas.premier.game.report.MatchReport;
import com.whippy.tas.premier.util.Utils;

public class GameSimulator {

	private static int CHANCE_OF_ACTION = 30;
	private static Random rand = new Random();
	private static Map<Integer, MatchAction> actionMap = new HashMap<Integer,MatchAction>();
	
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
		buildActionMap();
		
		
		System.out.println(simulateGame(team1, team2).toString());
	}
	
	public static MatchReport simulateGame(Team home, Team away){
		MatchReport matchReport = new MatchReport(home, away);
		matchReport.pushMatchEvent(new MatchEvent("here, lets get underway", 0, MatchAction.FRIST_KICK_OFF));
		for(int i = 0; i<45; i++){
			if(rand.nextInt(100) < CHANCE_OF_ACTION){
				figureOutAction(home, away, matchReport, i);
			}
		}
		matchReport.pushMatchEvent(new MatchEvent("score is " + home.getTeamName() + " " + matchReport.getHomeScore() + " - " + matchReport.getAwayScore() + " " + away.getTeamName(), 45, MatchAction.HALF_TIME));
		matchReport.pushMatchEvent(new MatchEvent("and we're underway" , 45, MatchAction.SECOND_KICK_OFF));
		for(int i = 45; i<90; i++){
			if(rand.nextInt(100) < CHANCE_OF_ACTION){
				figureOutAction(home, away, matchReport, i);
			}
		}
		matchReport.pushMatchEvent(new MatchEvent("" , 90, MatchAction.FULL_TIME));
		
		return matchReport;
	}
	
	public static void figureOutAction(Team team1, Team team2, MatchReport report, int time){
		MatchAction action = actionMap.get(rand.nextInt(actionMap.size()-1));
		report.pushMatchEvent(new MatchEvent("", time, action));
	}
	
	
	private static void buildActionMap(){
		actionMap.put(0, MatchAction.CORNER);
		actionMap.put(1, MatchAction.CORNER);
		actionMap.put(2, MatchAction.CORNER);
		actionMap.put(3, MatchAction.FREE_KICK_ATT);
		actionMap.put(4, MatchAction.FREE_KICK_ATT);
		actionMap.put(5, MatchAction.FREE_KICK_ATT);
		actionMap.put(6, MatchAction.FREE_KICK_ATT);
		actionMap.put(7, MatchAction.FREE_KICK_DEF);
		actionMap.put(8, MatchAction.FREE_KICK_DEF);
		actionMap.put(9, MatchAction.FREE_KICK_DEF);
		actionMap.put(10, MatchAction.FREE_KICK_DEF);
		actionMap.put(11, MatchAction.GOAL);
		actionMap.put(12, MatchAction.PENALTY);
		actionMap.put(13, MatchAction.RED);
		actionMap.put(14, MatchAction.YELLOW);
		actionMap.put(15, MatchAction.YELLOW);
		actionMap.put(16, MatchAction.SHOT);
		actionMap.put(17, MatchAction.SHOT);
		actionMap.put(18, MatchAction.SHOT);
		actionMap.put(19, MatchAction.SHOT);
		actionMap.put(20, MatchAction.SHOT);
	}
	
	
}
