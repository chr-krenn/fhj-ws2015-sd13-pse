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
	 * 
	 * @param driver
	 *            - the web driver.
	 * @param baseUrl
	 *            - the baseUrl of the app
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

		loginInternal(username, password);

		return new HomePage(driver, baseUrl);
	}
	
	/**
	 * Logs in with wrong password and returns the NotLoggedIn
	 * 
	 * @return NotLoggedIn PageObject
	 */
	public NotLoggedInPage loginWithWrongCredentials(String username, String password) {

		loginInternal(username, password);

		return new NotLoggedInPage(driver);
	}
	
	private void loginInternal(String username, String password)
	{
		driver.get(baseUrl + "/index.jsf");
		driver.findElement(By.linkText("einloggen!")).click();
		driver.findElement(By.id("loginform:username")).clear();
		driver.findElement(By.id("loginform:username")).sendKeys(username);
		driver.findElement(By.id("loginform:password")).clear();
		driver.findElement(By.id("loginform:password")).sendKeys(password);
		driver.findElement(By.id("loginform:loginbutton")).click();		
	}
	
	/**
	 * Opens the page where the user can reset the password
	 * 
	 * @return ResetPasswordPage PageObject
	 */
	public ResetPasswordPage forgotPassword() {
		driver.get(baseUrl + "/index.jsf");
		driver.findElement(By.linkText("einloggen!")).click();
		driver.findElement(By.id("loginform:resetPasswordLink")).click();

		return new ResetPasswordPage(driver);
	}
	
	/**
	 * Checks if the einloggen link is present
	 * 
	 * @return boolean
	 */
	public boolean isEinloggenLinkPresent()
	{
		return (driver.findElement(By.linkText("einloggen!")) != null);
	}

}
