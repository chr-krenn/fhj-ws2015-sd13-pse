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
	
	
}
