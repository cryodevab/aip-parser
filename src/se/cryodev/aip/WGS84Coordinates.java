package se.cryodev.aip;

public class WGS84Coordinates {
	/**
	 * Positive is N or E and negative is S or W.
	 */
	private float lat, lon;
	
	public WGS84Coordinates(float lat, float lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public float getLat() {
		return lat;
	}
	
	public float getLon() {
		return lon;
	}
	
	public String toString() {
		return lat + " " + lon;
	}
}
