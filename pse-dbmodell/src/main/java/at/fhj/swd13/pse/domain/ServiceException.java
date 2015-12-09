package at.fhj.swd13.pse.domain;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -6039587106876295562L;

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}
}
