package in.maitra.treats.invento.repo.jdbc;

import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.util.filter.EntityFilterableFieldRegistry;
import in.maitra.treats.invento.util.filter.FilterTuple;
import in.maitra.treats.invento.util.filter.Operator;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public abstract class AbstractJdbcRepo {

    @Value("${application.datasource.max-batch-size:1000}")
    protected Integer maxBatchSize;

    protected QueryParts buildWhereClause(PaginationFilter filter) throws InventoException {
        if (filter == null || filter.getFilterTuples() == null || filter.getFilterTuples().isEmpty()) {
            return new QueryParts("", List.of());
        }

        List<String> clauses = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        for (FilterTuple tuple : filter.getFilterTuples()) {
            String column = EntityFilterableFieldRegistry.getColumnNameForFilterableField(tuple.getEntityName(), tuple.getFieldName());
            if (column == null) {
                throw new InventoException("Unsupported filter field " + tuple.getFieldName() + " for entity " + tuple.getEntityName());
            }
            clauses.add(buildCondition(deriveTableAlias(tuple.getEntityName(), tuple.getFieldName()), column, tuple, params));
        }

        return new QueryParts(" WHERE " + String.join(" AND ", clauses), params);
    }

    protected abstract String deriveTableAlias(String entityName, String fieldName);

    private String buildCondition(String tableAlias, String column, FilterTuple tuple, List<Object> params) {
        Operator operator = tuple.getComparisonOperator() == null ? Operator.EQUALS : tuple.getComparisonOperator();
        List<Object> values = tuple.getFilterValues() == null ? List.of() : tuple.getFilterValues();
        return switch (operator) {
            case EQUALS -> addSingleValueCondition(tableAlias, column, "=", values, params);
            case NOT_EQUALS -> addSingleValueCondition(tableAlias, column, "<>", values, params);
            case GREATER_THAN -> addSingleValueCondition(tableAlias, column, ">", values, params);
            case GREATER_THAN_OR_EQUAL_TO -> addSingleValueCondition(tableAlias, column, ">=", values, params);
            case LESS_THAN -> addSingleValueCondition(tableAlias, column, "<", values, params);
            case LESS_THAN_OR_EQUAL_TO -> addSingleValueCondition(tableAlias, column, "<=", values, params);
            case LIKE -> addSingleValueCondition(tableAlias, column, "LIKE", values, params);
            case NOT_LIKE -> addSingleValueCondition(tableAlias, column, "NOT LIKE", values, params);
            case STARTS_WITH -> addPatternCondition(tableAlias, column, values, params, "", "%");
            case ENDS_WITH -> addPatternCondition(tableAlias, column, values, params, "%", "");
            case CONTAINS_STRING -> addPatternCondition(tableAlias, column, values, params, "%", "%");
            case IN -> addInCondition(tableAlias, column, values, params, false);
            case NOT_IN -> addInCondition(tableAlias, column, values, params, true);
            case IS_NULL -> tableAlias + "." + column + " IS NULL";
            case IS_NOT_NULL -> tableAlias + "." + column + " IS NOT NULL";
        };
    }

    private String addSingleValueCondition(String tableAlias, String column, String sqlOperator, List<Object> values, List<Object> params) {
        params.add(normalizeFilterValue(firstFilterValue(values)));
        return tableAlias + "." + column + " " + sqlOperator + " ?";
    }

    private String addPatternCondition(String tableAlias, String column, List<Object> values, List<Object> params, String prefix, String suffix) {
        params.add(prefix + normalizeFilterValue(firstFilterValue(values)) + suffix);
        return tableAlias + "." + column + " LIKE ?";
    }

    private String addInCondition(String tableAlias, String column, List<Object> values, List<Object> params, boolean negated) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Filter operator " + (negated ? Operator.NOT_IN : Operator.IN) + " requires at least one value");
        }

        StringJoiner placeholders = new StringJoiner(", ");
        for (Object value : values) {
            placeholders.add("?");
            params.add(normalizeFilterValue(value));
        }
        return tableAlias + "." + column + (negated ? " NOT IN (" : " IN (") + placeholders + ")";
    }

    private Object firstFilterValue(List<Object> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Filter operator requires a value");
        }
        return values.get(0);
    }

    private Object normalizeFilterValue(Object value) {
        return value instanceof Enum<?> enumValue ? enumValue.name() : value;
    }

    protected record QueryParts(String whereClause, List<Object> params) {
    }
}
