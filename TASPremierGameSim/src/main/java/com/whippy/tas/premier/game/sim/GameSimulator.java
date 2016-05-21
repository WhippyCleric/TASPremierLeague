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
	
	public static void runRandomGoal(Team home, Team away, MatchReport report, int time){
		//In the event of a random goal currently only scoring stat is taken into account
		double team1ScoreChance = getScoringStat(home);
		double team2ScoreChance = getScoringStat(away);
		int scorer = rand.nextInt((int) Math.floor(team1ScoreChance + team2ScoreChance));
		if(scorer<team1ScoreChance){
			report.homeGoal();
			report.pushMatchEvent(new MatchEvent(" for " + home.getTeamName(), time, MatchAction.GOAL));
		}else{
			report.awayGoal();				
			report.pushMatchEvent(new MatchEvent(" for " + away.getTeamName(), time, MatchAction.GOAL));
		}
	}
	
	public static void runCorner(Team home, Team away, MatchReport report, int time){
		//Figure out whose corner
		double team1CornerChance = getCornerStat(home);
		double team2CornerChance = getCornerStat(away);
		int cornerOwner = rand.nextInt((int) Math.floor(team1CornerChance + team2CornerChance));
		if(cornerOwner<team1CornerChance){
			report.homeGoal();
			report.pushMatchEvent(new MatchEvent(" for " + home.getTeamName(), time, MatchAction.CORNER));
		}else{
			report.awayGoal();				
			report.pushMatchEvent(new MatchEvent(" for " + away.getTeamName(), time, MatchAction.CORNER));
		}
	}
	
	
	public static void figureOutAction(Team home, Team away, MatchReport report, int time){
		MatchAction action = actionMap.get(rand.nextInt(actionMap.size()-1));
		if(action.equals(MatchAction.GOAL)){
			runRandomGoal(home, away, report, time);
		}else if(action.equals(MatchAction.CORNER)){
			runCorner(home, away, report, time);
		}else{			
			report.pushMatchEvent(new MatchEvent("", time, action));
		}
	}
	
	public static double getScoringStat(Team team){
		double score = 0;
		List<Player> players = team.getTheTeam();
		for (Player player : players) {
			Stats stats = player.getStats();
			if(stats.getPositions().contains(Position.FORWARD)){
				score = score + stats.getShooting();
			}else if(stats.getPositions().contains(Position.MIDFIELD)){
				score = score + (stats.getShooting() * 0.5);
			}else if(stats.getPositions().contains(Position.DEFENSE)){
				score = score + (stats.getShooting() * 0.2);
			}
		}
		return score;
	}
	

	public static double getCornerStat(Team team){
		double corner = 0;
		List<Player> players = team.getTheTeam();
		//Corners will are weighted so that low tackling of defenders and midfield is bad, and good passing for attacking half is good...
		for (Player player : players) {
			Stats stats = player.getStats();
			if(stats.getPositions().contains(Position.FORWARD)){
				corner = corner + stats.getPassing();
				corner = corner + stats.getTackling() * 0.2;
			}else if(stats.getPositions().contains(Position.MIDFIELD)){
				corner = corner + (stats.getPassing() * 0.5);
				corner = corner + stats.getTackling() * 0.5;
			}else if(stats.getPositions().contains(Position.DEFENSE)){
				corner = corner + (stats.getPassing() * 0.2);
				corner = corner + stats.getTackling();
			}
		}
		return corner;
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
