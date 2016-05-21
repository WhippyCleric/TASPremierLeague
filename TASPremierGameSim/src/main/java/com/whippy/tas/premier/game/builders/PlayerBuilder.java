package com.whippy.tas.premier.game.builders;

import java.util.ArrayList;
import java.util.List;

import com.whippy.tas.premier.beans.Player;
import com.whippy.tas.premier.beans.Position;
import com.whippy.tas.premier.beans.Stats;

public class PlayerBuilder {

	
	public static Player buildPlayer(List<String> attributes){
		Player player = new Player();
		player.setName(attributes.get(0));
		player.setValue(Integer.valueOf(attributes.get(1)));
		Stats stats = new Stats();
		
		String[] positionArray = attributes.get(2).split(" ");
		List<Position> positions = new ArrayList<Position>();
		
		for (String position : positionArray) {
			if(position.equals("Goal")){
				positions.add(Position.GOALKEEPER);
			}else if(position.equals("Keeper")){
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
		return player;
	}
}
