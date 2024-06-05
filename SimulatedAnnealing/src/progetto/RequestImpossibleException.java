package progetto;



public class RequestImpossibleException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public RequestImpossibleException() {
		super();
	}

	public RequestImpossibleException(String message) {
		super(message);
	}
}
