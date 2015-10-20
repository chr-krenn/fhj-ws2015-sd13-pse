package at.fhj.swd13.pse.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import at.fhj.swd13.pse.domain.document.DocumentLibraryEntry;
import at.fhj.swd13.pse.domain.document.DocumentLibraryService;

@ManagedBean
@RequestScoped
public class DocumentLibraryController {

	@ManagedProperty("#{param.communityId}")
	private int communityId;
	
	@Inject
	private DocumentLibraryService documentLibraryService;

	public DocumentLibraryController() {
		
	}
	
	public int getCommunityId() {
		return communityId;
	}
	
	public List<DocumentLibraryEntry> getEntries()
	{
		return documentLibraryService.getEntriesForCommunity(communityId);
	}
	
	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}
	

	@PostConstruct
	public void init() {
		//If no community id has been provided, use the default community
		if(communityId == 0)
			communityId = 1;
	}
}
