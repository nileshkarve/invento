package in.maitra.treats.invento.repo.jdbc.extractor;

import in.maitra.treats.invento.entity.Firm;
import in.maitra.treats.invento.entity.Vendor;
import in.maitra.treats.invento.repo.jdbc.FirmJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.VendorJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.mapper.FirmMapper;
import in.maitra.treats.invento.repo.jdbc.mapper.VendorMapper;
import in.maitra.treats.invento.util.persistence.EntityId;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VendorExtractor extends AbstractJdbcExtractor<Vendor> {

    @Override
    public List<Vendor> extractData(ResultSet rs) throws DataAccessException, SQLException {
        Map<EntityId, Vendor> vendorIdMap = new LinkedHashMap<>();

        while (rs.next()) {
            Vendor vendor = new VendorMapper(VendorJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
            Vendor alreadyCreatedVendor = vendorIdMap.computeIfAbsent(vendor.getId(), id -> vendor);
            if(alreadyCreatedVendor.getFirm() != null) {
                Firm firm = new FirmMapper(FirmJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
                alreadyCreatedVendor.setFirm(firm);
            }
        }
        return vendorIdMap.values().stream().toList();
    }
}

