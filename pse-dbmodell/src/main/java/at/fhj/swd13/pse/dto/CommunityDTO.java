package at.fhj.swd13.pse.dto;

import at.fhj.swd13.pse.db.entity.Community;


/**
 * DTO for the community entity
 *
 */
public class CommunityDTO {

	/**
	 * Name of the community
	 */
	private String name;
	
	/**
	 * Id of the community as token
	 */
	private String token;

	/**
	 * Parameterless ctor required for construction by the magic framework (lots of glitter used!)
	 */
	public CommunityDTO() {}
	
	public CommunityDTO( final String token, final String name ) {
		this.token = token;
		this.name = name;
	}
	
	/**
	 * 
	 * Ctor to create a new CommunityDTO from a community instance
	 * @param community
	 * The community which is the source for the new CommunityD
	 */
	public CommunityDTO(Community community){
		this.token = Integer.toString(community.getCommunityId());
		this.name = community.getName();
	}
	
	@Override
	public String toString() {
		return name; 
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * 
	 * @param token
	 */
	public void setToken(final String token) {
		this.token = token;
	}		
}
