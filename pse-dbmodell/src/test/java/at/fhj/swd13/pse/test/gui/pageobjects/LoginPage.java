/**
 * 
 */
package at.fhj.swd13.pse.test.gui.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author florian.genser
 *
 */
public class LoginPage {

	protected WebDriver driver;
	
	protected String baseUrl;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver - the web driver.
	 * @param baseUrl - the baseUrl of the app
	 */
	public LoginPage(WebDriver driver, String baseUrl) {
		this.driver = driver;
		this.baseUrl = baseUrl;
	}
	
	/**
	 * Logs in and returns the HomePage
	 * 
	 * @return HomePage PageObject
	 */
	public HomePage login(String username, String password) {
		
		driver.get(baseUrl + "/index.jsf");
		driver.findElement(By.linkText("einloggen!")).click();
		driver.findElement(By.id("loginform:username")).clear();
		driver.findElement(By.id("loginform:username")).sendKeys(username);
		driver.findElement(By.id("loginform:password")).clear();
		driver.findElement(By.id("loginform:password")).sendKeys(password);
		driver.findElement(By.id("loginform:loginbutton")).click();
		
	    return new HomePage(driver);
	}
}
