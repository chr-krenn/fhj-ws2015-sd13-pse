package at.fhj.swd13.pse.test.gui.pageobjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AdminPage {
	
	protected WebDriver driver;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver the web driver.
	 */
	public AdminPage (WebDriver driver) {
		this.driver = driver;
	}
	
	/**
	 * Get all community requests
	 * @return: List<String> with requested communities
	 */
	public List<String> getCommunityRequests()
	{
		List<String> communityRequets = new ArrayList<String>();
		
        for (WebElement we : driver.findElements(By.xpath(".//*[@id='communitySearch:requests_data']/tr")))
		{
			if(!we.getText().equals("No records found."))
				communityRequets.add(we.getText().substring(0, we.getText().indexOf("\n")));
		}
		return communityRequets;	
	}

	/**
	 * Get all user community requests
	 * @return: List<String> with requested communities
	 */
	public List<String> getUserCommunityRequests()
	{
		List<String> communityUserRequets = new ArrayList<String>();
		
        for (WebElement we : driver.findElements(By.xpath(".//*[@id='communityMemberSearch:memberrequests_data']/tr")))
		{
			if(!we.getText().equals("No records found."))
				communityUserRequets.add(we.getText().substring(0, we.getText().indexOf("\n", we.getText().indexOf("\n"))));
		}
		return communityUserRequets;	
	}
	
	/**
	 * Get the name of the first community request
	 * @return: the name of the first community to accept
	 */
	public String getFirstRequestedCommunityName(){
		String text = driver.findElement(By.xpath(".//*[@id='communitySearch:requests_data']/tr/td[1]")).getText();
		return text;
	}
	
	/**
	 * Get the name of the community accept button
	 * @return: the name of the accept button
	 */
	public String isAcceptCommunityButtonPresent(){
		String text = driver.findElement(By.xpath(".//*[@id='communitySearch:requests:0:approveCommunity']")).getText();
		return text;
	}
	
	/**
	 * Get the name of the community deny button
	 * @return: the name of the deny button
	 */
	public String isDenyCommunityButtonPresent(){
		String text = driver.findElement(By.xpath(".//*[@id='communitySearch:requests:0:declineCommunity']")).getText();
		return text;
	}
	
	/**
	 * click the community accept button
	 */
	public void clickCommunityAcceptButton(){
		WebElement button = driver.findElement(By.xpath(".//*[@id='communitySearch:requests:0:approveCommunity']"));
		button.click();
	}
	
	/**
	 * Look whether a membership request is present or not
	 * @return: the name of the community a request is pending
	 */
	public String getNameOfMembershipCommunityRequest(){
		String text = driver.findElement(By.xpath(".//*[@id='communityMemberSearch:memberrequests_data']/tr/td[1]")).getText();
		return text;
	}
	
	/**
	 * Get the name of the membership deny button
	 * @return: the name of the deny button
	 */
	public String isDenyMembershipButtonPresent(){
		String text = driver.findElement(By.xpath(".//*[@id='communityMemberSearch:memberrequests:0:declineMemberRequest']")).getText();
		return text;
	}
	
	/**
	 * Get the name of the membership accept button
	 * @return: the name of the accept button
	 */
	public String isAcceptMembershipButtonPresent(){
		String text = driver.findElement(By.xpath(".//*[@id='communityMemberSearch:memberrequests:0:approveMemberRequest']")).getText();
		return text;
	}
	
	/**
	 * click the accept membership button
	 */
	public void clickMembershipAcceptButton(){
		WebElement button = driver.findElement(By.xpath(".//*[@id='communityMemberSearch:memberrequests:0:approveMemberRequest']"));
		button.click();
	}
	
	/**
	 * get the name of the membership requestor
	 * @return: the name of the  membership requestor
	 */
	public String getNameOfMembershipRequestor(){
		String text = driver.findElement(By.xpath(".//*[@id='communityMemberSearch:memberrequests_data']/tr/td[2]")).getText();
		return text;
	}
}
