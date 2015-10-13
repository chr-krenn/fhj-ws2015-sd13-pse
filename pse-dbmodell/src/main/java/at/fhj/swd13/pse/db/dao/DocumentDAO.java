package at.fhj.swd13.pse.db.dao;

import at.fhj.swd13.pse.db.entity.Document;

public interface DocumentDAO {

	void insert(Document document);

	void remove(Document document);

	Document getById(int documentId);

}