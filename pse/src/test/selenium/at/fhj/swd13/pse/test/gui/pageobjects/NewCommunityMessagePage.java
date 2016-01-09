package at.fhj.swd13.pse.test.gui.pageobjects;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class NewCommunityMessagePage {
	protected WebDriver driver;

	/**
	 * Constructor taking web driver as parameter.
	 * 
	 * @param driver
	 *            the web driver.
	 */
	public NewCommunityMessagePage(WebDriver driver) {
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
	public void save(){
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
	



}
