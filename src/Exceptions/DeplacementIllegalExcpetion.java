package Exceptions;

/**
 * On obtient cette exception en tentant d''effetcuer un déplacement illégal
 */
public class DeplacementIllegalExcpetion extends Exception {


    public DeplacementIllegalExcpetion(String message) {
        super(message);
    }

    public DeplacementIllegalExcpetion(Throwable throwable) {
        super(throwable);
    }

    public DeplacementIllegalExcpetion() {

    }
}
