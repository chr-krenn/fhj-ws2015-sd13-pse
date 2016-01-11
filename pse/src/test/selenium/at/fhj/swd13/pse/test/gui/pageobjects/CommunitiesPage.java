package at.fhj.swd13.pse.test.gui.pageobjects;

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
	
	/**
	 * get the communities search button
	 * @return search button
	 */
	private WebElement getSearchButton() {
		return getElement(".//*[@id='communitySearch:communitySearch']");
	}
	
	/**
	 * get the webelement to the belonging xPath
	 * @return webelement
	 */
	private WebElement getElement(String xPath) {
		return driver.findElement(By.xpath(xPath));
	}
	
	/**
	 * get the webelement for the get community button
	 * @return webelement - community button
	 */
	private WebElement getCommunityButton(){
		return getElement(".//*[@id='communitySearch:communities_data']/tr[1]/td[1]");
	}
	
	/**
	 * check whether community list is present or not
	 * @return 1 if exists else 0
	 */
	public Boolean isCommunityListPresent()	{
		String header = "Community name";
		String text = getElement(".//*[@id='communitySearch:communities:j_idt40']").getText();	
		return header.equals(text);
	}
	
	/**
	 * check whether community search button is present or not
	 * @return 1 if exists else 0
	 */
	public Boolean isSearchButtonPresent(){
		String buttonname = "search";
		String text = getElement(".//*[@id='communitySearch:communitySearch']").getText();	
		return buttonname.equals(text);
	}
	
	/**
	 * clicks the search button if exists
	 * @return 1 if exists else 0
	 */
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
	 * checks whether community names are equal
	 * 
	 * @return 1 if it's the same name else 0
	 */
	public int communityNameEquals(String name){
		String text = getElement(".//*[@id='communitySearch:communities_data']/tr[1]/td[1]").getText();
		if (name.equals(text))
			return 1;
		else 
			return 0;
	}
	
	/**
	 * How many communities are listed 
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
	 * @return the number of listed communities
	 */
	public int getFoundCommunitiesNumber() {
		
		if(((driver.findElements(By.xpath("//tbody[@id='communitySearch:communities_data']/tr"))).size() == 1))
			return 1;
		else
			return driver.findElements(By.xpath("//tbody[@id='communitySearch:communities_data']/tr")).size();	
	}
	
	/**
	 * returns "ask community" button of given community
	 * 
	 * @return the ask button
	 */
	private WebElement getAskCommunityButton(int community){
		return getElement(".//*[@id='communitySearch:communities:" + community + ":askCommunity']");
		
	}
	
	/**
	 * returns "join community" button of given community
	 * 
	 * @return the join button
	 */
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
	/**
	 * see person is member of community
	 * 
	 * @return yes or no depending on whether person is member of community or not
	 */
	public String isMemberOfCommunity(int Community) {
		String text = getElement(".//*[@id='communitySearch:communities_data']/tr[" + Community + "]/td[4]").getText();
		return text;
	}
	
	/**
	 * get the name of given community displayed
	 * 
	 * @return the name of given community
	 */
	public String getCommunityName(int community){
		String text = getElement(".//*[@id='communitySearch:communities_data']/tr[" + community + "]/td[1]").getText();
		return text;
	}
	
}
