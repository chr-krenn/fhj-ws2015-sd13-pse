package at.fhj.swd13.pse.test.gui.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class CommunityPage{

	protected WebDriver driver;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver the web driver.
	 */
	public CommunityPage (WebDriver driver) {
		this.driver = driver;
	}
	
	/**
	 * Get title of Communities page
	 * @return Title String
	 */
	public String getTitle() {
		return getElement(".//*[@id='communityProfile']/h1").getText();
	}
	
	
	private WebElement getElement(String xPath) {
		return driver.findElement(By.xpath(xPath));
	}
	
	/**
	 * Checks whether section with header 'Activities' is present.  
	 * 
	 * @return true if Activities header is present.
	 */
	public Boolean isActivitiesStreamPresent(){
		String header = "Activities";
		String text = getElement(".//*[@id='j_idt44:j_idt45_header']/span").getText();	
		return header.equals(text);
	}
	
	private WebElement getCreateNewMessageButtonPresent(){
		return getElement(".//*[@id='createCommunityMessage']");
		
	}
	
	
	public Boolean createNewMessageButtonPresent(){
		WebElement button = getCreateNewMessageButtonPresent();	
		button.click();
		return true;
	}

	/**
	 * Open new message page
	 * 
	 * @return NewMessagePage PageObject
	 */
	public NewCommunityMessagePage openNewMessage() {
		getElement(".//*[@id='j_idt34_content']/div[1]/a").click();
		return new NewCommunityMessagePage(driver);
	}

	public String getActivityTitle(int activity) {
		if (activity == 1)
			return getElement(".//*[@id='j_idt44:j_idt46:0:j_idt56']/table[1]/tbody/tr/td/table/tbody/tr[1]/td/span").getText();
		else
			return getElement(".//*[@id='j_idt44:j_idt46:1:j_idt56']/table[1]/tbody/tr/td/table/tbody/tr[1]/td/span").getText();
	}

	public String getActivityText(int activity) {
		if (activity == 1)
			return getElement(".//*[@id='j_idt44:j_idt46:0:j_idt56']/table[1]/tbody/tr/td/table/tbody/tr[2]/td").getText();
		else
			return getElement(".//*[@id='j_idt44:j_idt46:1:j_idt56']/table[1]/tbody/tr/td/table/tbody/tr[2]/td").getText();
	}
	
	public String getAuthor(int activity){
		return getElement(".//*[@id='j_idt44:j_idt46']/div/ul/li[" + activity + "]/table/tbody/tr[1]/td/table/tbody/tr/td[2]/span").getText();
	}
	
	
	public String messageWasSaved(){		
		return getElement(".//*[@id='j_idt36_container']/div/div/div[2]/p").getText();
	}

	public boolean isFirstActivityPresent() {
		getElement(".//*[@id='j_idt44:j_idt46']/div/ul/li[1]");
		return false;
	}
	
	public boolean isSecondActivityPresent() {
		
		return false;
	}

	
	
}
