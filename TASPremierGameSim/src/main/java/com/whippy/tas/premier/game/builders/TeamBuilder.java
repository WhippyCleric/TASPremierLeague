package com.whippy.tas.premier.game.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.whippy.tas.premier.beans.Player;
import com.whippy.tas.premier.beans.Team;
import com.whippy.tas.premier.util.Utils;

public class TeamBuilder {

	private List<Player> allPlayers;

	public TeamBuilder(List<Player> allPlayers){
		this.allPlayers = allPlayers;
	}
	
	public Team buildTeam(String teamFile) throws IOException{
		List<String> names = Utils.getNames(teamFile, this.getClass());
		String teamName = teamFile;
		ArrayList<Player> theTeam = new ArrayList<Player>();
		ArrayList<Player> theSubs = new ArrayList<Player>();
		for (Player player : allPlayers) {
			if(names.contains(player.getName().toUpperCase())){
				if(names.indexOf(player.getName().toUpperCase())<11){					
					theTeam.add(player);
				}else{
					theSubs.add(player);
				}
			}
		}
		return new Team(teamName, theTeam, theSubs);
	}
}
