package at.fhj.swd13.pse.db.dao;

import java.util.List;

import javax.persistence.Query;

import at.fhj.swd13.pse.db.DAOBase;
import at.fhj.swd13.pse.db.DbContext;
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
	public void insert(Person person) {

		dbContext.persist(person);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.PersonDAO#remove(at.fhj.swd13.pse.db.Person)
	 */
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
	public Person getById(int personId) {

		final Query q = dbContext.createNamedQuery("Person.findById");
		q.setParameter("id", personId);

		return fetchSingle(q);
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

	@SuppressWarnings("unchecked")
	public List<Person> getAllPersons(int startRow, int maxRows) {

		Query q = dbContext.createNamedQuery("Person.findAll");
		q.setFirstResult(startRow);
		q.setMaxResults(maxRows);

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
	public PersonRelation createRelation(Person sourcePerson, Person targetPerson) {

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
}
