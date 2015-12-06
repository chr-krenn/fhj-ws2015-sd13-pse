package at.fhj.swd13.pse.test.gui.pageobjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
	
	protected WebDriver driver;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver the web driver.
	 */
	public HomePage (WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Checks whether section with header 'Activities' is present.  
	 * 
	 * @return true if Activities header is present.
	 */
	public Boolean isActivitiesStreamPresent(){
		String header = "Activities";
		String text = getElement(".//*[@id='j_idt58:j_idt59_header']/span").getText();	
		return header.equals(text);
	}
	
	/**
	 * Checks whether Details button for first activity is present.  
	 * 
	 * @return true if Details button is present.
	 */
	public Boolean isActivityDetailsButtonPresent(){
		return isActivityDetailsButtonPresent(0);
	}
	
	/**
	 * Checks whether Details button for indicated activity is present. 
	 * 
	 * @param messageNumber: index for activity
	 * @return true if Details button is present.
	 */
	public Boolean isActivityDetailsButtonPresent(int messageNumber){
		String buttonlabel = "Details";
		String text = getDetailsButton(messageNumber).getAttribute("value");
		return buttonlabel.equals(text);
	}
	
	/**
	 * Checks whether Details view for first activity displays correct data
	 * 
	 * @return true data in overview and detail view are equal.
	 */
	public Boolean isActivityStreamDetailViewCorrect(){
		return isActivityStreamDetailViewCorrect(0);
	}
	
	/**
	 * Checks whether Details view for indicated displays correct data.  
	 * 
	 * @param messageNumber: index for activity
	 * @return true data in overview and detail view are equal.
	 */
	public Boolean isActivityStreamDetailViewCorrect(int messageNumber){
		String author = getAuthor(messageNumber);
		LocalDateTime date = getDate(messageNumber);
		String header = author +", am " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) 
						+" um " +date.format(DateTimeFormatter.ofPattern("HH:mm"));
		String message = getElement(".//*[@id='j_idt58:j_idt60:0:j_idt62']/table[2]/tbody/tr/td/table/tbody/tr[2]/td").getText();
		getDetailsButton(messageNumber).click();
		
		//TODO: extract into MessageDetailViewPage
		String detailsHeader = getElement(".//*[@id='j_idt40:j_idt41_header']/span").getText();
		String detailsMessage = getElement(".//*[@id='j_idt40:j_idt51']/table/tbody/tr[2]/td").getText();
		
		if(header.equals(detailsHeader) && message.equals(detailsMessage)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check whether indicated activity is private
	 * 
	 * @param messageNumber: index of activity
	 * @return True if activity is private
	 */
	public Boolean isPrivateMessage(int messageNumber) {
		return getElement(getMessageHeaderLineXPath(messageNumber) +"td[1]").getText().equals("Privat");
	}
	
	/**
	 * Get Author
	 * 
	 * @param messageNumber: index of activity
	 * @return Author name as String
	 */
	public String getAuthor(int messageNumber) {
		if(isPrivateMessage(messageNumber)) {
			return getElement(getMessageHeaderLineXPath(messageNumber) +"td[3]/span").getText();
		} else {
			return getElement(getMessageHeaderLineXPath(messageNumber) +"td[4]/span").getText();
		}
	}
	
	/**
	 * Get Date
	 * 
	 * @param messageNumber: index of activity
	 * @return Date as LocalDateTime
	 */
	public LocalDateTime getDate(int messageNumber) {
		String dateString;
		if(isPrivateMessage(messageNumber)) {
			dateString = getElement(getMessageHeaderLineXPath(messageNumber) +"td[5]/span").getText();
		} else {
			dateString = getElement(getMessageHeaderLineXPath(messageNumber) +"td[6]/span").getText();
		}
		return parseDate(dateString);
	}
	
	public int getNumberOfLikes(int messageNumber) {
		String number = getElement(".//*[@id='j_idt58:j_idt60:" +messageNumber +":j_idt62']/table[3]/tbody/tr/td[2]").getText().trim();
		return Integer.parseInt(number);	
	}
	
	/**
	 * Click Like button if activity is not already liked by the current user
	 * 
	 * @param messageNumber: index of activity
	 * @return True if button was clicked
	 */
	public Boolean likeMessage(int messageNumber) {
		WebElement button = getLikeButton(messageNumber);
		if(button.getAttribute("value").equals("Like")) {
			button.click();
			return true;
		}
		return false;
	}
	
	/**
	 * Click "Like zurücknehmen" button if activity is liked by the current user
	 * 
	 * @param messageNumber: index of activity
	 * @return True if button was clicked
	 */
	public Boolean revertLike(int messageNumber) {
		WebElement button = getLikeButton(messageNumber);
		if(button.getAttribute("value").equals("Like zurücknehmen")) {
			button.click();
			return true;
		}
		return false;
	}
	
	
	public int clickLikeButton(int messageNumber) {
		if(likeMessage(messageNumber)) {
			return 1;
		} else if (revertLike(messageNumber)) {
			return -1;
		}
		return 0;
		
	}
	
	/**
	 * Get Like button WebElement for indicated activity
	 * 
	 * @param messageNumber: index of activity
	 * @return Like button as WebElement
	 */
	private WebElement getLikeButton(int messageNumber) {
		return getElement(".//*[@id='j_idt58:j_idt60:" +messageNumber +":j_idt62']/table[3]/tbody/tr/td[1]/input");
	}
	
	/**
	 * Get Details button WebElement for indicated activity
	 * 
	 * @param messageNumber: index of activity
	 * @return Details button as WebElement
	 */
	private WebElement getDetailsButton(int messageNumber) {
		if(isPrivateMessage(messageNumber)) {
			return getElement(getMessageHeaderLineXPath(messageNumber) +"td[6]/input");
		} else {
			return getElement(getMessageHeaderLineXPath(messageNumber) +"td[7]/input");
		}
	}
	
	/**
	 * Get xPath String for header line for indicated activity
	 * 
	 * @param messageNumber: index for activity
	 * @return
	 */
	private String getMessageHeaderLineXPath(int messageNumber) {
		return ".//*[@id='j_idt58:j_idt60:" +messageNumber +":j_idt62']/table[1]/tbody/tr/";
	}

	/**
	 * Get the WebElement for the given xPath
	 * 
	 * @param xPath
	 * @return WebElement
	 */
	private WebElement getElement(String xPath) {
		return driver.findElement(By.xpath(xPath));
	}
	
	/**
	 * Parse date from string with format dd.MM.yyyy HH:mm
	 * 
	 * @param dateString
	 * @return LocalDateTime
	 */
	private LocalDateTime parseDate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		LocalDateTime date = LocalDateTime.parse(dateString, formatter);
		return date;
	}
}
