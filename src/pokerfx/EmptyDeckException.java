package pokerfx;

public class EmptyDeckException extends RuntimeException {

	/**
	 * This runtime exception is thrown, if deck runs out of cards.
	 */
	private static final long serialVersionUID = 1L;

	public EmptyDeckException() {
		System.out.println("Deck is empty()");
	}

	public EmptyDeckException(String message) {
		super(message);
		System.out.println("Deck is empty(message)");
	}

	public EmptyDeckException(Throwable cause) {
		super(cause);
		System.out.println("Deck is empty(cause)");
	}

	public EmptyDeckException(String message, Throwable cause) {
		super(message, cause);
		System.out.println("Deck is empty(message, cause)");
	}
}