package Exceptions;

/**
 * On obtient cette exception en tentant d'accéder à une case vide
 */
public class CaseVideException extends Exception {


    public CaseVideException(String message) {
        super(message);
    }

    public CaseVideException(Throwable throwable) {
        super(throwable);
    }

    public CaseVideException() {

    }
}
