package in.maitra.treats.invento.repo.jdbc;

import in.maitra.treats.invento.entity.RawMaterialStock;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.RawMaterialStockRepo;
import in.maitra.treats.invento.repo.jdbc.extractor.RawMaterialStockExtractor;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.AuditableEntityRegistry;
import in.maitra.treats.invento.util.persistence.EntityId;
import in.maitra.treats.invento.util.persistence.EntityIdIdGenerator;
import in.maitra.treats.invento.util.persistence.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RawMaterialStockJdbcRepo extends AbstractJdbcRepo
        implements RawMaterialStockRepo<RawMaterialStock> {
    public static final String TABLE_ALIAS = "raw_material_stock";
    public static final String TABLE_NAME = "raw_material_stock";
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private static final String SELECT_COLUMNS =
            TABLE_ALIAS + ".id as " + TABLE_ALIAS + "_id, "
                    + TABLE_ALIAS + ".quantity as " + TABLE_ALIAS + "_quantity, "
                    + TABLE_ALIAS + ".price_per_unit as " + TABLE_ALIAS + "_price_per_unit, "
                    + TABLE_ALIAS + ".batch_code as " + TABLE_ALIAS + "_batch_code, "
                    + TABLE_ALIAS + ".manufacturing_date as " + TABLE_ALIAS + "_manufacturing_date, "
                    + TABLE_ALIAS + ".purchase_date as " + TABLE_ALIAS + "_purchase_date, "
                    + TABLE_ALIAS + ".expiry_date as " + TABLE_ALIAS + "_expiry_date, "
                    + TABLE_ALIAS + ".status as " + TABLE_ALIAS + "_status, "
                    + RawMaterialJdbcRepo.TABLE_ALIAS + ".id as " + RawMaterialJdbcRepo.TABLE_ALIAS + "_id, "
                    + RawMaterialJdbcRepo.TABLE_ALIAS + ".name as " + RawMaterialJdbcRepo.TABLE_ALIAS + "_name, "
                    + RawMaterialJdbcRepo.TABLE_ALIAS + ".description as " + RawMaterialJdbcRepo.TABLE_ALIAS + "_description, "
                    + RawMaterialJdbcRepo.TABLE_ALIAS + ".measuring_unit as " + RawMaterialJdbcRepo.TABLE_ALIAS + "_measuring_unit, "
                    + RawMaterialJdbcRepo.TABLE_ALIAS + ".raw_material_type as " + RawMaterialJdbcRepo.TABLE_ALIAS + "_raw_material_type, "
                    + RawMaterialJdbcRepo.TABLE_ALIAS + ".status as " + RawMaterialJdbcRepo.TABLE_ALIAS + "_status, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".id as " + VendorJdbcRepo.TABLE_ALIAS + "_id, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".name as " + VendorJdbcRepo.TABLE_ALIAS + "_name, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".description as " + VendorJdbcRepo.TABLE_ALIAS + "_description, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".contact_person as " + VendorJdbcRepo.TABLE_ALIAS + "_contact_person, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".contact_number as " + VendorJdbcRepo.TABLE_ALIAS + "_contact_number, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".email as " + VendorJdbcRepo.TABLE_ALIAS + "_email, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".gst_number as " + VendorJdbcRepo.TABLE_ALIAS + "_gst_number, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".address1 as " + VendorJdbcRepo.TABLE_ALIAS + "_address1, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".address2 as " + VendorJdbcRepo.TABLE_ALIAS + "_address2, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".city as " + VendorJdbcRepo.TABLE_ALIAS + "_city, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".state as " + VendorJdbcRepo.TABLE_ALIAS + "_state, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".country as " + VendorJdbcRepo.TABLE_ALIAS + "_country, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".postal_code as " + VendorJdbcRepo.TABLE_ALIAS + "_postal_code, "
                    + VendorJdbcRepo.TABLE_ALIAS + ".status as " + VendorJdbcRepo.TABLE_ALIAS + "_status, "
                    + FirmJdbcRepo.TABLE_ALIAS + ".id as " + FirmJdbcRepo.TABLE_ALIAS + "_id, "
                    + FirmJdbcRepo.TABLE_ALIAS + ".status as " + FirmJdbcRepo.TABLE_ALIAS + "_status, "
                    + FirmJdbcRepo.TABLE_ALIAS + ".name as " + FirmJdbcRepo.TABLE_ALIAS + "_name";

    private static final String FROM_CLAUSE = " FROM " + TABLE_NAME + " " + TABLE_ALIAS
            + " JOIN " + RawMaterialJdbcRepo.TABLE_NAME + " " + RawMaterialJdbcRepo.TABLE_ALIAS
            + " ON " + TABLE_ALIAS + ".raw_material_id = " + RawMaterialJdbcRepo.TABLE_ALIAS + ".id"
            + " JOIN " + VendorJdbcRepo.TABLE_NAME + " " + VendorJdbcRepo.TABLE_ALIAS
            + " ON " + TABLE_ALIAS + ".vendor_id = " + VendorJdbcRepo.TABLE_ALIAS + ".id"
            + " JOIN " + FirmJdbcRepo.TABLE_NAME + " " + FirmJdbcRepo.TABLE_ALIAS
            + " ON " + RawMaterialJdbcRepo.TABLE_ALIAS + ".firm_id = " + FirmJdbcRepo.TABLE_ALIAS + ".id";

    private final JdbcTemplate jdbcTemplate;
    private final EntityIdIdGenerator idGenerator;

    @Override
    public PagedResponse<RawMaterialStock> getAll(PaginationFilter filter) throws InventoException {
        int pageNumber = filter != null && filter.getPageNumber() != null && filter.getPageNumber() >= 0
                ? filter.getPageNumber() : DEFAULT_PAGE_NUMBER;
        int pageSize = filter != null && filter.getPageSize() != null && filter.getPageSize() > 0
                ? filter.getPageSize() : DEFAULT_PAGE_SIZE;
        QueryParts query = buildWhereClause(filter);
        Long total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*)" + FROM_CLAUSE + query.whereClause(), Long.class, query.params().toArray());
        List<Object> params = new ArrayList<>(query.params());
        params.add(pageSize);
        params.add((long) pageNumber * pageSize);
        List<RawMaterialStock> result = jdbcTemplate.query(
                "SELECT " + SELECT_COLUMNS + FROM_CLAUSE + query.whereClause()
                        + " ORDER BY " + RawMaterialJdbcRepo.TABLE_ALIAS + ".name LIMIT ? OFFSET ?",
                new RawMaterialStockExtractor(), params.toArray());
        return new PagedResponse<>(result, pageNumber, pageSize, total == null ? 0L : total);
    }

    @Override
    public Optional<RawMaterialStock> getById(EntityId id) throws InventoException {
        if (id == null) throw new InventoException("Id cannot be null");
        List<RawMaterialStock> stocks = jdbcTemplate.query("SELECT " + SELECT_COLUMNS + FROM_CLAUSE + " WHERE " + TABLE_ALIAS + ".id = ?", new RawMaterialStockExtractor(), id.asString());
        return null != stocks && !stocks.isEmpty() ? Optional.of(stocks.get(0)) : Optional.empty();
    }

    @Override
    public RawMaterialStock insert(RawMaterialStock stock) throws InventoException {
        if (stock == null) throw new InventoException("RawMaterialStock cannot be null");
        return insertAll(List.of(stock)).get(0);
    }

    @Override
    public List<RawMaterialStock> insertAll(List<RawMaterialStock> entities) throws InventoException {
        validate(entities, false);
        String sql = "INSERT INTO " + TABLE_NAME
                + " (id, raw_material_id, vendor_id, quantity, price_per_unit, batch_code,"
                + " manufacturing_date, purchase_date, expiry_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, entity) -> {
            entity.setId(idGenerator.generate());
            entity.setStatus(AuditableEntityRegistry.isEntityAuditable(entity.getEntityName())
                    ? Status.NEW : Status.APPROVED);
            ps.setObject(1, entity.getId().asString());
            ps.setObject(2, entity.getRawMaterial().getId().asString());
            ps.setObject(3, entity.getVendor().getId().asString());
            ps.setBigDecimal(4, entity.getQuantity());
            ps.setBigDecimal(5, entity.getPricePerUnit());
            ps.setString(6, entity.getBatchCode());
            ps.setObject(7, entity.getManufacturingDate(), Types.DATE);
            ps.setObject(8, entity.getPurchaseDate(), Types.DATE);
            ps.setObject(9, entity.getExpiryDate(), Types.DATE);
            ps.setString(10, entity.getStatus().name());
        });
        return entities;
    }

    @Override
    public RawMaterialStock update(RawMaterialStock stock) throws InventoException {
        if (stock == null) throw new InventoException("RawMaterialStock cannot be null");
        return updateAll(List.of(stock)).get(0);
    }

    @Override
    public List<RawMaterialStock> updateAll(List<RawMaterialStock> entities) throws InventoException {
        validate(entities, true);
        String sql = "UPDATE " + TABLE_NAME
                + " SET raw_material_id = ?, vendor_id = ?, quantity = ?, price_per_unit = ?, batch_code = ?,"
                + " manufacturing_date = ?, purchase_date = ?, expiry_date = ?, status = ? WHERE id = ?";

        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, entity) -> {
            ps.setObject(1, entity.getRawMaterial().getId().asString());
            ps.setObject(2, entity.getVendor().getId().asString());
            ps.setBigDecimal(3, entity.getQuantity());
            ps.setBigDecimal(4, entity.getPricePerUnit());
            ps.setString(5, entity.getBatchCode());
            ps.setObject(6, entity.getManufacturingDate(), Types.DATE);
            ps.setObject(7, entity.getPurchaseDate(), Types.DATE);
            ps.setObject(8, entity.getExpiryDate(), Types.DATE);
            ps.setString(9, entity.getStatus().name());
            ps.setObject(10, entity.getId().asString());
        });
        return entities;
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        RawMaterialStock stock = getById(id)
                .orElseThrow(() -> new InventoException("RawMaterialStock not found with id: " + id));
        stock.setStatus(Status.DELETED);
        update(stock);
        return true;
    }

    private void validate(List<RawMaterialStock> entities, boolean requireId) throws InventoException {
        if (entities == null || entities.isEmpty()
                || entities.stream().anyMatch(stock -> stock == null
                || (requireId && stock.getId() == null)
                || stock.getRawMaterial() == null || stock.getRawMaterial().getId() == null
                || stock.getVendor() == null || stock.getVendor().getId() == null
                || stock.getPurchaseDate() == null)) {
            throw new InventoException("Stock, Stock ID, RawMaterial, Vendor, related IDs or purchase date cannot be null");
        }
    }

    @Override
    protected String deriveTableAlias(String entityName, String fieldName) {
        return TABLE_ALIAS;
    }
}
