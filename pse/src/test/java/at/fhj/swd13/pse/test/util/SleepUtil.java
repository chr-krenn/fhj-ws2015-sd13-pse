/**
 * 
 */
package at.fhj.swd13.pse.test.util;


/**
 * @author florian.genser
 *
 */
public class SleepUtil {

	/**
	 * Sleeps for x millis on the current thread.<br>
	 * Equivalent to {@link Thread#sleep(long)}
	 * @param millis
	 */
	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
