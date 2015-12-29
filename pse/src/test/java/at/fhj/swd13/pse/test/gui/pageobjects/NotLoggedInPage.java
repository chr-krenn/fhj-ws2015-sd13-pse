/**
 * 
 */
package at.fhj.swd13.pse.test.gui.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author stefan.haring
 *
 */
public class NotLoggedInPage {

	protected WebDriver driver;

	/**
	 * Constructor taking web driver as parameter.
	 * 
	 * @param driver
	 *            - the web driver.
	 */
	public NotLoggedInPage(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Checks if the Doors of Durin Label is present
	 * 
	 * @return boolean
	 */
	public boolean isDoorsOfDurinLabelPresent() {
		return (driver.findElements(By.linkText("Doors of Durin")).size() == 1);
	}
}
