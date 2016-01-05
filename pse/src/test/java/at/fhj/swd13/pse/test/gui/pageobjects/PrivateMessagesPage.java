package at.fhj.swd13.pse.test.gui.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * 
 * @author Manuel Zametter
 *
 */
public class PrivateMessagesPage {

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
	public PrivateMessagesPage(WebDriver driver, String baseUrl) {
		this.driver = driver;
		this.baseUrl = baseUrl;
	}
	
	/**
	 * Returns the WebElement representing the latest received message.
	 * 
	 * @return The WebElement representing the latest received message
	 */
	private WebElement getLatestMessageElement() {
		WebElement firstMessage = driver.findElement(By.cssSelector("ul.ui-datascroller-list")).findElement(By.xpath("li[1]"));
		return firstMessage;
	}

	/**
	 * Returns the text of the latest message.
	 * 
	 * @return the text of the latest message
	 */
	public String getLatestMessageText() {
		WebElement lastMessage = getLatestMessageElement();
		return lastMessage.findElement(By.xpath("table/tbody/tr[2]/td/div/table/tbody/tr/td/table/tbody/tr[2]/td")).getText();
	}

	/**
	 * Returns the title of the latest message.
	 * 
	 * @return the title of the latest message
	 */
	public String getLatestMessageTitle() {
		WebElement lastMessage = getLatestMessageElement();
		return lastMessage.findElement(By.xpath("table/tbody/tr[2]/td/div/table/tbody/tr/td/table/tbody/tr[1]/td/span")).getText();
	}

	/**
	 * Returns the author of the latest message.
	 * 
	 * @return the author of the latest message.
	 */
	public String getLastMessageAuthor() {
		WebElement lastMessage = getLatestMessageElement();
		return lastMessage.findElement(By.xpath("table/tbody/tr[1]/td/table/tbody/tr/td[2]/span")).getText();
	}

}
