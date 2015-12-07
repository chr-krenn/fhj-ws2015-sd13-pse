package at.fhj.swd13.pse.test.gui.pageobjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MessageDetailView {
	
	protected WebDriver driver;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver the web driver.
	 */
	public MessageDetailView (WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Get header of activity (e.g. "Albert Salzinger, am 07.12.2015 um 16:04")
	 * @return Header String
	 */
	public String getHeader() {
		return getElement(".//*[@id='messagedetails:messagedetailspanel_header']/span").getText();
	}
	
	/**
	 * Get headline of message
	 * 
	 * @return Headline String
	 */
	public String getHeadline() {
		return getElement(".//*[@id='messagedetails:j_idt47']/table/tbody/tr[1]/td/span").getText();
	}
	
	/**
	 * Get message text
	 * 
	 * @return Message String
	 */
	public String getMessage() {
		return getElement(".//*[@id='messagedetails:j_idt47']/table/tbody/tr[2]/td").getText();
	}
	
	/**
	 * Get number of likes
	 * 
	 * @param messageNumber
	 * @return number of likes
	 */
	public int getNumberOfLikes() {
		String text = getElement(".//*[@id='messagedetails:messagedetailspanel_footer']").getText();
		String number = text.split(" ")[0];
		return Integer.parseInt(number);	
	}
	
	/**
	 * Click Like button if activity is not already liked by the current user
	 * 
	 * @return True if button was clicked
	 */
	public Boolean likeMessage() {
		WebElement button = getElement(".//*[@id='messagedetails:messagedetailspanel_footer']/input");
		if(button.getAttribute("value").equals("Like")) {
			button.click();
			return true;
		}
		return false;
	}
	
	/**
	 * Click "Like zurücknehmen" button if activity is liked by the current user
	 * 
	 * @return True if button was clicked
	 */
	public Boolean revertLike() {
		WebElement button = getElement(".//*[@id='messagedetails:messagedetailspanel_footer']/input");
		if(button.getAttribute("value").equals("Like zurücknehmen")) {
			button.click();
			return true;
		}
		return false;
	}
	
	/**
	 * Click "Like" or "Like zurücknehmen" button
	 * 
	 * @return 
	 * 			1 if "Like" was clicked
	 * 			-1 if "Like zurücknehmen" was clicked
	 * 			0 if no button was clicked
	 */
	public int clickLikeButton() {
		if(likeMessage()) {
			return 1;
		} else if (revertLike()) {
			return -1;
		}
		return 0;
		
	}
	
	/**
	 * Get list of names of users who like the message
	 * 
	 * @return ArrayList<String> of names
	 */
	public List<String> getUsersLikingMessage() {
		List<String> names = new ArrayList<>();
		int number = getNumberOfLikes();
		if (number > 0) {
			getElement(".//*[@id='messagedetails:fadePersons']").click();
			if(number == 1) {
				names.add(getElement(".//*[@id='j_idt59']/div/ul/li/table/tbody/tr/td/a/span").getText());
			} else {
				for(int i = 1; i <= number; i++) {
					names.add(getElement(".//*[@id='j_idt59']/div/ul/li[" +i 
							+"]/table/tbody/tr/td/a/span").getText());
				}
			}
		}
		return names;
	}
	
	/**
	 * Get number of comments indicated in Comments section header
	 * 
	 * @return number of comments
	 */
	public int getNumberOfCommentsInHeader() {
		String text = getElement(".//*[@id='j_idt65:j_idt66_header']/span").getText();
		String pattern = "(.*\\()(\\d+)(\\))";
		String number = text.replaceAll(pattern, "$2");
		return Integer.parseInt(number);	
	}
	
	/**
	 * Get number of comments listed in comments section
	 * FIXME: extremely slow! (waiting for timeout - why?)
	 * 
	 * @return number of comments
	 */
	public int getNumberOfDisplayedComments() {
		int number = driver.findElements(By.xpath(".//*[@id='j_idt65:commentList']/div/ul/li")).size();
		return number;	
	}
	
	public List<String> getCommentsText() {
		List<String> comments = new ArrayList<>();
		for(int i = 0; i < getNumberOfCommentsInHeader(); i++) {
			comments.add(getElement(".//*[@id='j_idt65:commentList:" + i
					+":j_idt75']/table[2]/tbody/tr/td/table/tbody/tr[2]/td").getText());
		}
		return comments;
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
}
