package at.fhj.swd13.pse.test.gui.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UserList {
	
	protected WebDriver driver;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver the web driver.
	 */
	public UserList (WebDriver driver) {
		this.driver = driver;
	}
	
	/**
	 *  Get User WebElements from user list
	 * 
	 * @return List with WebElements
	 */
	public List<WebElement> getUsers() {
		return driver.findElements(By.className("ui-datascroller-item"));
	}

	public UserPage openUserPage(int index) {
		List<WebElement> elements = driver.findElements(By.className("ui-datascroller-item"));
		elements.get(index).findElement(By.cssSelector("a > span")).click();
		return new UserPage(driver);
	}

}
