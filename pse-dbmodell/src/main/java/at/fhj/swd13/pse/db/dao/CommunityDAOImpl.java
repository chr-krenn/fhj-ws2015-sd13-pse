package at.fhj.swd13.pse.db.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.Query;

import at.fhj.swd13.pse.db.ConstraintViolationException;
import at.fhj.swd13.pse.db.DAOBase;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.ParameterException;
import at.fhj.swd13.pse.db.entity.Person;

public class CommunityDAOImpl extends DAOBase implements CommunityDAO {

	
	@Inject
	public CommunityDAOImpl(DbContext dbContext) {

		super(dbContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.fhj.swd13.pse.db.dao.CommunityDAO#insert(at.fhj.swd13.pse.db.entity.
	 * Community)
	 */
	@Override
	public void insert(Community community) throws ConstraintViolationException{

		dbContext.persist(community);
	}

	public Community getByName(final String name) {

		final Query q = dbContext.createNamedQuery("Community.findByName");
		q.setParameter("name", name);

		return fetchSingle(q);
	}
	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.CommunityDAO#get(int)
	 */
	public Community get(final int communityId) {
		
		final Query q = dbContext.createNamedQuery("Community.findById");
		q.setParameter("id", communityId);
		
		return fetchSingle(q);
		
	}
	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.CommunityDAO#getMatchingCommunities(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Community> getMatchingCommunities( final String needle )	{
	
		final Query q = dbContext.createNamedQuery("Community.findMatching" );
		q.setParameter("needle", needle + "%");
		
		return q.getResultList();
	}
	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.CommunityDAO#getUnconfirmedCommunites()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Community> getUnconfirmedCommunites() {
		
		return (List<Community>)dbContext.createNamedQuery("Community.findUnconfirmed").getResultList();		
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see at.fhj.swd13.pse.db.dao.CommunityDAO#remove(int)
	 */
	@Override
	public void remove(int communityId) {

		if (communityId <= 0) {

			throw new ParameterException("community null or not persisted yet");
		}

		final Query q = dbContext.createNamedQuery("Community.deleteById");
		q.setParameter("id", communityId);

		q.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.fhj.swd13.pse.db.dao.CommunityDAO#remove(at.fhj.swd13.pse.db.entity.
	 * Community)
	 */
	@Override
	public void remove(Community community) {

		if (community == null || community.getCommunityId() == 0) {
			throw new ParameterException("community null or not persisted yet");
		}

		dbContext.remove(community);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Community> getCommunities(final Person person) {
		final Query q = dbContext.createNamedQuery("Community.findCommunitiesByMember");
		q.setParameter("person", person);
		return (List<Community>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Community> getAllCommunities() {

		final Query q = dbContext.createNamedQuery("Community.findAll");

		return (List<Community>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Community> getAllCommunities(String searchFieldText) {

		final Query q = dbContext.createNamedQuery("Community.findMatching");
		q.setParameter("needle", searchFieldText+"%");
		return (List<Community>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Community getPrivateCommunity(Person person) {
		final Query q = dbContext.createNamedQuery("Community.findPrivateForUser");
		q.setParameter("person", person);
		List<Community> privateCommunity = q.getResultList();
		return privateCommunity.size() > 0 ? privateCommunity.get(0) : null;
	}
}
