package at.fhj.swd13.pse.test.gui.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CommunitiesPage {
	
	protected WebDriver driver;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver the web driver.
	 */
	public CommunitiesPage (WebDriver driver) {
		this.driver = driver;
	}
	
	/**
	 * Get title of Communities page
	 * @return Title String
	 */
	public String getTitle() {
		return driver.findElement(By.xpath(".//*[@id='communitySearch']/h1")).getText();
	}
	
	
}
