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

	private static final String  ICAMERA_FC= "Icamera FC";
	private static final String CREW_FC = "Crew FC";
	private static final String SCAP_CITY_FC = "Scap City FC";
	private static final int SHOT_FROM_CORNER = 20;
	private static final int CORNER_FROM_CORNER = 20;
	private static final int FOUL_CHANCE_FROM_CORNER = 10;
	private static final double HOME_ADVANTAGE = 1.1;
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
		Team team1 = teamBuilder.buildTeam(SCAP_CITY_FC);
		Team team2 = teamBuilder.buildTeam(ICAMERA_FC);
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

	public static void figureOutAction(Team home, Team away, MatchReport report, int time){
		//TODO this should be redone to work with passing and tackline stats
		MatchAction action = actionMap.get(rand.nextInt(actionMap.size()-1));

		if(action.equals(MatchAction.SHOT)){
			runRandomShot(home, away, report, time);
		}else if(action.equals(MatchAction.CORNER)){
			runCorner(home, away, report, time);
		}else if(action.equals(MatchAction.FREE_KICK_ATT)){
			runAttackingFreeKick(home, away, report, time);
		}else if(action.equals(MatchAction.FREE_KICK_ATT)){	
			runDefFreeKick(home, away, report, time);
		}else if(action.equals(MatchAction.PENALTY)){
			runRandomPenalty(home, away, report, time);
		}
	}

	private static void runRandomPenalty(Team home, Team away, MatchReport report, int time) {
		double team1PenaltyChance = (getPassingStat(home) + getSpeedStat(home) - getTacklingStat(away)) * HOME_ADVANTAGE;
		double team2PenaltyChance = getPassingStat(away) + getSpeedStat(away) - getTacklingStat(home);
		int penaltyTaker = rand.nextInt((int) Math.floor(team1PenaltyChance + team2PenaltyChance));
		if(penaltyTaker<team1PenaltyChance){
			runFoul(home, away, report, time, false);
			runPenalty(home, away, report, time, true);
		}else{
			runFoul(home, away, report, time, true);
			runPenalty(home, away, report, time, false);
		}
	}

	private static void runAttackingFreeKick(Team home, Team away, MatchReport report, int time) {
		double team1FreeKickChance = (getPassingStat(home) + getSpeedStat(home) - getTacklingStat(away)) * HOME_ADVANTAGE;
		double team2FreeKickChance = getPassingStat(away) + getSpeedStat(away) - getTacklingStat(home);
		int freeKicker = rand.nextInt((int) Math.floor(team1FreeKickChance + team2FreeKickChance));
		if(freeKicker<team1FreeKickChance){
			//Home Free Kick
			runFoul(home, away, report, time, false);
			//Odds of a shot are based on setPiece skill
			Player kickTaker = getSetPieceTaker(home);
			report.pushMatchEvent(new MatchEvent("to be taken by " + kickTaker.getName() + " for " + home.getTeamName(), time, MatchAction.FREE_KICK_ATT));
			if(rand.nextInt(kickTaker.getStats().getSetPieces()+100) < kickTaker.getStats().getSetPieces()){				
				runShotChance(home,away,report,time,true);			
			}
		}else{
			runFoul(home, away, report, time, true);
			//Away Free Kick
			//Odds of a shot are based on setPiece skill
			Player kickTaker = getSetPieceTaker(away);
			report.pushMatchEvent(new MatchEvent("to be taken by " + kickTaker.getName() + " for " + away.getTeamName(), time, MatchAction.FREE_KICK_ATT));
			if(rand.nextInt(kickTaker.getStats().getSetPieces()+100) < kickTaker.getStats().getSetPieces()){				
				runShotChance(home,away,report,time,false);			
			}
		}
	}

	private static void runDefFreeKick(Team home, Team away, MatchReport report, int time) {
		double team1FreeKickChance = (getPassingStat(home) + getSpeedStat(home) - getTacklingStat(away)) * HOME_ADVANTAGE;
		double team2FreeKickChance = getPassingStat(away) + getSpeedStat(away) - getTacklingStat(home);
		int freeKicker = rand.nextInt((int) Math.floor(team1FreeKickChance + team2FreeKickChance));
		if(freeKicker<team1FreeKickChance){
			//Home Free Kick
			runFoul(home, away, report, time, false);
			Player kickTaker = getSetPieceTaker(home);
			report.pushMatchEvent(new MatchEvent("to be taken by " + kickTaker.getName() + " for " + home.getTeamName(), time, MatchAction.FREE_KICK_DEF));
		}else{
			runFoul(home, away, report, time, true);
			//Away Free Kick
			Player kickTaker = getSetPieceTaker(away);
			report.pushMatchEvent(new MatchEvent("to be taken by " + kickTaker.getName() + " for " + away.getTeamName(), time, MatchAction.FREE_KICK_DEF));
		}
	}

	private static Player getSetPieceTaker(Team home) {
		List<Player> players = home.getTheTeam();
		Player taker = players.get(0);
		for (Player player : players) {
			if(player.getStats().getSetPieces()>taker.getStats().getSetPieces()){
				taker = player;
			}
		}
		return taker;
	}

	public static void runRandomShot(Team home, Team away, MatchReport report, int time){
		double team1ScoreChance = (getScoringStat(home) + getPassingStat(home) + (getSpeedStat(home)*2) - getTacklingStat(away)) * HOME_ADVANTAGE;
		double team2ScoreChance = getScoringStat(away) + getPassingStat(away) + (getSpeedStat(away)*2) - getTacklingStat(home);
		int scorer = rand.nextInt((int) Math.floor(team1ScoreChance + team2ScoreChance));
		if(scorer<team1ScoreChance){
			runShotChance(home,away,report,time,true);
		}else{
			runShotChance(home,away,report,time,false);
		}
	}

	public static void runShotChance(Team home, Team away, MatchReport report, int time, boolean isHome){
		Player goaly;
		Player shooter;
		if(isHome){
			goaly = findGoalKeeper(away);
			shooter = findShooter(home);
			report.pushMatchEvent(new MatchEvent("by " + shooter.getName() + " for " + home.getTeamName(), time, MatchAction.SHOT));
		}else{
			goaly = findGoalKeeper(home);			
			shooter = findShooter(away);
			report.pushMatchEvent(new MatchEvent("by " + shooter.getName() + " for " + away.getTeamName(), time, MatchAction.SHOT));
		}
		int scoreChance = rand.nextInt(shooter.getStats().getShooting() + goaly.getStats().getGoalKeeping());
		if(scoreChance<shooter.getStats().getShooting()){
			//goal
			if(isHome){
				report.homeGoal();
				report.pushMatchEvent(new MatchEvent("by " + shooter.getName() + " for " + home.getTeamName(), time, MatchAction.GOAL));
			}else{
				report.awayGoal();
				report.pushMatchEvent(new MatchEvent("by " + shooter.getName() + " for " + away.getTeamName(), time, MatchAction.GOAL));
			}
		}else{
			//save
			if(isHome){
				report.pushMatchEvent(new MatchEvent("by " + goaly.getName() + " for " + away.getTeamName(), time, MatchAction.SAVE));
			}else{
				report.pushMatchEvent(new MatchEvent("by " + goaly.getName() + " for " + home.getTeamName(), time, MatchAction.SAVE));
			}
		}
	}

	public static Player findShooter(Team team){
		List<Player> possibleShooters = new ArrayList<Player>();
		List<Player> players = team.getTheTeam();
		for (Player player : players) {
			if(player.getStats().getPositions().contains(Position.FORWARD)){
				possibleShooters.add(player);
				possibleShooters.add(player);
				possibleShooters.add(player);
			}else if(player.getStats().getPositions().contains(Position.MIDFIELD)){
				possibleShooters.add(player);
				possibleShooters.add(player);				
			}else if(player.getStats().getPositions().contains(Position.DEFENSE)){
				possibleShooters.add(player);
			}
		}
		return possibleShooters.get(rand.nextInt(possibleShooters.size()));
	}

	public static void runCorner(Team home, Team away, MatchReport report, int time){
		//Figure out whose corner
		double team1CornerChance = getCornerStat(home) * HOME_ADVANTAGE;
		double team2CornerChance = getCornerStat(away);
		int cornerOwner = rand.nextInt((int) Math.floor(team1CornerChance + team2CornerChance));
		if(cornerOwner<team1CornerChance){
			report.pushMatchEvent(new MatchEvent("for " + home.getTeamName(), time, MatchAction.CORNER));
			runPostCorner(home,away,report,time,true);
		}else{			
			report.pushMatchEvent(new MatchEvent("for " + away.getTeamName(), time, MatchAction.CORNER));
			runPostCorner(home,away,report,time,false);
		}
	}


	public static void runFoul(Team home, Team away, MatchReport report, int time, boolean isHome){
		double defenseDirty = 0;
		if(isHome){
			defenseDirty = getDirtyDefenseStat(home);
		}else{
			defenseDirty = getDirtyDefenseStat(away);			
		}
		int red = rand.nextInt(2000);
		int yellow = rand.nextInt(1000);
		boolean isRed = false;
		boolean isYellow = false;
		if(red<defenseDirty){
			isRed = true;
		}else if(yellow<defenseDirty){
			isYellow = true;
		}
		//find player
		if(isRed || isYellow){
			Player cardedPlayer;
			if(isHome){
				cardedPlayer = findDirtiestBackPlayer(home);
			}else{
				cardedPlayer = findDirtiestBackPlayer(away);
			}
			if(isRed && isHome){
				report.pushMatchEvent(new MatchEvent("for " + cardedPlayer.getName() + " " + home.getTeamName(), time, MatchAction.RED));
				home.redCard(cardedPlayer);
			}else if(isRed){
				report.pushMatchEvent(new MatchEvent("for " + cardedPlayer.getName() + " " + away.getTeamName(), time, MatchAction.RED));				
				away.redCard(cardedPlayer);
			}
			if(isYellow && isHome){
				if(cardedPlayer.isYellow()){
					report.pushMatchEvent(new MatchEvent("for " + cardedPlayer.getName() + " " + home.getTeamName() + " that's his second of the game so he's gone", time, MatchAction.YELLOW));
					home.redCard(cardedPlayer);
				}else{
					report.pushMatchEvent(new MatchEvent("for " + cardedPlayer.getName() + " " + home.getTeamName() , time, MatchAction.YELLOW));
					cardedPlayer.setYellow(true);
				}
			}else if(isYellow){
				if(cardedPlayer.isYellow()){
					report.pushMatchEvent(new MatchEvent("for " + cardedPlayer.getName() + " " + away.getTeamName() + " that's his second of the game so he's gone", time, MatchAction.YELLOW));
					away.redCard(cardedPlayer);
				}else{
					report.pushMatchEvent(new MatchEvent("for " + cardedPlayer.getName() + " " + away.getTeamName() , time, MatchAction.YELLOW));
					cardedPlayer.setYellow(true);
				}			
			}
		}
	}

	public static Player findDirtiestBackPlayer(Team team){
		List<Player> players = team.getTheTeam();
		Player dirtyBastad = players.get(0);
		for (Player player : players) {
			List<Position> positions = player.getStats().getPositions();
			if(positions.contains(Position.GOALKEEPER) || positions.contains(Position.DEFENSE)||positions.contains(Position.MIDFIELD)){
				if(dirtyBastad .getStats().getPositions().contains(Position.FORWARD)){
					dirtyBastad = player;
				}else{
					if(player.getStats().getDirtyness()>dirtyBastad.getStats().getDirtyness()){
						dirtyBastad = player;
					}
				}
			}
		}
		return dirtyBastad;
	}
	public static void runPenalty(Team home, Team away, MatchReport report, int time, boolean isHome){
		//Check for foul
		runFoul(home, away, report, time, !isHome);
		if(isHome){
			Player player = findBestPenaltyTaker(home);
			Player goaly = findGoalKeeper(away);
			report.pushMatchEvent(new MatchEvent("for " + home.getTeamName() + ". " + player.getName() + " prepares to take the shot, whilst " + goaly.getName() + " awaits his chance to save.", time, MatchAction.PENALTY));
			int scored = rand.nextInt((int) Math.floor(player.getStats().getShooting()  + (goaly.getStats().getGoalKeeping()*1.5)));
			if(scored<player.getStats().getShooting()){
				report.pushMatchEvent(new MatchEvent(player.getName() + " scores for " + home.getTeamName(), time, MatchAction.GOAL));
				report.homeGoal();
			}else{
				report.pushMatchEvent(new MatchEvent("by " + goaly.getName(), time, MatchAction.SAVE));
			}
		}else{
			Player player = findBestPenaltyTaker(away);			
			Player goaly = findGoalKeeper(home);
			int scored = rand.nextInt((int) Math.floor(player.getStats().getShooting() +  player.getStats().getSetPieces() + (goaly.getStats().getGoalKeeping()*1.5)));
			if(scored<player.getStats().getShooting()){
				report.pushMatchEvent(new MatchEvent(player.getName() + " scores for " + away.getTeamName(), time, MatchAction.GOAL));
				report.awayGoal();
			}else{
				report.pushMatchEvent(new MatchEvent("by " + goaly.getName(), time, MatchAction.SAVE));
			}
		}
	}

	private static Player findGoalKeeper(Team away) {
		Player bestGoaly =  away.getTheTeam().get(0);
		for (Player player : away.getTheTeam()) {
			if(player.getStats().getPositions().contains(Position.GOALKEEPER)){
				return player;
			}else{
				if(player.getStats().getGoalKeeping() > bestGoaly.getStats().getGoalKeeping()){
					bestGoaly = player;
				}
			}
		}
		return bestGoaly;
	}

	public static Player findBestPenaltyTaker(Team team){
		List<Player> players = team.getTheTeam();
		Player penaltyTaker = players.get(0);
		for (Player player : players) {
			if(player.getStats().getShooting() + player.getStats().getSetPieces() > penaltyTaker.getStats().getShooting() + penaltyTaker.getStats().getSetPieces()){
				penaltyTaker = player;
			}
		}
		return penaltyTaker;
	}

	public static void runPostCorner(Team home, Team away, MatchReport report, int time, boolean isHome){
		int whatHappend = rand.nextInt(100);
		if(whatHappend < SHOT_FROM_CORNER){
			//Shot
			runShotChance(home,away,report,time,isHome);
		}else if(whatHappend < SHOT_FROM_CORNER + FOUL_CHANCE_FROM_CORNER){
			//Foul
			if(isHome){
				double homePenaltyChance = getDirtyDefenseStat(away);
				double awayKickChance = getDirtyAttackStat(home);
				int penaltyOwner = rand.nextInt((int) Math.floor(homePenaltyChance + awayKickChance));
				if(penaltyOwner<homePenaltyChance){
					//Its a penalty
					runPenalty(home, away, report, time, isHome);
				}else{
					//Nothing happens
				}
			}else{
				double awayPenaltyChance = getDirtyDefenseStat(home);
				double homeKickChance = getDirtyAttackStat(away);
				int penaltyOwner = rand.nextInt((int) Math.floor(awayPenaltyChance + homeKickChance));
				if(penaltyOwner<awayPenaltyChance){
					runPenalty(home, away, report, time, isHome);
				}else{
					//Nothing happens
				}
			}
		}else if(whatHappend < SHOT_FROM_CORNER + FOUL_CHANCE_FROM_CORNER + CORNER_FROM_CORNER){
			if(isHome){
				report.pushMatchEvent(new MatchEvent("for " + home.getTeamName(), time, MatchAction.CORNER));				
			}else{
			}
			runPostCorner(home, away, report, time, isHome);
		}else{
			//Nothing comes from the corner
		}
	}




	public static double getDirtyAttackStat(Team team){
		double dirty = 0;
		List<Player> players = team.getTheTeam();
		for (Player player : players) {
			Stats stats = player.getStats();
			if(stats.getPositions().contains(Position.FORWARD)){
				dirty = dirty + stats.getDirtyness();
			}else if(stats.getPositions().contains(Position.MIDFIELD)){
				dirty = dirty + (stats.getDirtyness() * 0.5);
			}else if(stats.getPositions().contains(Position.DEFENSE)){
				dirty = dirty + (stats.getDirtyness() * 0.2);
			}
		}
		return dirty;
	}

	public static double getDirtyDefenseStat(Team team){
		double dirty = 0;
		List<Player> players = team.getTheTeam();
		for (Player player : players) {
			Stats stats = player.getStats();
			if(stats.getPositions().contains(Position.FORWARD)){
				dirty = dirty + (stats.getDirtyness() * 0.2);
			}else if(stats.getPositions().contains(Position.MIDFIELD)){
				dirty = dirty + (stats.getDirtyness() * 0.5);
			}else if(stats.getPositions().contains(Position.DEFENSE)){
				dirty = dirty + stats.getDirtyness();
			}
		}
		return dirty;
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
		return score + getLeadership(team);
	}
	public static double getPassingStat(Team team){
		double pass = 0;
		List<Player> players = team.getTheTeam();
		for (Player player : players) {
			Stats stats = player.getStats();
			if(stats.getPositions().contains(Position.MIDFIELD)){
				pass = pass + stats.getPassing();
			}else if(stats.getPositions().contains(Position.DEFENSE)){
				pass = pass + (stats.getPassing() * 0.5);
			}else if(stats.getPositions().contains(Position.FORWARD)){
				pass = pass + (stats.getPassing() * 0.2);
			}
		}
		return pass + getLeadership(team);
	}
	public static double getTacklingStat(Team team){
		double tackle = 0;
		List<Player> players = team.getTheTeam();
		for (Player player : players) {
			Stats stats = player.getStats();
			if(stats.getPositions().contains(Position.DEFENSE)){
				tackle = tackle + stats.getTackling();
			}else if(stats.getPositions().contains(Position.MIDFIELD)){
				tackle = tackle + (stats.getTackling() * 0.5);
			}else if(stats.getPositions().contains(Position.FORWARD)){
				tackle = tackle + (stats.getTackling() * 0.2);
			}
		}
		return tackle + getLeadership(team);
	}
	public static double getSpeedStat(Team team){
		double speed = 0;
		List<Player> players = team.getTheTeam();
		for (Player player : players) {
			Stats stats = player.getStats();
			if(stats.getPositions().contains(Position.FORWARD)){
				speed = speed + stats.getSpeed();
			}else if(stats.getPositions().contains(Position.MIDFIELD)){
				speed = speed + (stats.getSpeed() * 0.5);
			}else if(stats.getPositions().contains(Position.DEFENSE)){
				speed = speed + (stats.getSpeed() * 0.2);
			}
		}
		return speed + getLeadership(team);
	}

	public static int getLeadership(Team team){
		int topLeader = 0;
		List<Player> players = team.getTheTeam();
		for (Player player : players) {
			if(player.getStats().getLeadership()>topLeader){
				topLeader = player.getStats().getLeadership(); 
			}
		}
		return topLeader;
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
		return corner + getLeadership(team);
	}


	private static void buildActionMap(){
		actionMap.put(0, MatchAction.CORNER);
		actionMap.put(1, MatchAction.CORNER);
		actionMap.put(2, MatchAction.CORNER);
		actionMap.put(3, MatchAction.CORNER);
		actionMap.put(4, MatchAction.FREE_KICK_ATT);
		actionMap.put(5, MatchAction.FREE_KICK_ATT);
		actionMap.put(6, MatchAction.FREE_KICK_ATT);
		actionMap.put(7, MatchAction.FREE_KICK_ATT);
		actionMap.put(8, MatchAction.FREE_KICK_ATT);
		actionMap.put(9, MatchAction.FREE_KICK_DEF);
		actionMap.put(10, MatchAction.FREE_KICK_DEF);
		actionMap.put(11, MatchAction.FREE_KICK_DEF);
		actionMap.put(12, MatchAction.FREE_KICK_DEF);
		actionMap.put(13, MatchAction.FREE_KICK_DEF);
		actionMap.put(14, MatchAction.PENALTY);
		actionMap.put(15, MatchAction.SHOT);
		actionMap.put(16, MatchAction.SHOT);
		actionMap.put(17, MatchAction.SHOT);
		actionMap.put(18, MatchAction.SHOT);
		actionMap.put(19, MatchAction.SHOT);
	}


}
