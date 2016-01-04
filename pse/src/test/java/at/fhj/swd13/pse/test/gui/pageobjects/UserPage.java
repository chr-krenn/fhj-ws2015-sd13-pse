package at.fhj.swd13.pse.test.gui.pageobjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import at.fhj.swd13.pse.test.util.SleepUtil;

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
	 * Set Login Allowed state
	 *
	 * @param loginAllowed state
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
	 * @return loginAllowed state
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
	
	/**
	 * Click on tab "Kontakte" in user profile page
	 */
	public void openContactsTab() {
		driver.findElement(By.xpath(".//*[@id='userForm:j_idt97']/ul/li[2]/a")).click();
	}
	
	/**
	 * Click on tab "Communities" in user profile page
	 */
	public void openCommunitiesTab() {
		driver.findElement(By.xpath(".//*[@id='userForm:j_idt97']/ul/li[3]/a")).click();
	}
	
	/**
	 * Press community request button
	 */
	public void communityRequest(String community) {
		driver.findElement(By.id("userForm:j_idt97:membershipList:communityButton")).click(); 
		driver.findElement(By.id("communityDialogForm:communityname")).clear();
		driver.findElement(By.id("communityDialogForm:communityname")).sendKeys(community);
		driver.findElement(By.id("communityDialogForm:requestButton")).click();		
	}
	
	/**
	 * Get number of contacts
	 * 
	 * @return number of contacts
	 */
	public int getNumberOfContacts() {
		
		if(((driver.findElements(By.xpath(".//*[@id='userForm:j_idt97:j_idt110_data']/tr"))).size() == 1) &&
		    (driver.findElement(By.xpath(".//*[@id='userForm:j_idt97:j_idt110_data']/tr"))).getText().equals("No records found."))
			return 0;
		else
			return driver.findElements(By.xpath(".//*[@id='userForm:j_idt97:j_idt110_data']/tr")).size();
	}
	
	/**
	 * Get names of contacts
	 * 
	 * @return List with names of contacts
	 */
	public List<String> getNamesOfContacts() {
		
		List<String> nameList = new ArrayList<String>();
		
		for (WebElement we : driver.findElements(By.xpath(".//*[@id='userForm:j_idt97:j_idt110_data']/tr")))
		{
			if(!we.getText().equals("No records found."))
				nameList.add(we.getText());
		}
		
		return nameList;
	}
	
	/**
	 * Get "contactButton" button
	 * 
	 * @return WebElement for button
	 */
	private WebElement getContactButton() {
		return driver.findElement(By.id("userForm:contactButton"));
	}
		
	/**
	 * Add to contact
	 */
	public void addToContact() {
		getContactButton().click();
		
		//wait
		(new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            @Override
			public Boolean apply(WebDriver d) {
            	return getContactButton().getText().equals("Aus meinen Kontakten entfernen");
            }
        });	
	}
	
	/**
	 * Remove  contact
	 */
	public void removeContact() {
		getContactButton().click();
		
		//wait
		(new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
            @Override
			public Boolean apply(WebDriver d) {
            	return getContactButton().getText().equals("Zu meinen Kontakten hinzufügen");
            }
        });	
	}
	
	/**
	 * Open profile of contact
	 * 
	 * @param contactNumber: start with 1 for first contact
	 * @return UserPage PageObject
	 */
	public UserPage openContactProfile(int contactNumber) {
		openContactsTab();
		int numberOfContacts = getNumberOfContacts();
		if(numberOfContacts == 1 && contactNumber == 1) {
			driver.findElement(By.xpath(".//*[@id='userForm:j_idt97:j_idt110_data']/tr/td[1]/a")).click();
			return this;
		} else if (numberOfContacts > 1) {
			driver.findElement(By.xpath(".//*[@id='userForm:j_idt97:j_idt110_data']/tr[" +contactNumber +"]/td[1]/a")).click();
			return this;
		}
		return null;
	}
	
	/**
	 * Get "Nachricht senden" button
	 * 
	 * @return WebElement for button
	 */
	public WebElement getSendMessageButton() {
		return driver.findElement(By.id("userForm:sendMessageButton"));
	}
	
	
	/**
	 * Click "Nachricht senden" button to open new message page
	 * 
	 * @return NewMessagePage PageObject
	 */
	public NewMessagePage clickSendMessageButton() {
		getSendMessageButton().click();
		return new NewMessagePage(driver);
	}
	
	//FIXME: not tested because sending message doesn't work via Selenium yet
	/**
	 * Get confirmation text that message has been sent
	 * @return
	 */
	public String getConfirmationForSendingMessage() {
		return driver.findElement(By.xpath(".//*[@id='userForm:messages_container']/div/div/div[2]/span")).getText();
	}
	
	/**
	 * Get names of users with department
	 * 
	 * @return List of users with department
	 */
	public List<String> getNamesOfUsersWithDepartment()
	{
		List<String> nameList = new ArrayList<String>();
		
		for (WebElement we : driver.findElements(By.xpath(".//*[@id='userForm:j_idt97:j_idt99_data']/tr")))
		{
			if(!we.getText().equals("No records found."))
				nameList.add(we.getText());
		}
				
		return nameList;
	}
	
	/**
	 * Add interest
	 * 
	 * @param interest
	 */
	public void addInterest(String interest) {
		WebElement tagInput = driver.findElement(By.id("userForm:personTags_input"));
		tagInput.sendKeys(interest);
		SleepUtil.sleep(500);
		driver.findElement(By.xpath(".//*[@id='userForm:personTags_panel']/ul/li")).click();
	}
	
	/**
	 * Get the user interests
	 */
	public List<String> getUserInterests() {
		List<String> interests = new ArrayList<>();
		for (WebElement e : getInterestsWebElements()) {
			if(e.getText().length() > 0) 
				interests.add(e.getText());
		}
		return interests;
	}
	
	/**
	 * Get WebElements for interests
	 * 
	 * @return List of WebElements
	 */
	private List<WebElement> getInterestsWebElements() {
		return driver.findElements(By.xpath(".//*[@id='userForm:personTags']/ul/li"));
	}

	

	
	/**
	 * Set the lastname of a user
	 * @param the new lastname
	 */
	public void setUserLastname(String lastname) {
		WebElement inputLastname = driver.findElement(By.id("userForm:lastName"));
		inputLastname.clear();
		inputLastname.sendKeys(lastname);
	}
	
}
