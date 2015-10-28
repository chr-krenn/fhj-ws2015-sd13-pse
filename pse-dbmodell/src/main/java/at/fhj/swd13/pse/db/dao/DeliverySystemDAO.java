package at.fhj.swd13.pse.db.dao;

import at.fhj.swd13.pse.db.entity.DeliverySystem;

public interface DeliverySystemDAO {

	String SYSTEM_PSE = "pse_system";
	String SYSTEM_SMS = "sms";
	String SYSTEM_EMAIL = "email";

	/**
	 * get the delivery system identified with this token
	 * 
	 * @param token token of the delivery system
	 * 
	 * @return instance of a delivery system or null
	 */
	DeliverySystem get(String token);

	/**
	 * get the delivery system for the pse (the web-application) itself
	 * 
	 * @return instance of a delivery system or null
	 */
	DeliverySystem getPseService();

}