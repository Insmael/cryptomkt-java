package com.cryptomkt.api;

import com.cryptomkt.api.deserializer.MarketDeserializer;
import com.cryptomkt.api.entity.Market;
import com.cryptomkt.api.serializer.MarketSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class ObjectMapperProvider {

    final ObjectMapper defaultObjectMapper;

    public ObjectMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
    }

    public ObjectMapper getContext(Class<?> type) {
        return defaultObjectMapper;
    }

    public static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();

        result.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        result.configure(SerializationFeature.INDENT_OUTPUT, false);
        result.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        result.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        result.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Market.class, new MarketDeserializer());
        module.addSerializer(Market.class, new MarketSerializer());
        result.registerModule(module);

        result.registerModule(new JodaModule());

        return result;
    }
}
