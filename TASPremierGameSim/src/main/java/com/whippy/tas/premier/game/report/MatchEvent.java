package com.whippy.tas.premier.game.report;

public class MatchEvent {

	private final String summary;
	private final String time;
	private final MatchAction action;
	
	public MatchEvent(String summary, String time, MatchAction action) {
		this.summary = summary;
		this.time = time;
		this.action = action;
	}
	
	
}
