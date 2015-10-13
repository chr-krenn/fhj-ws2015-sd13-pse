package at.fhj.swd13.pse.dto;

import at.fhj.swd13.pse.db.entity.Person;

public class UserDTO {

	private String userName;

	private String lastName;

	private String firstName;

	private String emailAddress;

	private String phoneNumberMobile;

	private String imageRef;

	private String department;

	private String locationRoomNumber;

	private String job;

	public UserDTO() {}

	public UserDTO(Person person) {
		this.userName = person.getUserName();
		this.lastName = person.getLastName();
		this.firstName = person.getFirstName();
		this.emailAddress = person.getEmailAddress();
		this.phoneNumberMobile = person.getPhoneNumberMobile();
		this.department = person.getDepartment();
		this.locationRoomNumber = person.getLocationRoomNumber();
		this.job = person.getJobPosition();
		
		if (person.getDocument() != null) {
			//TODO: hardcoded...
			this.imageRef = "/store/media/" + person.getDocument().getDocumentId();
		} else {
			//TODO: hardcoded...
			this.imageRef = "/protected/img/no_img.jpg";
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getFullname() {
		return lastName + ", " + firstName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
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
	 * @param firstName
	 *            the firstName to set
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
	 * @param imageRef
	 *            the imageRef to set
	 */
	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return this.emailAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the locationRoomNumber
	 */
	public String getLocationRoomNumber() {
		return this.locationRoomNumber;
	}

	/**
	 * @param locationRoomNumber
	 *            the locationRoomNumber to set
	 */
	public void setLocationRoomNumber(String locationRoomNumber) {
		this.locationRoomNumber = locationRoomNumber;
	}

	/**
	 * @return the job
	 */
	public String getJob() {
		return job;
	}

	/**
	 * @param job
	 *            the job to set
	 */
	public void setJob(String job) {
		this.job = job;
	}

	/**
	 * @return the phoneNumberMobile
	 */
	public String getPhoneNumberMobile() {
		return phoneNumberMobile;
	}

	/**
	 * @param phoneNumberMobile
	 *            the phoneNumberMobile to set
	 */
	public void setPhoneNumberMobile(String phoneNumberMobile) {
		this.phoneNumberMobile = phoneNumberMobile;
	}
}
