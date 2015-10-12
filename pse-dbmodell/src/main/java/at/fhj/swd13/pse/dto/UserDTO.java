package at.fhj.swd13.pse.dto;

import at.fhj.swd13.pse.db.entity.Person;

public class UserDTO {
	
	
	private String lastName;
	
	private String firstName;
	
	private String imageRef;
	
	private String department;
	
	private String job;

	
	public UserDTO() {}
	
	public UserDTO( Person person ) {
		this.lastName = person.getLastName();
		this.firstName = person.getFirstName();
		this.department = person.getDepartment();
		this.job = person.getJobPosition();
		this.imageRef = "/protected/img/no_img.jpg";
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFullname() {
		return lastName +", " +  firstName;
	}


	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}


	
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}


	
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	
	/**
	 * @return the imageRef
	 */
	public String getImageRef() {
		return imageRef;
	}


	
	/**
	 * @param imageRef the imageRef to set
	 */
	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}


	
	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}


	
	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}


	
	/**
	 * @return the job
	 */
	public String getJob() {
		return job;
	}


	
	/**
	 * @param job the job to set
	 */
	public void setJob(String job) {
		this.job = job;
	}	
}
