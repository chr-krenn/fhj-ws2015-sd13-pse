package at.fhj.swd13.pse.test.gui.pageobjects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CommunitiesPage {
	
	protected WebDriver driver;
	
	/**
	 * Constructor taking web driver as parameter.
	 * @param driver the web driver.
	 */
	public CommunitiesPage (WebDriver driver) {
		this.driver = driver;
	}
	
	/**
	 * Get title of Communities page
	 * @return Title String
	 */
	public String getTitle() {
		return driver.findElement(By.xpath(".//*[@id='communitySearch']/h1")).getText();
	}
	
	private WebElement getSearchButton() {
		return getElement(".//*[@id='communitySearch:communitySearch']");
	}
	
	private WebElement getElement(String xPath) {
		return driver.findElement(By.xpath(xPath));
	}
	
	private WebElement getCommunityButton(){
		return getElement(".//*[@id='communitySearch:communities_data']/tr[1]/td[1]");
	}
	
	public Boolean isCommunityListPresent()	{
		String header = "Community name";
		String text = getElement(".//*[@id='communitySearch:communities:j_idt40']").getText();	
		return header.equals(text);
	}
	
	public Boolean isSearchButtonPresent(){
		String buttonname = "search";
		String text = getElement(".//*[@id='communitySearch:communitySearch']").getText();	
		return buttonname.equals(text);
	}
	
	
	public Boolean searchForCommunities() {
		WebElement button = getSearchButton();
		if(button.getAttribute("search").equals("search")) {
			button.click();
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether Community is private or not
	 * 
	 * @return true if Community is private.
	 */
	public int isCommunityPrivate(int community) {
		String isPrivate = "Yes";
		String text = getElement(".//*[@id='communitySearch:communities_data']/tr[" + community + "]/td[2]").getText();
		if (isPrivate.equals(text))
			return 1;
		else 
			return 0;
	}
	
	/**
	 * Get CommunitiyPage
	 * 
	 * @return CommunityPage PageObject
	 */
	public CommunityPage getCommunityPage() {
		WebElement button = getCommunityButton();
		button.click();
		return new CommunityPage(driver);
	}
	
	/**
	 * Search for Communities
	 * 
	 */
	public void searchCommunities(String search) {
		WebElement searchInput = driver.findElement(By.id("communitySearch:j_idt38"));
		searchInput.clear();
		searchInput.sendKeys(search);
		driver.findElement(By.id("communitySearch:communitySearch")).click();
		
		
	}
	
	/**
	 * Get name of community listed 
	 * 
	 * @return name of community listed first
	 */
	public int getCommunityName(String name){
		String text = getElement(".//*[@id='communitySearch:communities_data']/tr[1]/td[1]").getText();
		if (name.equals(text))
			return 1;
		else 
			return 0;
	}
	
	/**
	 * Get number of communities listed 
	 * 
	 * @return number of listed communities
	 */
	public int getListedCommunitiesNumber() {
		if(((driver.findElements(By.xpath("//tbody[@id='communitySearch:communities_data']/tr"))).size() == 1) &&
				driver.findElement(By.xpath("//tbody[@id='communitySearch:communities_data']/tr")).getText().equals("No records found."))
			return 0;
		else
			return driver.findElements(By.xpath("//tbody[@id='communitySearch:communities_data']/tr")).size();	
	}
	
	/**
	 * Get number of found communities  
	 * 
	 * @return number of listed communities
	 */
	public int getFoundCommunitiesNumber() {
		
		if(((driver.findElements(By.xpath("//tbody[@id='communitySearch:communities_data']/tr"))).size() == 1))
			return 1;
		else
			return driver.findElements(By.xpath("//tbody[@id='communitySearch:communities_data']/tr")).size();	
	}
	

	private WebElement getAskCommunityButton(int community){
		return getElement(".//*[@id='communitySearch:communities:" + community + ":askCommunity']");
		
	}
	
	private WebElement getJoinCommunityButton(int community){
		return getElement(".//*[@id='communitySearch:communities:" + community + ":addCommunity']");
		
	}
	
	/**
	 * Join a Public Community  
	 * 
	 */	
	public String joinPublicCommunity(int community){
		WebElement button = getJoinCommunityButton(community);
		button.click();
		String text = getElement(".//*[@id='communitySearch:messages']/div/ul/li/span[2]").getText();
		return text;
	}
	
	/**
	 * Join a Private Community  
	 * 
	 */
	public String joinPrivateCommunity(int community){
		WebElement button = getAskCommunityButton(community);
		button.click();
		String text = getElement(".//*[@id='communitySearch:messages']/div/ul/li/span[2]").getText();
		return text;
	}

	public String isMemberOfCommunity(int Community) {
		String text = getElement(".//*[@id='communitySearch:communities_data']/tr[" + Community + "]/td[4]").getText();
		return text;
	}
	
	
}
