package mini.integration.lib.module.exception.handling;

import mini.integration.lib.module.exception.GeneralException;

public class BusinessRuleException extends GeneralException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
