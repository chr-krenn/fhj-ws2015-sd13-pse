package at.fhj.swd13.pse.db.dao;

import java.util.List;

import at.fhj.swd13.pse.db.entity.Tag;

public interface TagDAO {

	/**
	 * Insert the given tag into the persistent store
	 * 
	 * @param tag the tag to persist
	 */
	void insert( Tag tag );
	
	/**
	 * Get a tag by its id
	 * 
	 * @param tagId the id of the tag to fetch
	 * 
	 * @return tag instance or null if none could be found
	 */
	Tag getById(int tagId);

	/**
	 * Get a list of tokens that start with the given string.
	 *  Search is case insensitive
	 * 
	 * @param beginning the beginning of the tag-token
	 * 
	 * @return a list with matching tokens or an empty list if none were found
	 */
	List<Tag> getByTokenLike( String beginning );	
	
	/**
	 * remove a tag identified by its tagID
	 * 
	 * @param tagId id of the tag to remove
	 */
	void remove( int tagId );
}