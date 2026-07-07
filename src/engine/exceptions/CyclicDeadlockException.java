package engine.exceptions;

public class CyclicDeadlockException extends RuntimeException{
    public CyclicDeadlockException(String message){
        super(message);
    }
}
