package at.fhj.swd13.pse.db.dao;

import javax.persistence.Query;

import at.fhj.swd13.pse.db.DAOBase;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.DeliverySystem;

public class DeliverySystemDAOImpl extends DAOBase implements DeliverySystemDAO {

	public DeliverySystemDAOImpl( DbContext dbContext ) {
		super(dbContext);
	}
	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.DeliverySystemDAO#get(java.lang.String)
	 */
	@Override
	public DeliverySystem get(final String token) {

		Query q = dbContext.createNamedQuery("DeliverySystem.findByToken");
		q.setParameter("token", token);

		return fetchSingle(q);

	}
	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.DeliverySystemDAO#getPseService()
	 */
	@Override
	public DeliverySystem getPseService() {
		return get( SYSTEM_PSE );
	}
}
