package at.fhj.swd13.pse.test.gui.pageobjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import at.fhj.swd13.pse.test.util.SleepUtil;

public class NewMessagePage {

	protected WebDriver driver;

	/**
	 * Constructor taking web driver as parameter.
	 * 
	 * @param driver
	 *            the web driver.
	 */
	public NewMessagePage(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Get header line of new message page
	 * 
	 * @return Header line as String
	 */
	public String getHeader() {
		return driver.findElement(By.xpath(".//*[@id='j_idt39:j_idt40']/tbody/tr[1]/td")).getText();
	}

	/**
	 * Press Save button
	 */
	public void save() {
		driver.findElement(By.id("j_idt39:j_idt81")).click();
	}

	/**
	 * Send message with indicated title and text
	 * 
	 * @param title
	 * @param text
	 */
	public void sendBasicMessage(String title, String text) {
		enterBasicMessageData(title, text);
		save();
	}

	/**
	 * Send message with indicated title, text and keyword
	 * 
	 * @param title
	 * @param text
	 * @param keyword
	 */
	public void sendMessageWithKeyword(String title, String text, String keyword) {
		enterBasicMessageData(title, text);
		addKeyword(keyword);
		save();
	}

	/**
	 * Enter title and text
	 * 
	 * @param title
	 * @param text
	 */
	public void enterBasicMessageData(String title, String text) {
		WebElement titleField = driver.findElement(By.id("j_idt39:headline"));
		titleField.clear();
		titleField.sendKeys(title);
		String window = driver.getWindowHandle();
		WebElement iframe = driver.findElement(By.tagName("iframe"));
		driver.switchTo().frame(iframe);
		WebElement iframeBody = driver.findElement(By.xpath("html/body"));
		
		// As the editor does not use a textfield but the body of a iframe, we are not able to call clear()
		// so we select the existing text and replace it with the new text.
		iframeBody.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		iframeBody.sendKeys(text);
		driver.switchTo().window(window);
	}

	/**
	 * Add keyword
	 * 
	 * @param keyword
	 * @return number of keywords
	 */
	public int addKeyword(String keyword) {
		WebElement tagInput = driver.findElement(By.id("j_idt39:messageTags_input"));
		tagInput.sendKeys(keyword);
		SleepUtil.sleep(500);
		driver.findElement(By.xpath(".//*[@id='j_idt39:messageTags_panel']/ul/li")).click();
		return driver.findElements(By.xpath(".//*[@id='j_idt39:messageTags']/ul/li")).size() - 1;
	}
	
	/**
	 * Add community
	 * 
	 * @param community
	 * @return number of communities
	 */
	public int addCommunity(String community) {
		WebElement tagInput = driver.findElement(By.id("j_idt39:targetCommunities_input"));
		tagInput.sendKeys(community);
		SleepUtil.sleep(500);
		driver.findElement(By.xpath(".//*[@id='j_idt39:targetCommunities_panel']/ul/li")).click();
		return driver.findElements(By.xpath(".//*[@id='j_idt39:targetCommunities']/ul/li")).size() - 1;
	}

	/**
	 * Returns the title of the Message
	 * 
	 * @return Title of the message
	 */
	public String getTitle(){
		WebElement titleField = driver.findElement(By.id("j_idt39:headline"));
		return titleField.getAttribute("value");
	}
	
	/**
	 * Returns the text of the message
	 * 
	 * @return Text of the message
	 */
	public String getText(){
		String window = driver.getWindowHandle();
		WebElement iframe = driver.findElement(By.tagName("iframe"));
		driver.switchTo().frame(iframe);
		WebElement iframeBody = driver.findElement(By.xpath("html/body"));
		String text = iframeBody.getText();
		driver.switchTo().window(window);
		return text;
	}
	
	/**
	 * Get WebElements for keywords
	 * 
	 * @return List of WebElements
	 */
	private List<WebElement> getKeywordWebElements() {
		return driver.findElements(By.xpath(".//*[@id='j_idt39:messageTags']/ul/li"));
	}

	/**
	 * Get String list of keywords
	 * 
	 * @return list of keywords as strings
	 */
	public List<String> getKeywords() {
		List<String> keywords = new ArrayList<>();
		for (WebElement e : getKeywordWebElements()) {
			keywords.add(e.getText());
		}
		return keywords;
	}
}
