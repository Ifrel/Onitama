package Exceptions;

/**
 * On obtient cette exception en tentant d''effetcuer un déplacement illégal
 */
public class ConfigurationIllegaleException extends Exception {


    public ConfigurationIllegaleException(String message) {
        super(message);
    }

    public ConfigurationIllegaleException(Throwable throwable) {
        super(throwable);
    }

    public ConfigurationIllegaleException() {

    }
}
