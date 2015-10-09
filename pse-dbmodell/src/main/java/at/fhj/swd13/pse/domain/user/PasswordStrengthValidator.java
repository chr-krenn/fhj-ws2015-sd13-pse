package at.fhj.swd13.pse.domain.user;

public interface PasswordStrengthValidator
{
	void validate(String password) throws WeakPasswordException;
}
