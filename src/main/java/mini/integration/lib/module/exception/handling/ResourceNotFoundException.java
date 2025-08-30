package mini.integration.lib.module.exception.handling;

import mini.integration.lib.module.exception.GeneralException;

public class ResourceNotFoundException extends GeneralException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
