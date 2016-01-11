package at.fhj.swd13.pse.db.dao;

import javax.persistence.Query;

import at.fhj.swd13.pse.db.DAOBase;
import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Document;

public class DocumentDAOImpl extends DAOBase implements DocumentDAO {

	public DocumentDAOImpl(DbContext dbContext) {
		super(dbContext);
	}

	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.DocumentDAO#insert(at.fhj.swd13.pse.db.entity.Document)
	 */
	@Override
	public void insert(Document document){
		dbContext.persist(document);
	}

	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.DocumentDAO#remove(at.fhj.swd13.pse.db.entity.Document)
	 */
	@Override
	public void remove(Document document) {
		if (document == null || document.getDocumentId() == 0) {

			throw new IllegalArgumentException("document null or not persisted yet");
		}
		dbContext.remove(document);
	}

	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.DocumentDAO#remove(int)
	 */
	public void remove( int documentId ) {
	
		final Query q = dbContext.createNamedQuery("Document.deleteById");
		q.setParameter("id", documentId);
		
		
		q.executeUpdate();
	}
	
	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.db.dao.DocumentDAO#getById(int)
	 */
	@Override
	public Document getById(final int documentId) {

		Query q = dbContext.createNamedQuery("Document.findById");
		q.setParameter("id", documentId);
		
		return fetchSingle(q);
	}
}
