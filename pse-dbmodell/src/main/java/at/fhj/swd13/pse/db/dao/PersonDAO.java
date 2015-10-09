package at.fhj.swd13.pse.db.dao;

import java.util.List;

import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.PersonRelation;

/**
 * Provide access to a Person from a persistence provider
 */
public interface PersonDAO {

	/**
	 * Add a person instance to persistent storage
	 * 
	 * @param person the person to persist
	 */
	void insert( Person person );
	
	
	/**
	 * Remove a person instance from the persistent storage
	 * 
	 * @param personId the id of the person to remove
	  */
	 void remove( int personId );

	/**
	 * Remove a person instance from the persistent storage
	 * 
	 * @param person the person to remove
	 */
	 void remove( Person person );
	
	/**
	 * Load a person with the given id 
	 * 
	 * @param personId the id of the person to retrieve
	 * @return the person with the given id, null if none was found
	 */
	Person getById(int personId) throws EntityNotFoundException;

	
	/**
	 * Load a person with the given username, the search is case sensitive
	 * 
	 * @param username the username of the person to retrieve
	 * 
	 * @return the person with the given username, null if none was found
	 */
	Person getByUsername( String username ) ;

	/**
	 * Load a person with the given username, the search is case sensitive
	 * 
	 * @param username the username of the person to retrieve
	 * @param assertFound when true, 
	 * 
	 * @return the person with the given username, null if none was found
	 */
	Person getByUsername( String username, boolean assertFound ) throws EntityNotFoundException;
	
	
	/**
	 * Load all persons sorted by last_name, first_name
	 * and provide paging
	 * 
	 * @param startRow the row at which to start
	 * @param maxRows the max number of rows to return
	 * 
	 * @return a list of persons within the page-window,
	 * 			 may be empty or only half-filled
	 */
	List<Person> getAllPersons( int startRow, int maxRows );
	
	/**
	 * Load all persons that have no hashedPassword (IS NULL)
	 * 
	 * @return List of persons with no password, empty if none found
	 */
	List<Person> getAllWithNullPasswords();
	
	/**
	 * Get all persons matching the given name
	 * Either as part of their last_name or user_name
	 * the serach is not case-insensitive
	 * 
	 * @param name part to look for in either the last_name or user_name
	 * 
	 * @return a list of users with a matching name, or an empty list if none match
	 */
	List<Person> getPersonLike( final String name );
	
	/**
	 * Create a relation between two persons
	 * 
	 * @param sourcePerson create relation from person
	 * @param targetPerson create relation to person
	 * 
	 * @return the created relation instance
	 */
	PersonRelation createRelation( Person sourcePerson, Person targetPerson );
	
	/**
	 * Remove all relations where the given person is the target
	 *  
	 *  !!! I am not sure what happens to already cached source persons
	 *  
	 * @param person
	 * 
	 * @return the number of removed target relations
	 */
	int removeTargetRelations( Person person );

}