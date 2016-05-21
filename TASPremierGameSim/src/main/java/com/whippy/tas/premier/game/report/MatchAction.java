package com.whippy.tas.premier.game.report;

public enum MatchAction {

	GOAL("GOAL"),
	SHOT_MISS("Shot"),
	SHOT_SAVE("Save"),
	CORNER("Corner"),
	FREE_KICK_DEF("Free kick in defenses half"),
	FREE_KICK_ATT("Free kick in attacking half"),
	PENALTY("Penalty"),
	YELLOW("Yellow Card"),
	RED("Red Card"),
	FRIST_KICK_OFF("Kick off"),
	HALF_TIME("Half time"),
	SECOND_KICK_OFF("Second half kick off"),
	FULL_TIME("Full time");
	
	
	private String action;

	MatchAction(String action){
		this.action = action;
	}
	
	@Override
	public String toString(){
		return action;
	}
}
