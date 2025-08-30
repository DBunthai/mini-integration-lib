package mini.integration.lib.module;

import mini.integration.lib.module.exception.GeneralException;

public interface QueryHandler<T, R> {
    R handle(T query) throws GeneralException;
}
