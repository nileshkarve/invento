package in.maitra.treats.invento.repo.jdbc.extractor;

import in.maitra.treats.invento.entity.Firm;
import in.maitra.treats.invento.entity.RawMaterial;
import in.maitra.treats.invento.repo.jdbc.FirmJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.RawMaterialJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.mapper.FirmMapper;
import in.maitra.treats.invento.repo.jdbc.mapper.RawMaterialMapper;
import in.maitra.treats.invento.util.persistence.EntityId;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RawMaterialExtractor extends AbstractJdbcExtractor<RawMaterial> {

    @Override
    public List<RawMaterial> extractData(ResultSet rs) throws DataAccessException, SQLException {
        Map<EntityId, RawMaterial> rawMaterialIdMap = new LinkedHashMap<>();

        while (rs.next()) {
            RawMaterial rawMaterial = new RawMaterialMapper(RawMaterialJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
            RawMaterial alreadyCreatedRawMaterial = rawMaterialIdMap.computeIfAbsent(rawMaterial.getId(), id -> rawMaterial);
            if(alreadyCreatedRawMaterial.getFirm() == null) {
                Firm firm = new FirmMapper(FirmJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
                alreadyCreatedRawMaterial.setFirm(firm);
            }
        }
        return rawMaterialIdMap.values().stream().toList();
    }
}

