package at.fhj.swd13.pse.domain.chat;

import java.util.List;

import at.fhj.swd13.pse.db.entity.Tag;

public interface TagService {

	List<Tag> getMatchingTags(String needle);

}