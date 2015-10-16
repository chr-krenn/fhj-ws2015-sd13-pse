package at.fhj.swd13.pse.xperimental;

public class MessageDTO {

	private String headLine;
	private String body;
	private String iconRef;
	private String documentRef;

	public MessageDTO() {
		headLine = "";
		body = "";
		iconRef = "";
		documentRef = "";
	}

	/**
	 * @return the headLine
	 */
	public String getHeadLine() {
		return headLine;
	}

	/**
	 * @param headLine
	 *            the headLine to set
	 */
	public void setHeadLine(String headLine) {
		this.headLine = headLine;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param boday
	 *            the boday to set
	 */
	public void setBody(String boday) {
		this.body = boday;
	}

	/**
	 * @return the iconRef
	 */
	public String getIconRef() {
		return iconRef;
	}

	/**
	 * @param iconRef
	 *            the iconRef to set
	 */
	public void setIconRef(String iconRef) {
		this.iconRef = iconRef;
	}

	/**
	 * @return the documentRef
	 */
	public String getDocumentRef() {
		return documentRef;
	}

	/**
	 * @param documentRef
	 *            the documentRef to set
	 */
	public void setDocumentRef(String documentRef) {
		this.documentRef = documentRef;
	}

}
