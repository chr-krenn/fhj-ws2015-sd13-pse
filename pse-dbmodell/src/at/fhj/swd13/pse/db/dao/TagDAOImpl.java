package at.fhj.swd13.pse.db.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Tag;

public class TagDAOImpl implements TagDAO {

	private final DbContext dbContext;

	public TagDAOImpl(DbContext dbContext) {

		this.dbContext = dbContext;
	}

	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.TagDAO#insert(at.fhj.swd13.pse.db.entity.Tag)
	 */
	public void insert( Tag tag ) {
		
		dbContext.persist( tag );
	}
	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.TagDAO#getById(int)
	 */
	@Override
	public Tag getById(int tagId) {

		final Query q = dbContext.createNamedQuery("Tag.findById");
		q.setParameter("id", tagId);

		try {

			return (Tag) q.getSingleResult();
		} catch (NoResultException | NonUniqueResultException x) {

			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.TagDAO#getByTokenLike(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Tag> getByTokenLike( String beginning ) {
		
		final Query q = dbContext.createNamedQuery( "Tag.findByTokenLike" );
		q.setParameter("token", beginning+"%" );
		
		return q.getResultList();
	}
	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.TagDAO#remove(int)
	 */
	public void remove( int tagId ) {
		
		final Query q = dbContext.createNamedQuery( "Tag.deleteById");
		q.setParameter("id", tagId);
		
		q.executeUpdate();
	}
}
