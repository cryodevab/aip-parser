package dev.cryo.aip.model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Obstacle {

	protected int codeId;
	protected String codeType;
	protected String txtName;
	protected String codeLgt;
	protected String codeMarking;
	protected String txtDescrLgt;
	protected String txtDescrMarking;
	protected WGS84Coordinates coordinates;
	protected int valGeoAccuracy;
	protected String uomGeoAccuracy;
	protected int valElev;
	protected int valElevAccuracy;
	protected int valHgt;
	protected String codeHgtAccuracy;
	protected String uomDistVer;
	protected int valRadius;
	protected String uomRadius;
	protected String codeGroupId;
	protected String txtGroupName;
	protected String codeLinkedToId;
	protected String codeLinkType;
	protected String datetimeValidWef;
	protected String datetimeValidTil;
	protected String txtRmk;
	protected String source;
	
	// Get a line just a specified in the OFMX Obstacle CSV file 
	public String getOFMline() throws IOException {
		
		// Escape all CSV strings
		// https://github.com/openflightmaps/ofmx/wiki/OFMX-CSV
        Field[] fields = Obstacle.class.getDeclaredFields();
        for (Field field : fields) {
        	if (field.getType().getSimpleName().equals("String")) {
        		try {
        			// Replace all <"> with <"">
        			field.set(this, ((String) field.get(this)).replaceAll("\"", "\"\""));
        			
        			// Replace multiple spaces with single ones
        			field.set(this, ((String) field.get(this)).replaceAll("\\s+", " "));
        			
        			// Replace < / > with </>
        			field.set(this, ((String) field.get(this)).replaceAll(" / ", "/"));
        			
        			// Replace < /> with </>
        			field.set(this, ((String) field.get(this)).replaceAll(" /", "/"));
        			
        			// Replace </ > with </>
        			field.set(this, ((String) field.get(this)).replaceAll("/ ", "/"));
        			
        		} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IOException(e.getLocalizedMessage());
				}
        	}
        }
		
		return 
				"\"" +
				codeId + "\",\"" + 
				codeType + "\",\"" + 
				txtName + "\",\"" + 
				codeLgt + "\",\"" + 
				codeMarking + "\",\"" + 
				txtDescrLgt +  "\",\"" +
				txtDescrMarking + "\",\"" + 
				coordinates.getLat() + "\",\"" + 
				coordinates.getLon() + "\",\"" + 
				valGeoAccuracy + "\",\"" + 
				uomGeoAccuracy + "\",\"" + 
				valElev + "\",\"" + 
				valElevAccuracy + "\",\"" + 
				valHgt + "\",\"" + 
				codeHgtAccuracy + "\",\"" + 
				uomDistVer + "\",\"" + 
				valRadius + "\",\"" + 
				uomRadius + "\",\"" + 
				codeGroupId + "\",\"" + 
				txtGroupName + "\",\"" + 
				codeLinkedToId + "\",\"" + 
				codeLinkType + "\",\"" + 
				datetimeValidWef + "\",\"" + 
				datetimeValidTil + "\",\"" + 
				txtRmk + "\",\"" + 
				source + "\"";
	}
	
	// Seriously Java?
	protected String capitalize(String input) {
		if (input == null || input.length() == 0)
			return "";
		
		return input.substring(0,1).toUpperCase() + input.substring(1, input.length()).toLowerCase();
	}
	
	// Convert time (horrible APIs require horrible solutions)
	protected LocalDate parseWef(String wef) {
		String[] months = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		for (int i = 0; i <= 11; i++) {
			if (wef.contains(months[i])) {
				wef = wef.replaceFirst(months[i], i + 1 + "");
				break;
			}
		}
		return LocalDate.parse(wef, DateTimeFormatter.ofPattern("d M yyyy"));
	}
}
