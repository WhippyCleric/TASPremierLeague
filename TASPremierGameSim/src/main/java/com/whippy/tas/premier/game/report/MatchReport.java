package com.whippy.tas.premier.game.report;

import java.util.ArrayList;
import java.util.List;

import com.whippy.tas.premier.beans.Team;

public class MatchReport {
	
	private List<MatchEvent> matchEvents;
	private Team homeTeam;
	private Team awayTeam;

	public MatchReport(Team homeTeam, Team awayTeam) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		matchEvents = new ArrayList<MatchEvent>();
	}
	
	public void pushMatchEvent(MatchEvent event){
		matchEvents.add(event);
	}
	
	public String getPreGameSummary(){
		StringBuilder preGameSummary = new StringBuilder();
		preGameSummary.append("Another fine day for a game of football with home side ");
		preGameSummary.append(homeTeam.getTeamName());
		preGameSummary.append(" facing off against ");
		preGameSummary.append(awayTeam.getTeamName());
		preGameSummary.append("\nLets take a look at the line ups: \n");
		preGameSummary.append(homeTeam.toString());
		preGameSummary.append("\n");
		preGameSummary.append(awayTeam.toString());
		return preGameSummary.toString();
	}
	
	@Override
	public String toString(){
		StringBuilder reportBuilder = new StringBuilder();
		reportBuilder.append(getPreGameSummary());
		return reportBuilder.toString();
	}
	
	
	
}
