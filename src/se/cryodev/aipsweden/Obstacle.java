package se.cryodev.aipsweden;

public class Obstacle {

	private int number;
	private String name;
	private WGS84Coordinates coordinates;
	private int feetHeight;
	private int feetElevation;
	private LightCharacter light;
	private Obstacle.Type type;

	public Obstacle(String text) {
		// Get the text and parse it to create the object
	}
	
	public enum Type {
		WINDTURB, CHIMNEY, BRGPYLON, MAST, BUILDING, PYLON, TOWER, POWERPYLON
	}
}
