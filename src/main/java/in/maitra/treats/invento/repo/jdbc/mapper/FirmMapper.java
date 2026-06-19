package in.maitra.treats.invento.repo.jdbc.mapper;

import in.maitra.treats.invento.entity.Firm;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FirmMapper extends AbstractMapper<Firm> {

    public FirmMapper(String tableAliasInQuery) {
        super(tableAliasInQuery);
    }

    private static final String ID_COLUMN_NAME = "id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String STATUS_COLUMN_NAME = "status";

    @Override
    public Firm createEntityFromResultSet(ResultSet resultSet) throws SQLException {
        Firm firm = new  Firm();
        EntityId firmId = mapEntityId(resultSet.getObject(deriveResultSetColumnName(ID_COLUMN_NAME)));
        firm.setId(firmId);
        firm.setName(resultSet.getString(deriveResultSetColumnName(NAME_COLUMN_NAME)));
        firm.setStatus(mapStatus(resultSet.getString(deriveResultSetColumnName(STATUS_COLUMN_NAME))));
        return firm;
    }
}
