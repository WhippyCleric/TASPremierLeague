package com.whippy.tas.premier.game.report;

import java.util.ArrayList;
import java.util.List;

import com.whippy.tas.premier.beans.Team;

import sun.nio.cs.ext.MacThai;

public class MatchReport {
	
	private List<MatchEvent> matchEvents;
	private Team homeTeam;
	private Team awayTeam;
	private int homeScore = 0;
	private int awayScore = 0;
	private StringBuilder preGameSummary;

	public MatchReport(Team homeTeam, Team awayTeam) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		matchEvents = new ArrayList<MatchEvent>();
		 preGameSummary = new StringBuilder();
		preGameSummary.append("Another fine day for a game of football with home side ");
		preGameSummary.append(homeTeam.getTeamName());
		preGameSummary.append(" facing off against ");
		preGameSummary.append(awayTeam.getTeamName());
		preGameSummary.append("\nLets take a look at the line ups: \n");
		preGameSummary.append(homeTeam.toString());
		preGameSummary.append("\n");
		preGameSummary.append(awayTeam.toString());
	}
	
	public void pushMatchEvent(MatchEvent event){
		matchEvents.add(event);
	}
	
	public void homeGoal(){
		homeScore++;
	}
	
	public int getHomeScore() {
		return homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void awayGoal(){
		awayScore++;
	}
	
	public String getPreGameSummary(){
		return preGameSummary.toString();
	}
	
	@Override
	public String toString(){
		StringBuilder reportBuilder = new StringBuilder();
		reportBuilder.append(getPreGameSummary());
		for (MatchEvent matchEvent : matchEvents) {
			reportBuilder.append("\n");
			reportBuilder.append(matchEvent.toString());
		}
		reportBuilder.append("\nThats all from here today, final score ");
		reportBuilder.append(homeTeam.getTeamName());
		reportBuilder.append(" ");
		reportBuilder.append(homeScore);
		reportBuilder.append(" - ");
		reportBuilder.append(awayScore);
		reportBuilder.append(" ");
		reportBuilder.append(awayTeam.getTeamName());
		return reportBuilder.toString();
	}
	
	
	
}
