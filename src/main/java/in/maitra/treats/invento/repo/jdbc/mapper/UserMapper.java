package in.maitra.treats.invento.repo.jdbc.mapper;

import in.maitra.treats.invento.entity.User;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper extends AbstractMapper<User> {

    public UserMapper(String tableAliasInQuery) {
        super(tableAliasInQuery);
    }

    private static final String ID_COLUMN_NAME = "id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String EMAIL_COLUMN_NAME = "email";
    private static final String CONTACT_NUMBER_COLUMN_NAME = "contact_number";
    private static final String STATUS_COLUMN_NAME = "status";

    @Override
    public User createEntityFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new  User();
        EntityId userId = mapEntityId(resultSet.getObject(deriveResultSetColumnName(ID_COLUMN_NAME)));
        user.setId(userId);
        user.setName(resultSet.getString(deriveResultSetColumnName(NAME_COLUMN_NAME)));
        user.setEmail(resultSet.getString(deriveResultSetColumnName(EMAIL_COLUMN_NAME)));
        user.setContactNumber(resultSet.getString(deriveResultSetColumnName(CONTACT_NUMBER_COLUMN_NAME)));
        user.setStatus(mapStatus(resultSet.getString(deriveResultSetColumnName(STATUS_COLUMN_NAME))));
        return user;
    }
}
