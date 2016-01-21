package at.fhj.swd13.pse.db;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

public abstract class DAOBase {

	protected final DbContext dbContext;

	protected DAOBase(DbContext dbContext) {

		this.dbContext = dbContext;
	}

	/**
	 * Fetches a single result from a query
	 * 
	 * @param q
	 *            the query from which to fetch the result
	 * 
	 * @return single instance form the query or null if result was empty
	 */
	@SuppressWarnings({ "unchecked","squid:S1166" })
	protected <T> T fetchSingle(final Query q) {
		try {

			return (T)q.getSingleResult();

		} catch (NoResultException | NonUniqueResultException x) {

			return null;
		}
	}
}
