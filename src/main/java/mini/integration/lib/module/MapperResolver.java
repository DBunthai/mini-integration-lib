package mini.integration.lib.module;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Component
public class MapperResolver {

    public String map(OffsetDateTime dateTime) {
        return Objects.nonNull(dateTime) ? dateTime.withOffsetSameInstant(ZoneOffset.UTC).toString() : null;
    }

    public OffsetDateTime map(String value) {
        return Objects.nonNull(value) ? OffsetDateTime.parse(value) : null;
    }
}
