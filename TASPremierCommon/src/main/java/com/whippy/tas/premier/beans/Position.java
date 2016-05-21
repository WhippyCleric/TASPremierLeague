package com.whippy.tas.premier.beans;
public enum Position{
		FORWARD("Forward"),
		MIDFIELD("Midfield"),
		DEFENSE("Defense"),
		GOALKEEPER("Goal Keeper");

		private final String text;

		/**
		 * @param text
		 */
		private Position(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}