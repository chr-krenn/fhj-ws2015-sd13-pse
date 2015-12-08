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
	 * Set active state
	 *
	 * @param active state
	 */
	public void setActive(boolean active) {
		WebElement checkbox = driver.findElement(By.xpath("//div[@id='userForm:active']/div[2]/span"));
		boolean checked = checkbox.getAttribute("class").contains("ui-icon-check");
		
		if (active ^ checked) {
			driver.findElement(By.xpath("//div[@id='userForm:active']/div[2]/span")).click();
		}
	}

	/**
	 * Get active state
	 * 
	 * @return active state
	 */
	public boolean getActive() {
		WebElement checkbox = driver.findElement(By.xpath("//div[@id='userForm:active']/div[2]/span"));
		return checkbox.getAttribute("class").contains("ui-icon-check");
	}
	
	/**
	 * Set active state
	 *
	 * @param active state
	 */
	public void setLoginAllowed(boolean loginAllowed) {
		WebElement checkbox = driver.findElement(By.xpath("//div[@id='userForm:loginallowed']/div[2]/span"));
		boolean checked = checkbox.getAttribute("class").contains("ui-icon-check");
		
		if (loginAllowed ^ checked) {
			driver.findElement(By.xpath("//div[@id='userForm:loginallowed']/div[2]/span")).click();
		}
	}

	/**
	 * Get login allowed state
	 * 
	 * @return active state
	 */
	public boolean getLoginAllowed() {
		WebElement checkbox = driver.findElement(By.xpath("//div[@id='userForm:loginallowed']/div[2]/span"));
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

	/**
	 * Get user first name
	 * 
	 * @return user first name
	 */
	public String getUserFirstName() {
		WebElement input = driver.findElement(By.id("userForm:firstName"));
		return input.getAttribute("value");
	}

	/**
	 * Get user email address
	 * 
	 * @return user email address
	 */
	public String getUserEmailAddress() {
		WebElement input = driver.findElement(By.id("userForm:emailAddress"));
		return input.getAttribute("value");
	}

	/**
	 * Get user department
	 * 
	 * @return user department
	 */
	public String getUserDepartment() {
		WebElement input = driver.findElement(By.id("userForm:department"));
		return input.getAttribute("value");
	}
	
	/**
	 * Get user building
	 * 
	 * @return user building
	 */
	public String getUserBuilding() {
		WebElement input = driver.findElement(By.id("userForm:building"));
		return input.getAttribute("value");
	}

	/**
	 * Get user floor
	 * 
	 * @return user floor
	 */
	public String getUserFloor() {
		WebElement input = driver.findElement(By.id("userForm:floor"));
		return input.getAttribute("value");
	}

	/**
	 * Get user room number
	 * 
	 * @return user room number
	 */
	public String getUserRoomNumber() {
		WebElement input = driver.findElement(By.id("userForm:roomnumber"));
		return input.getAttribute("value");
	}
	
	/**
	 * Get user phone number mobile
	 * 
	 * @return user phone number mobil
	 */
	public String getUserPhoneNumberMobile() {
		WebElement input = driver.findElement(By.id("userForm:phoneNumberMobile"));
		return input.getAttribute("value");
	}
}
