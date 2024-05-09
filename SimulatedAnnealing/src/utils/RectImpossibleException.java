package utils;



public class RectImpossibleException extends Exception{
	
	public RectImpossibleException() {
		super();
	}

	//quando viene creato per sbaglio un rettangolo impossibile, viene lanciata
	public RectImpossibleException(String message) {
		super(message);
	}
}
