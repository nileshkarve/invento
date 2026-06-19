package in.maitra.treats.invento.repo.jdbc.mapper;

import in.maitra.treats.invento.util.persistence.EntityId;
import in.maitra.treats.invento.util.persistence.PersistableEntity;
import in.maitra.treats.invento.util.persistence.Status;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractMapper<T extends PersistableEntity> {

    private final String tableAliasInQuery;

    protected EntityId mapEntityId(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof byte[] bytes) {
            return EntityId.fromBytes(bytes);
        }
        if (value instanceof UUID uuid) {
            return EntityId.fromString(uuid.toString());
        }
        return EntityId.fromString(value.toString());
    }

    protected Status mapStatus(String value) {
        return value == null ? null : Status.valueOf(value);
    }

    protected String deriveResultSetColumnName(String columnName) {
        return tableAliasInQuery.concat("_").concat(columnName);
    }

    public abstract T createEntityFromResultSet(ResultSet resultSet) throws SQLException;
}
