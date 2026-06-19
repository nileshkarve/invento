package in.maitra.treats.invento.repo.jdbc;

import in.maitra.treats.invento.entity.Vendor;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.VendorRepo;
import in.maitra.treats.invento.repo.jdbc.extractor.VendorExtractor;
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
public class VendorJdbcRepo extends AbstractJdbcRepo implements VendorRepo<Vendor> {
    public static final String TABLE_ALIAS = "vendor";
    public static final String TABLE_NAME = "vendor";
    private static final String SELECT_COLUMNS =
            TABLE_ALIAS + ".id as " + TABLE_ALIAS.concat("_").concat("id") + ", "
                    + TABLE_ALIAS + ".name as " + TABLE_ALIAS.concat("_").concat("name") + ", "
                    + TABLE_ALIAS + ".description as " + TABLE_ALIAS.concat("_").concat("description") + ", "
                    + TABLE_ALIAS + ".contact_person as " + TABLE_ALIAS.concat("_").concat("contact_person") + ", "
                    + TABLE_ALIAS + ".contact_number as " + TABLE_ALIAS.concat("_").concat("contact_number") + ", "
                    + TABLE_ALIAS + ".email as " + TABLE_ALIAS.concat("_").concat("email") + ", "
                    + TABLE_ALIAS + ".gst_number as " + TABLE_ALIAS.concat("_").concat("gst_number") + ", "
                    + TABLE_ALIAS + ".address1 as " + TABLE_ALIAS.concat("_").concat("address1") + ", "
                    + TABLE_ALIAS + ".address2 as " + TABLE_ALIAS.concat("_").concat("address2") + ", "
                    + TABLE_ALIAS + ".city as " + TABLE_ALIAS.concat("_").concat("city") + ", "
                    + TABLE_ALIAS + ".state as " + TABLE_ALIAS.concat("_").concat("state") + ", "
                    + TABLE_ALIAS + ".country as " + TABLE_ALIAS.concat("_").concat("country") + ", "
                    + TABLE_ALIAS + ".postal_code as " + TABLE_ALIAS.concat("_").concat("postal_code") + ", "
                    + TABLE_ALIAS + ".status as " + TABLE_ALIAS.concat("_").concat("status") + ", "
                    + TABLE_ALIAS + ".firm_id as " + TABLE_ALIAS.concat("_").concat("firm_id") + ", "
                    + FirmJdbcRepo.TABLE_ALIAS + ".id as " + FirmJdbcRepo.TABLE_ALIAS.concat("_").concat("id") + ", "
                    + FirmJdbcRepo.TABLE_ALIAS + ".status as " + FirmJdbcRepo.TABLE_ALIAS.concat("_").concat("status") + ", "
                    + FirmJdbcRepo.TABLE_ALIAS + ".name as " + FirmJdbcRepo.TABLE_ALIAS.concat("_").concat("name");

    private static final String FROM_CLAUSE = " FROM " + TABLE_NAME + " " + TABLE_ALIAS + " JOIN "  + FirmJdbcRepo.TABLE_NAME + " " + FirmJdbcRepo.TABLE_ALIAS + " ON " + TABLE_ALIAS + ".firm_id = " + FirmJdbcRepo.TABLE_ALIAS + ".id";
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final JdbcTemplate jdbcTemplate;
    private final EntityIdIdGenerator idGenerator;

    @Override
    public PagedResponse<Vendor> getAll(PaginationFilter filter) throws InventoException {
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
        List<Vendor> vendors = jdbcTemplate.query(
                "SELECT " + SELECT_COLUMNS + FROM_CLAUSE + queryParts.whereClause()
                        + " ORDER BY " + TABLE_ALIAS + ".name LIMIT ? OFFSET ?",
                new VendorExtractor(),
                params.toArray()
        );

        return new PagedResponse<>(vendors, pageNumber, pageSize, totalElements == null ? 0L : totalElements);
    }

    @Override
    public Optional<Vendor> getById(EntityId id) throws InventoException {
        if(null != id) {
            List<Vendor> vendors = jdbcTemplate.query("SELECT " + SELECT_COLUMNS + FROM_CLAUSE + " WHERE " + TABLE_ALIAS + ".id = ?", new VendorExtractor(), id.asString());
            if (null == vendors || vendors.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(vendors.get(0));
            }
        }
        throw new InventoException("Id cannot be null");
    }

    @Override
    public Vendor insert(Vendor vendor) throws InventoException {
        if(null != vendor) {
            return insertAll(List.of(vendor)).get(0);
        }
        throw new InventoException("Vendor cannot be null");
    }

    @Override
    public List<Vendor> insertAll(List<Vendor> entities) throws InventoException {
        if (entities == null || entities.isEmpty()) {
            throw new InventoException("Vendor cannot be null");
        }
        String sql = "INSERT INTO " + TABLE_NAME + " (id, name, description, contact_person, contact_number, email, gst_number, address1, address2, city, state, country, postal_code, status, firm_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, entity) -> {
            EntityId id = idGenerator.generate();
            entity.setId(id);
            entity.setStatus(AuditableEntityRegistry.isEntityAuditable(entity.getEntityName()) ? Status.NEW : Status.APPROVED);
            ps.setObject(1, id.asString());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getDescription());
            ps.setString(4, entity.getContactPerson());
            ps.setString(5, entity.getContactNumber());
            ps.setString(6, entity.getEmail());
            ps.setString(7, entity.getGstNumber());
            ps.setString(8, entity.getAddress1());
            ps.setString(9, entity.getAddress2());
            ps.setString(10, entity.getCity());
            ps.setString(11, entity.getState());
            ps.setString(12, entity.getCountry());
            ps.setString(13, entity.getPostalCode());
            ps.setString(14, entity.getStatus().name());
            ps.setObject(15, entity.getFirm().getId().asString());
        });
        return entities;
    }

    @Override
    public Vendor update(Vendor vendor) throws InventoException {
        if (vendor == null || vendor.getId() == null || vendor.getFirm() == null || vendor.getFirm().getId() == null) {
            throw new InventoException("Vendor/Firm or its ID cannot be null");
        }
        return updateAll(List.of(vendor)).get(0);
    }

    @Override
    public List<Vendor> updateAll(List<Vendor> entities) throws InventoException {
        if (entities == null || entities.isEmpty()) {
            throw new InventoException("Vendors to update cannot be null");
        }
        String sql = "UPDATE " + TABLE_NAME + " SET name = ?, description = ?, contact_person = ?, contact_number = ?, email = ?, gst_number = ?, address1 = ?, address2 = ?, city = ?, state = ?, country = ?, postal_code = ?, status = ?, firm_id = ? WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, entity) -> {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setString(3, entity.getContactPerson());
            ps.setString(4, entity.getContactNumber());
            ps.setString(5, entity.getEmail());
            ps.setString(6, entity.getGstNumber());
            ps.setString(7, entity.getAddress1());
            ps.setString(8, entity.getAddress2());
            ps.setString(9, entity.getCity());
            ps.setString(10, entity.getState());
            ps.setString(11, entity.getCountry());
            ps.setString(12, entity.getPostalCode());
            ps.setString(13, entity.getStatus().name());
            ps.setObject(14, entity.getFirm().getId().asString());
            ps.setObject(15, entity.getId().asString());
        });
        return entities;
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        Vendor vendor = getById(id).orElseThrow(() -> new InventoException("Vendor not found with id: " + id));
        vendor.setStatus(Status.DELETED);
        update(vendor);
        return true;
    }

    @Override
    protected String deriveTableAlias(String entityName, String fieldName) {
        return TABLE_ALIAS;
    }
}
