package in.maitra.treats.invento.repo.jdbc.mapper;

import in.maitra.treats.invento.entity.Product;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper extends AbstractMapper<Product> {
    private static final String ID_COLUMN_NAME = "id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String DESCRIPTION_COLUMN_NAME = "description";
    private static final String MRP_COLUMN_NAME = "mrp";
    private static final String STATUS_COLUMN_NAME = "status";

    public ProductMapper(String tableAliasInQuery) {
        super(tableAliasInQuery);
    }

    @Override
    public Product createEntityFromResultSet(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        EntityId id = mapEntityId(resultSet.getObject(deriveResultSetColumnName(ID_COLUMN_NAME)));
        product.setId(id);
        product.setName(resultSet.getString(deriveResultSetColumnName(NAME_COLUMN_NAME)));
        product.setDescription(resultSet.getString(deriveResultSetColumnName(DESCRIPTION_COLUMN_NAME)));
        product.setMrp(resultSet.getBigDecimal(deriveResultSetColumnName(MRP_COLUMN_NAME)));
        product.setStatus(mapStatus(resultSet.getString(deriveResultSetColumnName(STATUS_COLUMN_NAME))));
        return product;
    }
}
