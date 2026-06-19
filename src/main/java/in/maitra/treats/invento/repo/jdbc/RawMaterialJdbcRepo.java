package in.maitra.treats.invento.repo.jdbc;

import in.maitra.treats.invento.entity.RawMaterial;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.RawMaterialRepo;
import in.maitra.treats.invento.repo.jdbc.extractor.RawMaterialExtractor;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.AuditableEntityRegistry;
import in.maitra.treats.invento.util.persistence.EntityId;
import in.maitra.treats.invento.util.persistence.EntityIdIdGenerator;
import in.maitra.treats.invento.util.persistence.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RawMaterialJdbcRepo extends AbstractJdbcRepo implements RawMaterialRepo<RawMaterial> {
    public static final String TABLE_ALIAS = "raw_material";
    public static final String TABLE_NAME = "raw_material";
    private static final String SELECT_COLUMNS =
            TABLE_ALIAS + ".id as " + TABLE_ALIAS.concat("_").concat("id") + ", "
                    + TABLE_ALIAS + ".name as " + TABLE_ALIAS.concat("_").concat("name") + ", "
                    + TABLE_ALIAS + ".description as " + TABLE_ALIAS.concat("_").concat("description") + ", "
                    + TABLE_ALIAS + ".measuring_unit as " + TABLE_ALIAS.concat("_").concat("measuring_unit") + ", "
                    + TABLE_ALIAS + ".raw_material_type as " + TABLE_ALIAS.concat("_").concat("raw_material_type") + ", "
                    + TABLE_ALIAS + ".status as " + TABLE_ALIAS.concat("_").concat("status") + ", "
                    + TABLE_ALIAS + ".firm_id as " + TABLE_ALIAS.concat("_").concat("firm_id") + ", "
                    + FirmJdbcRepo.TABLE_ALIAS + ".id as " + FirmJdbcRepo.TABLE_ALIAS.concat("_").concat("id") + ", "
                    + FirmJdbcRepo.TABLE_ALIAS + ".status as " + FirmJdbcRepo.TABLE_ALIAS.concat("_").concat("status") + ", "
                    + FirmJdbcRepo.TABLE_ALIAS + ".name as " + FirmJdbcRepo.TABLE_ALIAS.concat("_").concat("name");

    private static final String FROM_CLAUSE = " FROM " + TABLE_NAME + " " + TABLE_ALIAS
            + " JOIN " + FirmJdbcRepo.TABLE_NAME + " " + FirmJdbcRepo.TABLE_ALIAS + " ON " + TABLE_ALIAS + ".firm_id = " + FirmJdbcRepo.TABLE_ALIAS + ".id";

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final JdbcTemplate jdbcTemplate;
    private final EntityIdIdGenerator idGenerator;

    @Override
    public PagedResponse<RawMaterial> getAll(PaginationFilter filter) throws InventoException {
        int pageNumber = filter != null && filter.getPageNumber() != null && filter.getPageNumber() >= 0 ? filter.getPageNumber() : DEFAULT_PAGE_NUMBER;
        int pageSize = filter != null && filter.getPageSize() != null && filter.getPageSize() > 0 ? filter.getPageSize() : DEFAULT_PAGE_SIZE;

        QueryParts queryParts = buildWhereClause(filter);
        Long totalElements = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM " + TABLE_NAME + " " + TABLE_ALIAS + queryParts.whereClause(),
                Long.class,
                queryParts.params().toArray()
        );

        List<Object> params = new ArrayList<>(queryParts.params());
        params.add(pageSize);
        params.add((long) pageNumber * pageSize);
        List<RawMaterial> rawMaterials = jdbcTemplate.query(
                "SELECT " + SELECT_COLUMNS + FROM_CLAUSE + queryParts.whereClause()
                        + " ORDER BY " + TABLE_ALIAS + ".name LIMIT ? OFFSET ?",
                new RawMaterialExtractor(),
                params.toArray()
        );

        return new PagedResponse<>(rawMaterials, pageNumber, pageSize, totalElements == null ? 0L : totalElements);
    }

    @Override
    public Optional<RawMaterial> getById(EntityId id) throws InventoException {
        if(null != id) {
            List<RawMaterial> rawMaterials = jdbcTemplate.query("SELECT " + SELECT_COLUMNS + FROM_CLAUSE + " WHERE " + TABLE_ALIAS + ".id = ?", new RawMaterialExtractor(), id.asString());
            if (null == rawMaterials || rawMaterials.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(rawMaterials.get(0));
            }
        }
        throw new InventoException("Id cannot be null");
    }

    @Override
    public RawMaterial insert(RawMaterial rawMaterial) throws InventoException {
        if(null != rawMaterial) {
            return insertAll(List.of(rawMaterial)).get(0);
        }
        throw new InventoException("RawMaterial cannot be null");
    }

    @Override
    public List<RawMaterial> insertAll(List<RawMaterial> entities) throws InventoException {
        if (entities == null || entities.isEmpty()) {
            throw new InventoException("RawMaterial cannot be null");
        }
        String sql = "INSERT INTO " + TABLE_NAME
                + " (id, name, description, measuring_unit, raw_material_type, status, firm_id)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, rawMaterial) -> {
            EntityId id = idGenerator.generate();
            rawMaterial.setId(id);
            rawMaterial.setStatus(AuditableEntityRegistry.isEntityAuditable(rawMaterial.getEntityName()) ? Status.NEW : Status.APPROVED);
            ps.setObject(1, id.asString());
            ps.setString(2, rawMaterial.getName());
            ps.setString(3, rawMaterial.getDescription());
            ps.setString(4, rawMaterial.getMeasuringUnit().name());
            ps.setString(5, rawMaterial.getRawMaterialType().name());
            ps.setString(6, rawMaterial.getStatus().name());
            ps.setObject(7, rawMaterial.getFirm().getId().asString());
        });
        return entities;
    }

    @Override
    public RawMaterial update(RawMaterial rawMaterial) throws InventoException {
        if (rawMaterial == null || rawMaterial.getId() == null || rawMaterial.getFirm() == null || rawMaterial.getFirm().getId() == null) {
            throw new InventoException("RawMaterial/Firm or their IDs cannot be null");
        }
        return updateAll(List.of(rawMaterial)).get(0);
    }

    @Override
    public List<RawMaterial> updateAll(List<RawMaterial> entities) throws InventoException {
        if (entities == null || entities.isEmpty()) {
            throw new InventoException("RawMaterials to update cannot be null");
        }
        String sql = "UPDATE " + TABLE_NAME
                + " SET name = ?, description = ?, measuring_unit = ?, raw_material_type = ?,"
                + " status = ?, firm_id = ? WHERE id = ?";

        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, rawMaterial) -> {
                    ps.setString(1, rawMaterial.getName());
                    ps.setString(2, rawMaterial.getDescription());
                    ps.setString(3, rawMaterial.getMeasuringUnit() != null ? rawMaterial.getMeasuringUnit().name() : null);
                    ps.setString(4, rawMaterial.getRawMaterialType() != null ? rawMaterial.getRawMaterialType().name() : null);
                    ps.setString(5, rawMaterial.getStatus().name());
                    ps.setObject(6, rawMaterial.getFirm().getId().asString());
                    ps.setObject(7, rawMaterial.getId().asString());
                }
        );
        return entities;
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        RawMaterial rawMaterial = getById(id).orElseThrow(() -> new InventoException("RawMaterial not found with id: " + id));
        rawMaterial.setStatus(Status.DELETED);
        update(rawMaterial);
        return true;
    }

    @Override
    protected String deriveTableAlias(String entityName, String fieldName) {
        return TABLE_ALIAS;
    }
}

