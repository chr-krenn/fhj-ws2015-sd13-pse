package at.fhj.swd13.pse.dto;

import javax.ejb.Stateless;
import javax.inject.Inject;

import at.fhj.swd13.pse.db.entity.Person;
import at.fhj.swd13.pse.domain.document.DocumentService;

@Stateless
public class UserDTOBuilder {

	@Inject
	private DocumentService documentService;

	
	public UserDTO createFrom( final Person person ) {
		
		UserDTO newDTO = new UserDTO( person );
		
		if ( person.getDocument() != null ) {
			newDTO.setImageRef( documentService.buildServiceUrl( person.getDocument().getDocumentId() ) ) ;
		}
		else {
			newDTO.setImageRef( documentService.getDefaultDocumentRef( DocumentService.DocumentCategory.USER_IMAGE ) );
		} 
		
		return newDTO;
		
	}
}
