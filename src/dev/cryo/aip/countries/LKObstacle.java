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
		String uomDistVer = obs_uom_dist_ver.toUpperCase();
		if (uomDistVer.equals("M")) {
			this.uomDistVer = "M";
		} else if (uomDistVer.equals("FT") || uomDistVer.equals("FEET")) {
			this.uomDistVer = "FT";
		} else {
			throw new NumberFormatException("Unsupported obs_uom_dist_ver CSV column; expecting M or FT.");
		}
		
		this.codeId = Integer.valueOf(obs_id);
		this.txtName = obs_txt_name.toUpperCase();
		this.codeLgt = obs_code_lgt.equals("Y") ? "Y" : "N";
		this.codeMarking = obs_txt_descr_marking.isEmpty() ? "N" : "Y";
		this.txtDescrLgt = capitalize(obs_txt_descr_lgt);
		this.txtDescrMarking = capitalize(obs_txt_descr_marking);
		this.coordinates = new WGS84Coordinates(obs_geo_lat, obs_geo_long);
		this.valGeoAccuracy = 0;
		this.uomGeoAccuracy = "M";
		this.valElev = Integer.valueOf(obs_val_elev);
		this.valElevAccuracy = 0;
		this.valHgt = Integer.valueOf(obs_val_hgt);
		this.codeHgtAccuracy = "Y";
		this.valRadius = 0;
		this.uomRadius = "M";
		this.txtRmk = capitalize(obs_txt_descr_type);
		
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
			if (uuid == null) uuid = UUID.randomUUID().toString();
			groupUUIDs.put(groupId, uuid);
			this.codeGroupId = uuid;
			
			// Name of group
			this.txtGroupName = "Group ID " + groupId;
			
			// This obstacle if physically connected with
			this.codeLinkedToId = (lastChar == 'c') ? "" : String.valueOf(this.codeId + 1);
			
			// The type of connection
			if (obs_txt_descr_type.toLowerCase().matches("(.*pole.*|.*transmission\\sline.*)")) {
				this.codeLinkType = "CABLE";
			} else {
				this.codeLinkType = "OTHER";
			}
		} else {
			this.codeGroupId = "";
			this.txtGroupName = "";
			this.codeLinkedToId = "";
			this.codeLinkType = "";
		}
		
		// No WEF is provided in the CSV, using current date.
		LocalDate fromDate = LocalDate.now();
		LocalDate toDate = fromDate.plusYears(2);
		this.datetimeValidWef = fromDate.toString() + "T00:00:00Z";
		this.datetimeValidTil = toDate.toString() + "T00:00:00Z";
		
		// The date does not match AIRAC date since it is not provided in the CSN file
		this.source = "LK|ENR|5.4|" + fromDate.toString() + "|CSV";
		
		// Search for the type of obstacle
		obs_txt_descr_type = obs_txt_descr_type.toLowerCase();
		if (obs_txt_descr_type.matches("(.*transmitter.*|.*antenna.*)")) { // OK
			this.codeType = "ANTENNA";
		} else if (obs_txt_descr_type.matches(".*bridge.*")) { // No samples
			this.codeType = "BRIDGE";
		} else if (obs_txt_descr_type.matches(".*building.*")) { // OK
			this.codeType = "BUILDING";
		} else if (obs_txt_descr_type.matches(".*chimney.*")) { // OK
			this.codeType = "CHIMNEY";
		} else if (obs_txt_descr_type.matches(".*crane.*")) { // No samples
			this.codeType = "CRANE";
		} else if (obs_txt_descr_type.matches(".*mast.*")) { // No samples 
			this.codeType = "MAST";
		} else if (obs_txt_descr_type.matches("(.*tower.*|.*pole.*|.*transmission\\sline.*)")) { // OK
			this.codeType = "TOWER";
		} else if (obs_txt_descr_type.matches(".*tree.*")) { // No samples
			this.codeType = "TREE";
		} else if (obs_txt_descr_type.matches(".*wind.*")) { // OK 
			this.codeType = "WINDTURBINE";
		} else {
			this.codeType = "OTHER";
		}
		
	}

}
