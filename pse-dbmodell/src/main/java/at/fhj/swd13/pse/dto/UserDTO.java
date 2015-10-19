package at.fhj.swd13.pse.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.fhj.swd13.pse.db.entity.Person;

public class UserDTO {

	private String userName;

	private String lastName;

	private String firstName;

	private String emailAddress;

	private Date dateOfBirth;

	private Date dateOfEntry;

	private String phoneNumberMobile;

	private String imageRef;

	private int imageId;

	private String department;

	private String locationBuilding;

	private String locationRoomNumber;

	private Integer locationFloor;

	private String job;

	private List<Person> contacts;

	public UserDTO() {
	}

	public UserDTO(Person person) {
		this.userName = person.getUserName();
		this.lastName = person.getLastName();
		this.firstName = person.getFirstName();
		this.emailAddress = person.getEmailAddress();
		this.dateOfBirth = person.getDateOfBirth();
		this.dateOfEntry = person.getDateOfEntry();
		this.phoneNumberMobile = person.getPhoneNumberMobile();
		this.department = person.getDepartment();
		this.locationBuilding = person.getLocationBuilding();
		this.locationRoomNumber = person.getLocationRoomNumber();
		this.locationFloor = person.getLocationFloor();
		this.job = person.getJobPosition();
		this.contacts = new ArrayList<Person>(person.getContacts());
		if (person.getDocument() != null) {
			this.imageId = person.getDocument().getDocumentId();
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
	 * 
	 * @return
	 */
	public int getImageId() {
		return imageId;
	}

	/**
	 * 
	 * @param imageId
	 *            the imageId to set
	 */
	public void setImageId(int imageId) {
		this.imageId = imageId;
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

	/**
	 * @return the locationFloor
	 */
	public Integer getLocationFloor() {
		return locationFloor;
	}

	/**
	 * @param locationFloor
	 *            the locationFloor to set
	 */
	public void setLocationFloor(Integer locationFloor) {
		this.locationFloor = locationFloor;
	}

	/**
	 * @return the locationBuilding
	 */
	public String getLocationBuilding() {
		return locationBuilding;
	}

	/**
	 * @param locationBuilding
	 *            the locationBuilding to set
	 */
	public void setLocationBuilding(String locationBuilding) {
		this.locationBuilding = locationBuilding;
	}

	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the dateOfEntry
	 */
	public Date getDateOfEntry() {
		return dateOfEntry;
	}

	/**
	 * @param dateOfEntry
	 *            the dateOfEntry to set
	 */
	public void setDateOfEntry(Date dateOfEntry) {
		this.dateOfEntry = dateOfEntry;
	}

	public List<Person> getContacts() {
		return contacts;
	}

	public void setContacts(List<Person> contacts) {
		this.contacts = contacts;
	}
}
