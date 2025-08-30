package mini.integration.lib.module.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectMapperLib {

    public enum ObjectMapperRule {
        RESTRICTED, UNRESTRICTED
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectMapper objectMapperStrict;
    private static final Map<ObjectMapperRule, ObjectMapper> registry = new HashMap<>();


    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapperStrict = objectMapper.copy();

        registry.put(ObjectMapperRule.RESTRICTED, objectMapperStrict());
        registry.put(ObjectMapperRule.UNRESTRICTED, objectMapperUnStrict());
    }

    private static ObjectMapper objectMapperStrict() {

        return objectMapper;
    }

    private static ObjectMapper objectMapperUnStrict() {
        return objectMapperStrict.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static ObjectMapper objectMapper() {
        return registry.get(ObjectMapperRule.RESTRICTED);
    }

    public static ObjectMapper objectMapper(ObjectMapperRule objectMapperRule) {
        return registry.getOrDefault(objectMapperRule, objectMapperStrict);
    }

    public static <T, E> List<E> mapList(List<T> list, Class<E> map, ObjectMapperRule objectMapperRule) {
        return list.stream().map(m -> objectMapper(objectMapperRule).convertValue(m, map)).collect(Collectors.toList());
    }

    public static <T, E> E mapObj(T obj, Class<E> map, ObjectMapperRule objectMapperRule) {
        return objectMapper(objectMapperRule).convertValue(obj, map);
    }
}
