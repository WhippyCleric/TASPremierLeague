import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;




public class Generator {

	
	
	private static final String FIRST_NAMES = "first.txt";
	private static final String LAST_NAMES = "last.txt";
	private static final int NUMBER_OF_PLAYERS = 1000;
	private static final int MIN_AGE = 16;
	private static final int MAX_AGE = 38;

	public static void main(String args[]) throws IOException{
		Random rand = new Random();
		List<String> firstNames = Utils.getNames(FIRST_NAMES);
		List<String> lastNames = Utils.getNames(LAST_NAMES);
		System.out.println("Name,Value,Positions,Age,Shooting,Speed,Passing,Tackling,Leadership,Set Pieces,Dirtyness,Goal Keeping");
		for(int i=0;i<NUMBER_OF_PLAYERS;i++){
			Player player = new Player();
			List<Position> positions = generatePosition(rand);
			int age = generateAge(rand);
			Stats stats = createStats(age, positions, rand);
			player.setStats(stats);
			player.setName(firstNames.get(rand.nextInt(firstNames.size())) + " " + lastNames.get(rand.nextInt(lastNames.size())));
			player.setValue((int) (Math.floor(generateValue(stats))));
			System.out.println(player);
		}
		
	}
	
	private static double generateValue(Stats stats){
		if(stats.getPositions().contains(Position.GOALKEEPER)){
			int goodAttributes = (stats.getGoalKeeping() * 4) + stats.getLeadership() + stats.getSetPieces() ;
			int badAttributes = (stats.getAge() * 2) + (stats.getDirtyness());
			double value = goodAttributes - badAttributes;
			value = value * 1000;
			return value;
		}else{			
			int goodAttributes = stats.getGoalKeeping() + stats.getLeadership() + stats.getPassing() + stats.getSetPieces() + stats.getShooting() + stats.getSpeed()+ stats.getTackling();
			int badAttributes = (stats.getAge() * 3) + (stats.getDirtyness());
			double value = goodAttributes - badAttributes;
			if(stats.getPositions().size()>1){
				value = value * 1.2;
			}
			value = value * 1000;
			if(value < 4000){
				value = 4000;
			}
			return value;
		}
	}

	
	private static Stats createStats(int age, List<Position> positions, Random rand){
		Stats stats = new Stats();
		stats = setGoalKeepingStat(positions, rand, stats);
		stats = setLeadershipStat(age, rand, stats);
		stats = setSpeedStat(age, rand, stats);
		stats = setPassingStat(positions, rand, stats);
		stats = setTacklingStat(positions, rand, stats);
		stats = setShootingStat(positions, rand, stats);
		stats.setDirtyness(rand.nextInt((100) + 1));
		stats.setLeadership(rand.nextInt((100) + 1));
		stats.setSetPieces(rand.nextInt((100) + 1));
		stats.setPositions(positions);
		stats.setAge(age);
		return stats;
	}
	
	
	private static Stats setPassingStat(List<Position> positions, Random rand, Stats stats) {
		if(positions.contains(Position.MIDFIELD)){
			//Midfielders must have a passing stat of at least 40
			stats.setPassing(rand.nextInt((100 - 40) + 1) + 40);
		}else{
			stats.setPassing(rand.nextInt((100) + 1));
		}
		return stats;
	}
	
	private static Stats setShootingStat(List<Position> positions, Random rand, Stats stats) {
		if(positions.contains(Position.FORWARD)){
			//Defenders must have a shooting stat of at least 40
			stats.setShooting(rand.nextInt((100 - 40) + 1) + 40);
		}else if(positions.contains(Position.MIDFIELD)){
			//Midfielders must have a shooting stat of at least 25
			stats.setShooting(rand.nextInt((100 - 25) + 1) + 25);
		}else{
			stats.setShooting(rand.nextInt((80) + 1));
		}
		return stats;
	}
	
	private static Stats setTacklingStat(List<Position> positions, Random rand, Stats stats) {
		if(positions.contains(Position.DEFENSE)){
			//Defenders must have a tackling stat of at least 40
			stats.setTackling(rand.nextInt((100 - 40) + 1) + 40);
		}else if(positions.contains(Position.MIDFIELD)){
			//Midfielders must have a tackling stat of at least 25
			stats.setTackling(rand.nextInt((100 - 25) + 1) + 25);
		}else{
			stats.setTackling(rand.nextInt((100) + 1));
		}
		return stats;
	}
	
	private static Stats setSpeedStat(int age, Random rand, Stats stats) {
		if(age > 33){
			//Players over can only have max speed of 70
			stats.setSpeed(rand.nextInt((70 - 10) + 1) + 10);
		}else if (age > 28){
			//Players over can only have max speed of 85
			stats.setSpeed(rand.nextInt((85 - 10) + 1) + 10);
		}else{
			//Player under 28 can have max speed of 100
			stats.setSpeed(rand.nextInt((100-10) + 1)+10);
		}
		return stats;
	}
	
	private static Stats setLeadershipStat(int age, Random rand, Stats stats) {
		if(age < 20){
			//Young players can't exceed leadership of 60
			stats.setLeadership(rand.nextInt((60) + 1));
		}else if (age < 25){
			//Player between 20 and 25 can have leadership up to 85
			stats.setLeadership(rand.nextInt((85) + 1));
		}else{
			//Player over 25 can have up to 100 leadership
			stats.setLeadership(rand.nextInt((100) + 1));
		}
		return stats;
	}

	private static Stats setGoalKeepingStat(List<Position> positions, Random rand, Stats stats) {
		if(positions.contains(Position.GOALKEEPER)){
			//Goal Keepers goal keeping stat must be more than 50
			stats.setGoalKeeping(rand.nextInt((100 - 50) + 1) + 50);
		}else{
			//Non Goal Keepers max is 50
			stats.setGoalKeeping(rand.nextInt((50) + 1));
		}
		return stats;
	}

	private static int generateAge(Random rand) {
		return rand.nextInt((MAX_AGE - MIN_AGE) + 1) + MIN_AGE;
	}

	private static List<Position> generatePosition(Random rand) {
		List<Position> positions = new ArrayList<Position>(); 
		int position = rand.nextInt((88 - 0) + 1);
		if(position<=8){
			positions.add(Position.GOALKEEPER);
		}else if(position<28){
			positions.add(Position.DEFENSE);
		}else if(position<38){
			positions.add(Position.DEFENSE);
			positions.add(Position.MIDFIELD);
		}else if(position<58){
			positions.add(Position.MIDFIELD);
		}else if(position<68){
			positions.add(Position.MIDFIELD);
			positions.add(Position.FORWARD);
		}else{
			positions.add(Position.FORWARD);
		}
		return positions;
	}
	
}
