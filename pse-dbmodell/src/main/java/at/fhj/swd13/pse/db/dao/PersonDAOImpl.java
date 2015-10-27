package at.fhj.swd13.pse.db.dao;

import java.util.List;

import javax.persistence.Query;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DAOBase;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.EntityNotFoundException;
import at.fhj.swd13.pse.db.entity.ParameterException;
import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.db.entity.PersonRelation;

public class PersonDAOImpl extends DAOBase implements PersonDAO {

	public PersonDAOImpl(DbContext dbContext) {

		super(dbContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.PersonDAO#insert(at.fhj.swd13.pse.db.Person)
	 */
	@Override
	public void insert(Person person) throws ConstraintViolationException{

		dbContext.persist(person);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.PersonDAO#remove(at.fhj.swd13.pse.db.Person)
	 */
	@Override
	public void remove(Person person) {

		if (person == null || person.getPersonId() == 0) {
			throw new ParameterException("person null or not persisted yet");
		}

		for (PersonRelation relation : person.getPersonSourceRelations()) {

			dbContext.remove(relation);
		}

		dbContext.remove(person);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.dao.PersonDAO#remove(int)
	 */
	@Override
	public void remove(int personId) {

		final Query q = dbContext.createNamedQuery("Person.deleteById");
		q.setParameter("id", personId);

		q.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhjoanneum.swd13.pse.db.PersonDAO#getById(int)
	 */
	@Override
	public Person getById(int personId) throws EntityNotFoundException {

		final Query q = dbContext.createNamedQuery("Person.findById");
		q.setParameter("id", personId);

		Person result = fetchSingle(q);

		if (result == null)
			throw new EntityNotFoundException("Person not found");

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.PersonDAO#getByUsername(java.lang.String)
	 */
	@Override
	public Person getByUsername(String username) {

		final Query q = dbContext.createNamedQuery("Person.findByUserName");
		q.setParameter("uname", username);

		return fetchSingle(q);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.PersonDAO#getByUsername(java.lang.String)
	 */
	@Override
	public Person getByUsername(String username, boolean assertRequired) throws EntityNotFoundException {

		Person p = getByUsername(username);

		if (assertRequired && p == null) {

			throw new EntityNotFoundException("Unknown user with username " + username);
		}

		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.dao.PersonDAO#getAllPersons(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getAllPersons(int startRow, int maxRows) {

		Query q = dbContext.createNamedQuery("Person.findAll");
		q.setFirstResult(startRow);
		q.setMaxResults(maxRows);

		return (List<Person>) q.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.fhj.swd13.pse.db.PersonDAO#getAllPersonsWithDepartment(String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getAllPersonsWithDepartment(String department) {
		Query q = dbContext.createNamedQuery("Person.findAllWithDepartment");
		q.setParameter("department", department);

		return (List<Person>) q.getResultList();
	}

	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.PersonDAO#getAllPersons()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List <Person> getAllPersons() {

		Query q = dbContext.createNamedQuery("Person.findAll");
		return (List<Person>) q.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getAllWithNullPasswords() {

		Query q = dbContext.createNamedQuery("Person.findAllNullPasswords");

		return (List<Person>) q.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.dao.PersonDAO#getPersonLike(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getPersonLike(final String name) {

		final Query q = dbContext.createNamedQuery("Person.findNameLike");
		q.setParameter("name", "%" + name + "%");

		return q.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.fhj.swd13.pse.db.PersonDAO#createRelation(at.fhj.swd13.pse.db.Person,
	 * at.fhj.swd13.pse.db.Person)
	 */
	@Override
	public PersonRelation createRelation(Person sourcePerson, Person targetPerson) throws ConstraintViolationException {

		PersonRelation relation = new PersonRelation(sourcePerson, targetPerson);

		dbContext.persist(relation);

		return relation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.fhj.swd13.pse.db.PersonDAO#removeTargetRelations(at.fhj.swd13.pse.
	 * db.Person)
	 */
	@Override
	public int removeTargetRelations(final Person person) {

		int removedRelationCount = 0;

		for (PersonRelation relation : person.getPersonTargetRelations()) {

			relation.getSourcePerson().removeRelationTo(person);

			dbContext.remove(relation);
			++removedRelationCount;
		}

		person.getPersonTargetRelations().clear();

		return removedRelationCount;
	}

	
	/*
	 * 
	 * for now, deleting of a relation is possible for the source person and the target person
	 * therefore, both combinations are executed 
	 * (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.PersonDAO#removeRelation(at.fhj.swd13.pse.db.entity.Person, at.fhj.swd13.pse.db.entity.Person)
	 */
	
	@Override
	public void removeRelation(Person sourcePerson, Person targetPerson) {
		
		final Query q = dbContext.createNamedQuery("PersonRelation.deleteByPersonIds");
		q.setParameter("sourcePerson", sourcePerson).setParameter("targetPerson", targetPerson);
		q.executeUpdate();	

		final Query q2 = dbContext.createNamedQuery("PersonRelation.deleteByPersonIds");
		q2.setParameter("sourcePerson", targetPerson).setParameter("targetPerson", sourcePerson);
		q2.executeUpdate();	

		
	}

	@Override
	public Person getByEmailAddress(String emailAddress) {
		final Query q = dbContext.createNamedQuery("Person.findByEmailAddress");
		q.setParameter("emailAddress", emailAddress);

		return fetchSingle(q);
	
	}
}
