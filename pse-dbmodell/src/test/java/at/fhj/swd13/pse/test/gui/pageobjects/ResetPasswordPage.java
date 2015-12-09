package at.fhj.swd13.pse.test.gui.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ResetPasswordPage {

	protected WebDriver driver;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver the web driver.
	 */
	public ResetPasswordPage(WebDriver driver) {
		this.driver = driver;		
	}
	
	
	/**
	 * Returns the text of the h1 - tags
	 * @return the text of the first h1 tag in the page
	 */
	public String getHeaderText() {
		WebElement header = driver.findElement(By.xpath("//html/body/div[2]/div/div/div/h1"));		
		return header.getText();		
	}
	
	
	/**
	 * Resets the password
	 * @param emailAddress E-Mail-Address of the user
	 */
	public void resetPassword(String emailAddress) {
		
		driver.findElement(By.id("resetPasswordForm:emailAddress")).clear();
		driver.findElement(By.id("resetPasswordForm:emailAddress")).sendKeys(emailAddress);		
		driver.findElement(By.id("resetPasswordForm:resetPasswordButton")).click();	
	}
	
	/**
	 * Returns the status text, which is displayed when the user resets his password
	 * @return status text as string
	 */
	public String getStatusText() {
		WebElement statusElement = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/form/span/span"));
		return statusElement.getText();
	}
	
}
