package clovis.server.db

/**
 * Something went wrong while accessing the database.
 */
sealed class DatabaseProblem(val exception: Throwable) {

	/**
	 * Something went wrong, but we couldn't recognize why.
	 */
	class Unknown(exception: Throwable) : DatabaseProblem(exception)

	/**
	 * A database constraint was violated.
	 */
	class ConstraintViolation(exception: Throwable) : DatabaseProblem(exception)

}
