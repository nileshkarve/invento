package in.maitra.treats.invento.repo.jdbc.mapper;

import in.maitra.treats.invento.entity.RawMaterialStock;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RawMaterialStockMapper extends AbstractMapper<RawMaterialStock> {
    public RawMaterialStockMapper(String tableAliasInQuery) {
        super(tableAliasInQuery);
    }

    @Override
    public RawMaterialStock createEntityFromResultSet(ResultSet rs) throws SQLException {
        RawMaterialStock stock = new RawMaterialStock();
        stock.setId(mapEntityId(rs.getObject(deriveResultSetColumnName("id"))));
        stock.setQuantity(rs.getBigDecimal(deriveResultSetColumnName("quantity")));
        stock.setPricePerUnit(rs.getBigDecimal(deriveResultSetColumnName("price_per_unit")));
        stock.setBatchCode(rs.getString(deriveResultSetColumnName("batch_code")));
        stock.setManufacturingDate(rs.getObject(deriveResultSetColumnName("manufacturing_date"), java.time.LocalDate.class));
        stock.setPurchaseDate(rs.getObject(deriveResultSetColumnName("purchase_date"), java.time.LocalDate.class));
        stock.setExpiryDate(rs.getObject(deriveResultSetColumnName("expiry_date"), java.time.LocalDate.class));
        stock.setStatus(mapStatus(rs.getString(deriveResultSetColumnName("status"))));
        return stock;
    }
}
