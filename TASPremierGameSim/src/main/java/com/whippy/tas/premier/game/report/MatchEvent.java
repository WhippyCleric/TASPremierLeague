package com.whippy.tas.premier.game.report;

public class MatchEvent {

	private final String summary;
	private final int time;
	private final MatchAction action;
	
	public MatchEvent(String summary, int time, MatchAction action) {
		this.summary = summary;
		this.time = time;
		this.action = action;
	}
	
	@Override
	public String toString(){
		StringBuilder matchEventSB = new StringBuilder();
		matchEventSB.append(time);
		matchEventSB.append(": ");
		matchEventSB.append(action.toString());
		matchEventSB.append(" ");
		matchEventSB.append(summary);
		return matchEventSB.toString();
	}
	
	
}
