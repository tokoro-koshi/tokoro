package com.tokorokoshi.tokoro.modules.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.*;
import org.springframework.ai.converter.BeanOutputConverter;

public class JsonHelper {
    public static <T> String getJsonSchema(Class<T> type) {
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(
                SchemaVersion.DRAFT_2020_12,
                OptionPreset.PLAIN_JSON
        );
        SchemaGeneratorConfig config = configBuilder
                .with(Option.EXTRA_OPEN_API_FORMAT_VALUES)
                .without(Option.FLATTENED_ENUMS_FROM_TOSTRING)
                .build();

        SchemaGenerator generator = new SchemaGenerator(config);
        JsonNode jsonSchema = generator.generateSchema(type);

        System.out.println(jsonSchema.asText());
        System.out.println(jsonSchema.toPrettyString());

        return jsonSchema.toPrettyString();
    }

    public static <T> T fromJson(String json, Class<T> type) {
        var converter = new BeanOutputConverter<>(type);
        return converter.convert(json);
    }
}
