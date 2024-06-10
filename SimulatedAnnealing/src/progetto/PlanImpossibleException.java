package progetto;

public class PlanImpossibleException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public PlanImpossibleException() {
		super();
	}

	public PlanImpossibleException(String message) {
		super("no feasible plan found: \n" + message);
	}

}