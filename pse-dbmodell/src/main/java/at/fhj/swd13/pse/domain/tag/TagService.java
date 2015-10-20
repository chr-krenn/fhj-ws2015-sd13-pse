package at.fhj.swd13.pse.domain.tag;

import java.util.List;

import at.fhj.swd13.pse.db.entity.Tag;

public interface TagService {

	List<Tag> getMatchingTags(String needle);
	
	Tag getTagByToken(String token);
	
}