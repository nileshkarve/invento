package in.maitra.treats.invento.repo.jdbc.mapper;

import in.maitra.treats.invento.entity.RawMaterial;
import in.maitra.treats.invento.util.MeasuringUnit;
import in.maitra.treats.invento.util.RawMaterialType;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RawMaterialMapper extends AbstractMapper<RawMaterial> {

    public RawMaterialMapper(String tableAliasInQuery) {
        super(tableAliasInQuery);
    }

    private static final String ID_COLUMN_NAME = "id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String DESCRIPTION_COLUMN_NAME = "description";
    private static final String MEASURING_UNIT_COLUMN_NAME = "measuring_unit";
    private static final String RAW_MATERIAL_TYPE_COLUMN_NAME = "raw_material_type";
    private static final String STATUS_COLUMN_NAME = "status";

    @Override
    public RawMaterial createEntityFromResultSet(ResultSet resultSet) throws SQLException {
        RawMaterial rawMaterial = new RawMaterial();
        EntityId rawMaterialId = mapEntityId(resultSet.getObject(deriveResultSetColumnName(ID_COLUMN_NAME)));
        rawMaterial.setId(rawMaterialId);
        rawMaterial.setName(resultSet.getString(deriveResultSetColumnName(NAME_COLUMN_NAME)));
        rawMaterial.setDescription(resultSet.getString(deriveResultSetColumnName(DESCRIPTION_COLUMN_NAME)));
        String measuringUnitStr = resultSet.getString(deriveResultSetColumnName(MEASURING_UNIT_COLUMN_NAME));
        rawMaterial.setMeasuringUnit(measuringUnitStr != null ? MeasuringUnit.valueOf(measuringUnitStr) : null);
        String rawMaterialType = resultSet.getString(deriveResultSetColumnName(RAW_MATERIAL_TYPE_COLUMN_NAME));
        rawMaterial.setRawMaterialType(rawMaterialType != null ? RawMaterialType.valueOf(rawMaterialType) : null);
        rawMaterial.setStatus(mapStatus(resultSet.getString(deriveResultSetColumnName(STATUS_COLUMN_NAME))));
        return rawMaterial;
    }
}

