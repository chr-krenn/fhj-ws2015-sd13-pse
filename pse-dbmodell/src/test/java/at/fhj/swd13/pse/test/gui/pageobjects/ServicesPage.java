package at.fhj.swd13.pse.test.gui.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ServicesPage {
	
	protected WebDriver driver;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver the web driver.
	 */
	public ServicesPage (WebDriver driver) {
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
		return new HomePage(driver);
	}
	
}
