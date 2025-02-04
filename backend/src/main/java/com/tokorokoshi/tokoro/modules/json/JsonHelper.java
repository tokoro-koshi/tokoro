package com.tokorokoshi.tokoro.modules.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.*;
import org.springframework.ai.converter.BeanOutputConverter;

/**
 * A helper class for JSON operations.
 */
public class JsonHelper {
    /**
     * Get the JSON schema for a class.
     *
     * @param type The class to get the schema for
     * @param <T>  The type of the class
     * @return The JSON schema
     */
    public static <T> String getJsonSchema(Class<T> type) {
        // Set up schema configuration
        SchemaGeneratorConfigBuilder configBuilder =
                new SchemaGeneratorConfigBuilder(
                        SchemaVersion.DRAFT_2020_12,
                        OptionPreset.PLAIN_JSON
                );

        // Add additional properties false to all objects
        SchemaGeneratorConfig config = configBuilder
                .with(Option.EXTRA_OPEN_API_FORMAT_VALUES)
                .without(Option.FLATTENED_ENUMS_FROM_TOSTRING)
                .build();

        // Generate schema
        SchemaGenerator generator = new SchemaGenerator(config);
        JsonNode jsonSchema = generator.generateSchema(type);

        // Add additional properties false to all objects
        addAdditionalPropertiesFalse(jsonSchema);

        // Add required fields
        addRequired(jsonSchema);

        return jsonSchema.toPrettyString();
    }

    /**
     * Add additionalProperties: false to all objects in the JSON schema.
     *
     * @param node The node to add additionalProperties: false to
     */
    private static void addAdditionalPropertiesFalse(JsonNode node) {
        if (!node.isObject()) return;

        ObjectNode objectNode = (ObjectNode) node;

        // Check if the object contains "properties" and set additionalProperties to false
        if (objectNode.has("properties")) {
            objectNode.put(
                    "additionalProperties",
                    false
            );
        }

        // Recursively apply to all objects inside
        objectNode.fields()
                  .forEachRemaining(entry -> addAdditionalPropertiesFalse(entry.getValue()));
    }

    /**
     * Add required fields to all objects in the JSON schema.
     *
     * @param node The node to add required fields to
     */
    private static void addRequired(JsonNode node) {
        if (!node.isObject()) return;
        ObjectNode objectNode = (ObjectNode) node;

        // Check if the object contains "properties" and set additionalProperties to false
        if (objectNode.has("properties")) {
            var requiredIt = objectNode.get("properties").fieldNames();
            ArrayNode required = objectNode.putArray("required");
            requiredIt.forEachRemaining(required::add);
        }

        // Recursively apply to all objects inside
        objectNode.fields()
                  .forEachRemaining(entry -> addRequired(entry.getValue()));
    }

    /**
     * Obtain an object from a JSON string.
     *
     * @param json The JSON string
     * @param type The type of the object
     * @param <T>  The type of the object
     * @return The object
     */
    public static <T> T fromJson(String json, Class<T> type) {
        var converter = new BeanOutputConverter<>(type);
        return converter.convert(json);
    }
}
