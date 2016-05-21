package com.whippy.tas.premier.beans;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.whippy.tas.premier.util.Utils;

public class Team {

	private String teamName;
	private List<Player> theTeam;

	public Team(String teamName, List<Player> theTeam) throws IOException{
		this.teamName = teamName;
		this.theTeam = theTeam;
	}

}
