package at.fhj.swd13.pse.domain.chat;

import java.util.List;

import at.fhj.swd13.pse.db.DbContext;
import at.fhj.swd13.pse.db.entity.Tag;
import at.fhj.swd13.pse.service.ServiceBase;

/**
 * provide all tag related services
 * 
 * @author Gustav Gans
 *
 */
public class TagServiceImpl extends ServiceBase implements TagService {

	/**
	 * parameterless ctor needed for injection to work
	 */
	public TagServiceImpl() {
		super();
	}

	/**
	 * ctor for testing purpses mainly
	 * 
	 * @param dbContext
	 */
	public TagServiceImpl(DbContext dbContext) {
		super(dbContext);
	}

	/* (non-Javadoc)
	 * @see at.fhj.swd13.pse.domain.chat.TagService#getMatchingTags(java.lang.String)
	 */
	@Override
	public List<Tag> getMatchingTags(final String needle) {

		return dbContext.getTagDAO().getByTokenLike(needle);
	}
}
