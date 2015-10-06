package at.fhj.swd13.pse.db.dao;

import javax.persistence.Query;

import at.fhj.swd13.pse.db.DAOBase;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Community;
import at.fhj.swd13.pse.db.entity.ParameterException;

public class CommunityDAOImpl extends DAOBase implements CommunityDAO {

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
	public void insert(Community community) {

		dbContext.persist(community);
	}

	public Community getByName(final String name) {

		final Query q = dbContext.createNamedQuery("Community.findByName");
		q.setParameter("name", name);

		return fetchSingle(q);
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
}
