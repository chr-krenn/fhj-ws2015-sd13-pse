package at.fhj.swd13.pse.db.dao;

import java.util.List;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.Person;

public interface CommunityDAO {

	/**
	 * Add a community instance to the persistent storage
	 * 
	 * @param community
	 *            the community to add
	 */
	void insert(final Community community) throws ConstraintViolationException;

	/**
	 * Get a community from persistent storage by its unique name
	 * 
	 * @param name
	 *            name of the community to get
	 * 
	 * @return instance of the community or null if none found
	 */
	Community getByName(final String name);

	/**
	 * Get a community from persistent storage by its id
	 * 
	 * @param communityId
	 *            id of the community to get
	 * 
	 * @return instance of the community or null if none found
	 */
	Community get(final int communityId);

	/**
	 * get all communities that start with the given needle
	 * 
	 * @param needle
	 *            string the communities must start with
	 * 
	 * @return a list of matching communities or an empty list
	 */
	List<Community> getMatchingCommunities(final String needle);

	/**
	 * Remove a community from the persistent storage by its id
	 * 
	 * @param communityId
	 *            the id of the community to remove
	 */
	void remove(final int communityId);

	/**
	 * Remove the given community from the persistent storage
	 * 
	 * @param community
	 */
	void remove(final Community community);

	/**
	 * Get a list of all unconfirmed communities
	 * 
	 * @return a list of all currently unconfirmed communities or an empty list
	 */
	List<Community> getUnconfirmedCommunites();
	
	/**
	 * Get a list of all communities
	 * @param person
	 * @return list of communities of the person
	 */
	List<Community> getCommunities(final Person person);
	
	/**
	 * Get a list of all communities
	 * @return list of communities
	 */
	List<Community> getAllCommunities();
	
	/**
	 * Get a list of all communities with a search string
	 * @param searchFieldText
	 * @return list of communities
	 */
	List<Community> getAllCommunities(String searchFieldText);
	
	/**
	 * Get the private community for a person
	 * @param person
	 * @return private community
	 */
	Community getPrivateCommunity(Person person);
}
