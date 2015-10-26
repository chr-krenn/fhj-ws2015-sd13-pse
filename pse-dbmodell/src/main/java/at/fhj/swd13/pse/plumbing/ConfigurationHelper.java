package at.fhj.swd13.pse.plumbing;


/**
 * Helper class for configuration access
 */
public abstract class ConfigurationHelper {
	
	/**
	 * load a property from the current system config
	 * 
	 * @param propertyName name of the property to load
	 * 
	 * @param defaultValue value to return if property does not exist in config
	 * 
	 * @return the value from the config file or defaultValue if entry could not be found
	 */
	public static String saveSetProperty(final String propertyName, final String defaultValue) {

		if (System.getProperty(propertyName) != null) {

			final String newValue = System.getProperty(propertyName);

			return newValue;
		} else {
			return defaultValue;
		}
	}

	/**
	 * load a property from the current system config
	 * 
	 * @param propertyName name of the property to load
	 * 
	 * @param defaultValue value to return if property does not exist in config
	 * 
	 * @return the value from the config file or defaultValue if entry could not be found
	 */
	public static int saveSetPropertyInt(final String propertyName, final String defaultValue) {

		return Integer.parseInt(saveSetProperty(propertyName, defaultValue));
	}

}
