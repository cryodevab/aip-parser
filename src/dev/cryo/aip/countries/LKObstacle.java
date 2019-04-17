package dev.cryo.aip.countries;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

import dev.cryo.aip.model.Obstacle;
import dev.cryo.aip.model.WGS84Coordinates;

public class LKObstacle extends Obstacle {

	public LKObstacle(
			String obs_id,
			String aip_id,
			String obs_txt_name, 
			String obs_txt_descr_type, 
			String obs_val_elev, 
			String obs_val_hgt,
			String obs_uom_dist_ver, 
			String obs_txt_ver_datum, 
			String obs_geo_lat, 
			String obs_geo_long,
			String obs_code_datum,
			String obs_txt_descr_marking,
			String obs_txt_descr_lgt,
			String obs_code_lgt,
			String obs_txt_rmk,
			HashMap<Integer, String> groupUUIDs
			) throws NumberFormatException {
		
		// Check if the coordinate system datum is supported
		if (!obs_code_datum.toUpperCase().equals("WGE")) {
			throw new NumberFormatException("Unsupported obs_code_datum CSV column; expecting WGE.");
		}
		
		// Check if the unit of measurement of vertical distance is supported
		String obsUomDistVer = obs_uom_dist_ver.toUpperCase();
		if (obsUomDistVer.equals("M")) {
			uomDistVer = "M";
		} else if (obsUomDistVer.equals("FT") || obsUomDistVer.equals("FEET")) {
			uomDistVer = "FT";
		} else {
			throw new NumberFormatException("Unsupported obs_uom_dist_ver CSV column; expecting M or FT.");
		}
		
		codeId = Integer.valueOf(obs_id);
		txtName = obs_txt_name.toUpperCase();
		codeLgt = obs_code_lgt.equals("Y") ? "Y" : "N";
		codeMarking = obs_txt_descr_marking.isEmpty() ? "N" : "Y";
		txtDescrLgt = capitalize(obs_txt_descr_lgt);
		txtDescrMarking = capitalize(obs_txt_descr_marking);
		coordinates = new WGS84Coordinates(obs_geo_lat, obs_geo_long);
		valGeoAccuracy = 0;
		uomGeoAccuracy = "M";
		valElev = Integer.valueOf(obs_val_elev);
		valElevAccuracy = 0;
		valHgt = Integer.valueOf(obs_val_hgt);
		codeHgtAccuracy = "Y";
		valRadius = 0;
		uomRadius = "M";
		txtRmk = capitalize(obs_txt_descr_type);
		
		// Group stuff (transmission lines supported)
		if (!aip_id.matches("^[0-9]+$")) {
			
			// Currently we only support groups with 3 obstacles
			char lastChar = aip_id.toLowerCase().charAt(aip_id.length() - 1);
			if (lastChar < 'a' || lastChar > 'c') {
				throw new NumberFormatException("Group obstacles with more than 3 members are not supported.");
			}
			
			// UUID generation
			int groupId = Integer.valueOf(aip_id.replaceAll("\\D",""));
			String uuid = (String) groupUUIDs.get(groupId);
			if (uuid == null) {
				uuid = UUID.randomUUID().toString();
				groupUUIDs.put(groupId, uuid);
			}
			codeGroupId = uuid;
			
			// Name of group
			txtGroupName = "Group ID " + groupId;
			
			// This obstacle if physically connected with
			codeLinkedToId = (lastChar == 'c') ? "" : String.valueOf(codeId + 1);
			
			// The type of connection
			if (obs_txt_descr_type.toLowerCase().matches("(.*pole.*|.*transmission\\sline.*)")) {
				codeLinkType = "CABLE";
			} else {
				codeLinkType = "OTHER";
			}
		} else {
			codeGroupId = "";
			txtGroupName = "";
			codeLinkedToId = "";
			codeLinkType = "";
		}
		
		// No WEF is provided in the CSV, using current date.
		LocalDate fromDate = LocalDate.now();
		LocalDate toDate = fromDate.plusYears(2);
		datetimeValidWef = fromDate.toString() + "T00:00:00Z";
		datetimeValidTil = toDate.toString() + "T00:00:00Z";
		
		// The date does not match AIRAC date since it is not provided in the CSN file
		source = "LK|ENR|5.4|" + fromDate.toString() + "|CSV";
		
		// Search for the type of obstacle
		obs_txt_descr_type = obs_txt_descr_type.toLowerCase();
		if (obs_txt_descr_type.matches("(.*transmitter.*|.*antenna.*)")) { // OK
			codeType = "ANTENNA";
		} else if (obs_txt_descr_type.matches(".*bridge.*")) { // No samples
			codeType = "BRIDGE";
		} else if (obs_txt_descr_type.matches(".*building.*")) { // OK
			codeType = "BUILDING";
		} else if (obs_txt_descr_type.matches(".*chimney.*")) { // OK
			codeType = "CHIMNEY";
		} else if (obs_txt_descr_type.matches(".*crane.*")) { // No samples
			codeType = "CRANE";
		} else if (obs_txt_descr_type.matches(".*mast.*")) { // No samples 
			codeType = "MAST";
		} else if (obs_txt_descr_type.matches("(.*tower.*|.*pole.*|.*transmission\\sline.*)")) { // OK
			codeType = "TOWER";
		} else if (obs_txt_descr_type.matches(".*tree.*")) { // No samples
			codeType = "TREE";
		} else if (obs_txt_descr_type.matches(".*wind.*")) { // OK 
			codeType = "WINDTURBINE";
		} else {
			codeType = "OTHER";
		}
		
	}

}
