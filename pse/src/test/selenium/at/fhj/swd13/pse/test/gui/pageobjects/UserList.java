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
	 * Get title of user list
	 * 
	 * @return Title of data scroller
	 */
	public String getTitle() {
		return driver.findElement(By.xpath(".//*[@id='j_idt37:j_idt38']/div[1]")).getText();
	}
	
	/**
	 *  Get User WebElements from user list
	 * 
	 * @return List with WebElements
	 */
	public List<WebElement> getUsers() {
		return driver.findElements(By.className("ui-datascroller-item"));
	}

	/**
	 *  Forwards to the User Page for the list entry with the given index
	 * 
	 * @param index the list index of the entry 
	 * @return UserPage PageObject for the entry 
	 */
	public UserPage openUserPage(int index) {
		List<WebElement> elements = driver.findElements(By.className("ui-datascroller-item"));
		elements.get(index).findElement(By.cssSelector("a > span")).click();
		return new UserPage(driver);
	}
}
