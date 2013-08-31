package volkov;
public class MongoDbException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MongoDbException() {
	}

	public MongoDbException(String message) {
		super(message);
	}
}
