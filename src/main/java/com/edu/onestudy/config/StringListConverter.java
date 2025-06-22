package com.edu.onestudy.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final String SPLIT_CHAR = ","; // Or any other suitable delimiter

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            return null;
        }
        // For TEXT[], PostgreSQL expects the array literal format, e.g., "{tag1,tag2}"
        // However, Hibernate Types handles this directly. For a simple comma-separated
        // string, this converter is a fallback.
        // For true TEXT[] mapping with hibernate-types-52, you might not need this converter
        // if you use @TypeDef(name = "list-array", typeClass = ListArrayType.class)
        // and a List<String> field. But it's good for demonstration if you want manual control.
        return "{" + String.join(SPLIT_CHAR, stringList) + "}";
    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        // Remove curly braces if present from PostgreSQL array literal
        String cleanedString = string.replaceAll("^\\{|\\}$", "");
        return Arrays.asList(cleanedString.split(SPLIT_CHAR));
    }
}