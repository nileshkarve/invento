package in.maitra.treats.invento.repo.jdbc.mapper;

import in.maitra.treats.invento.entity.ProductCategory;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductCategoryMapper extends AbstractMapper<ProductCategory> {

    private static final String ID_COLUMN_NAME = "id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String DESCRIPTION_COLUMN_NAME = "description";
    private static final String STATUS_COLUMN_NAME = "status";

    public ProductCategoryMapper(String tableAliasInQuery) {
        super(tableAliasInQuery);
    }

    @Override
    public ProductCategory createEntityFromResultSet(ResultSet resultSet) throws SQLException {
        ProductCategory productCategory = new ProductCategory();
        EntityId id = mapEntityId(resultSet.getObject(deriveResultSetColumnName(ID_COLUMN_NAME)));
        productCategory.setId(id);
        productCategory.setName(resultSet.getString(deriveResultSetColumnName(NAME_COLUMN_NAME)));
        productCategory.setDescription(resultSet.getString(deriveResultSetColumnName(DESCRIPTION_COLUMN_NAME)));
        productCategory.setStatus(mapStatus(resultSet.getString(deriveResultSetColumnName(STATUS_COLUMN_NAME))));
        return productCategory;
    }
}
