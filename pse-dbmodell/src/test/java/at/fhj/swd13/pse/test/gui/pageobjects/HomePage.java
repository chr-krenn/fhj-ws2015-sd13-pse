package at.fhj.swd13.pse.test.gui.pageobjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
	
	protected WebDriver driver;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver the web driver.
	 */
	public HomePage (WebDriver driver) {
		this.driver = driver;
	}
	
	public void logout() {
		driver.findElement(By.xpath(".//*[@id='j_idt8:j_idt15_menuButton']")).click();
		driver.findElement(By.xpath(".//*[@id='j_idt8:j_idt15_menu']/ul/li[4]/a")).click();
		
		//wait
		(new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            @Override
			public Boolean apply(WebDriver d) {
            	return d.findElement(By.linkText("einloggen!")) != null;
            }
        });
	}

	/**
	 * Checks whether section with header 'Activities' is present.  
	 * 
	 * @return true if Activities header is present.
	 */
	public Boolean isActivitiesStreamPresent(){
		String header = "Activities";
		String text = getElement(".//*[@id='activityform:activitypanel_header']/span").getText();	
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
		String headline = getElement(".//div[starts-with(@id, 'activityform:activities:" + messageNumber + "')]/table[2]/tbody/tr/td/table/tbody/tr[1]/td").getText();
		String message = getElement(".//div[starts-with(@id, 'activityform:activities:" + messageNumber + "')]/table[2]/tbody/tr/td/table/tbody/tr[2]/td").getText();
		
		MessageDetailView detailView = openDetailView(messageNumber);

		String detailsHeader = detailView.getHeader();
		String detailsHeadline = detailView.getHeadline();
		String detailsMessage = detailView.getMessage();
		
		if(header.equals(detailsHeader) && headline.equals(detailsHeadline) && message.equals(detailsMessage)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Get detail view for first activity
	 * 
	 * @return MessageDetailView page object
	 */
	public MessageDetailView openDetailView() {
		return openDetailView(0);
	}
	
	/**
	 * Get detail view for indicated activity
	 * 
	 * @param messageNumber
	 * @return MessageDetailView page object
	 */
	public MessageDetailView openDetailView(int messageNumber) {
		getDetailsButton(messageNumber).click();
		return new MessageDetailView(driver);
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
	
	/**
	 * Get number of likes for indicated activity
	 * 
	 * @param messageNumber
	 * @return number of likes
	 */
	public int getNumberOfLikes(int messageNumber) {
		String number = getElement(".//div[starts-with(@id, 'activityform:activities:" + messageNumber + "')]/table[3]/tbody/tr/td[2]").getText().trim();
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
	
	/**
	 * Click "Like" or "Like zurücknehmen" button
	 * 
	 * @param messageNumber
	 * @return 
	 * 			1 if "Like" was clicked
	 * 			-1 if "Like zurücknehmen" was clicked
	 * 			0 if no button was clicked
	 */
	public int clickLikeButton(int messageNumber) {
		if(likeMessage(messageNumber)) {
			return 1;
		} else if (revertLike(messageNumber)) {
			return -1;
		}
		return 0;
		
	}
	
	/**
	 * Get list of names of users who like the indicated message
	 * 
	 * @param messageNumber
	 * @return ArrayList<String> of names
	 */
	public List<String> getUsersLikingMessage(int messageNumber) {
		List<String> names = new ArrayList<>();
		int number = getNumberOfLikes(messageNumber);
		if (number > 0) {
			getElement(".//*[@id='activityform:activities:" +messageNumber +":fadePersons']").click();
			if(number == 1) {
				names.add(getElement(".//*[@id='activityform:activities:" +messageNumber 
						+":ratingScroller']/div/ul/li/table/tbody/tr/td/a/span").getText());
			} else {
				for(int i = 1; i <= number; i++) {
					names.add(getElement(".//*[@id='activityform:activities:" +messageNumber +":ratingScroller']/div/ul/li[" +i 
							+"]/table/tbody/tr/td/a/span").getText());
				}
			}
		}
		return names;
	}
	
	/**
	 * Get number of comments
	 * 
	 * @param messageNumber
	 * @return number of comments
	 */
	public int getNumberOfComments(int messageNumber) {
		String text = getElement(".//div[starts-with(@id, 'activityform:activities:" + messageNumber + "')]/table[3]/tbody/tr/td[5]").getText();
		String number = text.split(" ")[0];
		return Integer.parseInt(number);	
	}

	/**
	 * Get AdminPage
	 * 
	 * @return AdminPage PageObject
	 */
	public AdminPage getAdministrationPage() {
	    WebElement button = driver.findElement(By.id("j_idt8:j_idt13"));
		if(button != null) {
			button.click();
			return new AdminPage(driver);
		}
		return null;
	}
	
	/**
	 * Get read-only UserPage
	 * 
	 * @return UserPage PageObject
	 */
	public UserPage getReadOnlyUserProfilePage() {
	    driver.findElement(By.id("j_idt8:j_idt15_button")).click();
	    return new UserPage(driver);
	}
	
	/**
	 * Get UserPage
	 * 
	 * @return UserPage PageObject
	 */
	public UserPage getUserProfilePage() {
	    driver.findElement(By.id("j_idt8:j_idt15_menuButton")).click();
	    driver.findElement(By.cssSelector("span.ui-menuitem-text")).click();
	    return new UserPage(driver);
	}
	
	/**
	 * Search User
	 * 
	 * @param search: search string
	 * @return UserList PageObject with user list
	 */
	public UserList searchUser(String search) {
		WebElement searchInput = driver.findElement(By.id("searchform:usersearch"));
		searchInput.clear();
		searchInput.sendKeys(search);
	    driver.findElement(By.id("searchform:searchbutton")).click();
	    return new UserList(driver);
	}

	/**
	 * Returns message number (index in activity stream) for message with indicated headline
	 * Required for testing
	 * 
	 * @param headline
	 * @return message number as int or -1 if not found
	 */
	public int getMessageNumberByHeadline(String headline) {
		for (int i = 0; i < driver.findElements(By.xpath(".//*[@id='activityform:activities']/div/ul/li")).size(); i++) {
			if(getElement(".//*[@id='activityform:activities:"+i+":j_idt57']/table[2]/tbody/tr/td/table/tbody/tr[1]/td/span")
					.getText().equals(headline)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Get Like button WebElement for indicated activity
	 * 
	 * @param messageNumber: index of activity
	 * @return Like button as WebElement
	 */
	private WebElement getLikeButton(int messageNumber) {
		return getElement(".//div[starts-with(@id, 'activityform:activities:" + messageNumber + "')]/table[3]/tbody/tr/td[1]/input");
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
		return ".//div[starts-with(@id, 'activityform:activities:" + messageNumber + "')]/table[1]/tbody/tr/";
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
	
	/**
	 * Change the password of the logged in user
	 */
	public void changePassword(String oldPassword, String newPassword) {
		driver.findElement(By.xpath(".//*[@id='j_idt8:j_idt15_menuButton']")).click();
		driver.findElement(By.xpath(".//*[@id='j_idt8:j_idt15_menu']/ul/li[3]/a")).click();
		driver.findElement(By.id("passwordchangeform:oldPassword")).clear();
		driver.findElement(By.id("passwordchangeform:oldPassword")).sendKeys(oldPassword);
		driver.findElement(By.id("passwordchangeform:newPassword")).clear();
		driver.findElement(By.id("passwordchangeform:newPassword")).sendKeys(newPassword);
		driver.findElement(By.id("passwordchangeform:newPasswordConfirmation")).clear();
		driver.findElement(By.id("passwordchangeform:newPasswordConfirmation")).sendKeys(newPassword);
		driver.findElement(By.id("passwordchangeform:confirmationButton")).click();
		
		//wait
		(new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            @Override
			public Boolean apply(WebDriver d) {
            	return d.findElement(By.linkText("Nachricht eingeben")) != null;
            }
        });		
	}
	
	/**
	 * Get logged in user
	 * 
	 * @return logged in user
	 */
	public String getLoggedInUser() {
		return driver.findElement(By.id("j_idt8:j_idt15_button")).getText();
	}
}
