package at.fhj.swd13.pse.test.gui.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ServicesPage {
	
	protected WebDriver driver;
	protected String baseUrl;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver
	 *            - the web driver.
	 * @param baseUrl
	 *            - the baseUrl of the app
	 */
	public ServicesPage (WebDriver driver, String baseUrl) {
		this.baseUrl = baseUrl;
		this.driver = driver;
	}
	
	/**
	 * Get content of Services page
	 * 
	 * @return content string
	 */
	public String getContent() {
		return driver.findElement(By.id("j_idt37")).getText();
	}
	
	/**
	 * Get HomePage
	 * 
	 * @return HomePage PageObject
	 */
	public HomePage openHomePage() {
		driver.findElement(By.id("j_idt8:j_idt9")).click();
		return new HomePage(driver, baseUrl);
	}
	
}
