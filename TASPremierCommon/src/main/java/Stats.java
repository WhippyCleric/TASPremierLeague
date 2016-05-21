import java.util.List;

public class Stats {

	

	private int shooting;
	private int speed;
	private int tackling;
	private int age;
	private int passing;
	private int goalKeeping;
	private int leadership;
	private int setPieces;
	private int dirtyness;

	private List<Position> positions;

	public void setShooting(int shooting) {
		this.shooting = shooting;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public void setTackling(int tackling) {
		this.tackling = tackling;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setPassing(int passing) {
		this.passing = passing;
	}
	public void setGoalKeeping(int goalKeeping) {
		this.goalKeeping = goalKeeping;
	}
	public void setLeadership(int leadership) {
		this.leadership = leadership;
	}
	public void setSetPieces(int setPieces) {
		this.setPieces = setPieces;
	}
	public void setDirtyness(int dirtyness) {
		this.dirtyness = dirtyness;
	}
	public int getShooting() {
		return shooting;
	}
	public int getSpeed() {
		return speed;
	}
	public int getTackling() {
		return tackling;
	}
	public int getAge() {
		return age;
	}
	public int getPassing() {
		return passing;
	}
	@Override
	public String toString() {
		StringBuilder stringRep = new StringBuilder();
		for (Position position : positions) {
			stringRep.append(position.toString());
			stringRep.append(" ");
		}
		stringRep.append("," + age + "," + shooting + "," + speed + "," + passing + "," + tackling + "," + leadership + "," + setPieces + "," + dirtyness + "," + goalKeeping);
		return stringRep.toString();
	}
	public int getGoalKeeping() {
		return goalKeeping;
	}
	public int getLeadership() {
		return leadership;
	}
	public int getSetPieces() {
		return setPieces;
	}
	public int getDirtyness() {
		return dirtyness;
	}

	public List<Position> getPositions() {
		return positions;
	}
	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}


}
