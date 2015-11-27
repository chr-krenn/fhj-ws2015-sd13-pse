/**
 * 
 */
package at.fhj.swd13.pse.test.db;

import org.junit.Test;

import at.fhj.swd13.pse.test.base.SeleniumBaseTestCase;

/**
 * @author florian.genser
 *
 */
public class SeleniumSampleIT extends SeleniumBaseTestCase {

	@Test
	public void testTitle() throws Exception {
		login("florian.genser", "12345678");

		verifyEquals("PSE", driver.getTitle());

	}

	@Test
	public void testSelenium() throws Exception {
		login("florian.genser", "12345678");

		verifyTextById("florian.genser", "j_idt8:j_idt15_button");

	}

}
