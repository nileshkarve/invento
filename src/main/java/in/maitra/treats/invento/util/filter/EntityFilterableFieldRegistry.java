package in.maitra.treats.invento.util.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EntityFilterableFieldRegistry {
    private EntityFilterableFieldRegistry() {}

    private static final Map<String, List<FilterableField>> entityFilterableFieldsMap = new HashMap<>();

    public static void registerFilterableField(String entityName, String fieldName, String columnName) {
        List<FilterableField> filterableFields = entityFilterableFieldsMap.getOrDefault(entityName, new ArrayList<>());
        filterableFields.add(new EntityFilterableFieldRegistry.FilterableField(fieldName, columnName));
        entityFilterableFieldsMap.put(entityName, filterableFields);
    }

    public static String getColumnNameForFilterableField(String entityName, String fieldName) {
        return entityFilterableFieldsMap.get(entityName).stream()
                .filter(field -> field.getName().equals(fieldName))
                .map(FilterableField::getColumnName)
                .findFirst()
                .orElse(null);
    }

    @Getter
    @RequiredArgsConstructor
    private static class FilterableField {
        private final String name;
        private final String columnName;
    }
}
