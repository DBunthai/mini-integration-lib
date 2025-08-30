package mini.integration.lib.module.exception.handling;

import mini.integration.lib.module.exception.GeneralException;

public class InvalidArgumentException extends GeneralException {
    public InvalidArgumentException(String message) {
        super(message);
    }
}
