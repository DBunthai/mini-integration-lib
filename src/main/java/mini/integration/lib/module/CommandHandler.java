package mini.integration.lib.module;

import mini.integration.lib.module.exception.GeneralException;

public interface CommandHandler<T, R> {
    R handle(T command) throws GeneralException;
}
