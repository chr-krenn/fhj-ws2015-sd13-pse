package at.fhj.swd13.pse.test.gui.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UserPage {
	
	protected WebDriver driver;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver the web driver.
	 */
	public UserPage (WebDriver driver) {
		this.driver = driver;
	}

	
	/**
	 * Press Save button
	 */
	public void save() {
		driver.findElement(By.id("userForm:savebutton")).click();
	}


	/**
	 * Set out-of-office state
	 *
	 * @param out-of-office state
	 */
	public void setOutOfOffice(boolean outOfOffice) {
		WebElement checkbox = driver.findElement(By.xpath("//div[@id='userForm:outofoffice']/div[2]/span"));
		boolean checked = checkbox.getAttribute("class").contains("ui-icon-check");
		
		if (outOfOffice ^ checked) {
			driver.findElement(By.xpath("//div[@id='userForm:outofoffice']/div[2]/span")).click();
		}
	}

	/**
	 * Get out-of-office state
	 * 
	 * @return out-of-office state
	 */
	public boolean getOutOfOffice() {
		WebElement checkbox = driver.findElement(By.xpath("//div[@id='userForm:outofoffice']/div[2]/span"));
		return checkbox.getAttribute("class").contains("ui-icon-check");
	}

	/**
	 * Get user last name
	 * 
	 * @return user last name
	 */
	public String getUserLastName() {
		WebElement input = driver.findElement(By.id("userForm:lastName"));
		return input.getAttribute("value");
	}

}
