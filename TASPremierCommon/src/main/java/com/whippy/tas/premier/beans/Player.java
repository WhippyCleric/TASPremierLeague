package com.whippy.tas.premier.beans;

public class Player {

	@Override
	public String toString() {
		return name + "," + value + "," + stats;
	}
	public String name;
	private int value;
	public Stats stats;
	private boolean isYellow = false;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Stats getStats() {
		return stats;
	}
	public void setStats(Stats stats) {
		this.stats = stats;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public boolean isYellow() {
		return isYellow;
	}
	public void setYellow(boolean isYellow) {
		this.isYellow = isYellow;
	}
	
}
