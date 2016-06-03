package com.whippy.tas.premier.beans;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.whippy.tas.premier.util.Utils;

public class Team {

	private String teamName;
	private List<Player> theTeam;
	private List<Player> subs;

	public Team(String teamName, List<Player> theTeam, List<Player> subs) throws IOException{
		this.teamName = teamName;
		this.theTeam = theTeam;
		this.subs = subs;
	}
	
	public void sub(Player on, Player off){
		theTeam.remove(off);
		subs.remove(on);
		theTeam.add(on);
	}
	
	public void redCard(Player off){
		theTeam.remove(off);
	}
	
	public String getTeamName() {
		return teamName;
	}

	public List<Player> getTheTeam() {
		return theTeam;
	}

	public List<Player> getSubs() {
		return subs;
	}
	
	@Override
	public String toString(){
		StringBuilder teamSummary = new StringBuilder();
		teamSummary.append(getTeamName());
		teamSummary.append("\n");
		teamSummary.append(getPlayerSummary(theTeam));
		teamSummary.append("Subs: \n");			
		teamSummary.append(getPlayerSummary(subs));
		return teamSummary.toString();
	}

	private String getPlayerSummary(List<Player> players) {
		StringBuilder playerSummary = new StringBuilder();
		for (Player player : players) {
			playerSummary.append(player.getName());
			playerSummary.append("  ");
			playerSummary.append(player.getStats().getPositions());
			playerSummary.append("\n");			
		}
		return playerSummary.toString();
	}

}
