package at.fhj.swd13.pse.plumbing;

import javax.persistence.Column;

public abstract class JpaHelper {

	/**
	 * get the max allowed length of the field from the @Column annotation
	 * 
	 * @param target
	 * 
	 * @param fieldName
	 * 
	 * @return
	 * 
	 * @throws NoSuchFieldException
	 * 
	 * @throws SecurityException
	 */
	public static int getColumneLength(final Object target, final String fieldName) throws NoSuchFieldException, SecurityException {

		Column col = target.getClass().getDeclaredField(fieldName).getAnnotation(Column.class);

		if (col != null) {
			return col.length();
		} else {
			return 0;
		}
	}

	/**
	 * get the max allowed length of the field from the @Column annotation
	 * 
	 * @param target
	 * 
	 * @param fieldName
	 * 
	 * @return
	 * 
	 * @throws NoSuchFieldException
	 * 
	 * @throws SecurityException
	 */
	public static int getColumneLength(final Class<?> target, final String fieldName) {

		try {

			Column col;

			col = target.getDeclaredField(fieldName).getAnnotation(Column.class);

			if (col != null) {
				return col.length();
			} else {
				return 0;
			}
		} catch (NoSuchFieldException | SecurityException e) {
			return 0;
		}

	}
}
