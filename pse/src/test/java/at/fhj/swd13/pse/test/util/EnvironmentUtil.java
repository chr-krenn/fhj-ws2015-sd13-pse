/**
 * 
 */
package at.fhj.swd13.pse.test.util;


/**
 * @author florian.genser
 *
 */
public class EnvironmentUtil {

	public static String reolvePort() {
		
		final String pseEnv = System.getenv("PSE_ENV");

		final String pseEnvSystem = System.getProperty("PSE_ENV", pseEnv);
		if (pseEnvSystem == null || !pseEnvSystem.equals("CI")) {
			return "8080";
		}
		return "8000";
		
	}
}
