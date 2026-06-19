package in.maitra.treats.invento.repo.jdbc.extractor;

import in.maitra.treats.invento.entity.Firm;
import in.maitra.treats.invento.entity.RawMaterial;
import in.maitra.treats.invento.entity.RawMaterialStock;
import in.maitra.treats.invento.entity.Vendor;
import in.maitra.treats.invento.repo.jdbc.FirmJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.RawMaterialStockJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.RawMaterialJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.VendorJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.mapper.FirmMapper;
import in.maitra.treats.invento.repo.jdbc.mapper.RawMaterialStockMapper;
import in.maitra.treats.invento.repo.jdbc.mapper.RawMaterialMapper;
import in.maitra.treats.invento.repo.jdbc.mapper.VendorMapper;
import in.maitra.treats.invento.util.persistence.EntityId;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RawMaterialStockExtractor extends AbstractJdbcExtractor<RawMaterialStock> {
    @Override
    public List<RawMaterialStock> extractData(ResultSet rs) throws DataAccessException, SQLException {
        Map<EntityId, RawMaterialStock> inventories = new LinkedHashMap<>();
        while (rs.next()) {
            RawMaterialStock inventory =
                    new RawMaterialStockMapper(RawMaterialStockJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
            RawMaterialStock mapped = inventories.computeIfAbsent(inventory.getId(), id -> inventory);
            if (mapped.getRawMaterial() == null) {
                RawMaterial rawMaterial =
                        new RawMaterialMapper(RawMaterialJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
                Firm firm = new FirmMapper(FirmJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
                rawMaterial.setFirm(firm);
                mapped.setRawMaterial(rawMaterial);
            }
            if (mapped.getVendor() == null) {
                Vendor vendor = new VendorMapper(VendorJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
                mapped.setVendor(vendor);
            }
        }
        return inventories.values().stream().toList();
    }
}
