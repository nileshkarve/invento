package in.maitra.treats.invento.repo.jdbc;

import in.maitra.treats.invento.entity.ProductCategory;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.ProductCategoryRepo;
import in.maitra.treats.invento.repo.jdbc.extractor.ProductCategoryExtractor;
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
public class ProductCategoryJdbcRepo extends AbstractJdbcRepo implements ProductCategoryRepo<ProductCategory> {
    public static final String TABLE_ALIAS = "product_category";
    public static final String TABLE_NAME = "product_category";
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private static final String SELECT_COLUMNS =
            TABLE_ALIAS + ".id as " + TABLE_ALIAS + "_id, "
                    + TABLE_ALIAS + ".name as " + TABLE_ALIAS + "_name, "
                    + TABLE_ALIAS + ".description as " + TABLE_ALIAS + "_description, "
                    + TABLE_ALIAS + ".status as " + TABLE_ALIAS + "_status, "
                    + TABLE_ALIAS + ".firm_id as " + TABLE_ALIAS + "_firm_id, "
                    + FirmJdbcRepo.TABLE_ALIAS + ".id as " + FirmJdbcRepo.TABLE_ALIAS + "_id, "
                    + FirmJdbcRepo.TABLE_ALIAS + ".status as " + FirmJdbcRepo.TABLE_ALIAS + "_status, "
                    + FirmJdbcRepo.TABLE_ALIAS + ".name as " + FirmJdbcRepo.TABLE_ALIAS + "_name";

    private static final String FROM_CLAUSE = " FROM " + TABLE_NAME + " " + TABLE_ALIAS
            + " JOIN " + FirmJdbcRepo.TABLE_NAME + " " + FirmJdbcRepo.TABLE_ALIAS
            + " ON " + TABLE_ALIAS + ".firm_id = " + FirmJdbcRepo.TABLE_ALIAS + ".id";

    private final JdbcTemplate jdbcTemplate;
    private final EntityIdIdGenerator idGenerator;

    @Override
    public PagedResponse<ProductCategory> getAll(PaginationFilter filter) throws InventoException {
        int pageNumber = filter != null && filter.getPageNumber() != null && filter.getPageNumber() >= 0
                ? filter.getPageNumber() : DEFAULT_PAGE_NUMBER;
        int pageSize = filter != null && filter.getPageSize() != null && filter.getPageSize() > 0
                ? filter.getPageSize() : DEFAULT_PAGE_SIZE;
        QueryParts queryParts = buildWhereClause(filter);
        Long totalElements = jdbcTemplate.queryForObject(
                "SELECT COUNT(*)" + FROM_CLAUSE + queryParts.whereClause(),
                Long.class, queryParts.params().toArray());

        List<Object> params = new ArrayList<>(queryParts.params());
        params.add(pageSize);
        params.add((long) pageNumber * pageSize);
        List<ProductCategory> categories = jdbcTemplate.query(
                "SELECT " + SELECT_COLUMNS + FROM_CLAUSE + queryParts.whereClause()
                        + " ORDER BY " + TABLE_ALIAS + ".name LIMIT ? OFFSET ?",
                new ProductCategoryExtractor(), params.toArray());
        return new PagedResponse<>(categories, pageNumber, pageSize, totalElements == null ? 0L : totalElements);
    }

    @Override
    public Optional<ProductCategory> getById(EntityId id) throws InventoException {
        if (id == null) {
            throw new InventoException("Id cannot be null");
        }
        List<ProductCategory> categories = jdbcTemplate.query("SELECT " + SELECT_COLUMNS + FROM_CLAUSE + " WHERE " + TABLE_ALIAS + ".id = ?", new ProductCategoryExtractor(), id.asString());
        return null != categories && !categories.isEmpty() ? Optional.of(categories.get(0)) : Optional.empty();
    }

    @Override
    public ProductCategory insert(ProductCategory category) throws InventoException {
        if (category == null) {
            throw new InventoException("ProductCategory cannot be null");
        }
        return insertAll(List.of(category)).get(0);
    }

    @Override
    public List<ProductCategory> insertAll(List<ProductCategory> entities) throws InventoException {
        if (entities == null || entities.isEmpty()) {
            throw new InventoException("ProductCategories cannot be null or empty");
        }
        validateFirmIds(entities);
        String sql = "INSERT INTO " + TABLE_NAME
                + " (id, name, description, status, firm_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, entity) -> {
            EntityId id = idGenerator.generate();
            entity.setId(id);
            entity.setStatus(AuditableEntityRegistry.isEntityAuditable(entity.getEntityName())
                    ? Status.NEW : Status.APPROVED);
            ps.setObject(1, entity.getId().asString());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getDescription());
            ps.setString(4, entity.getStatus().name());
            ps.setObject(5, entity.getFirm().getId().asString());
        });
        return entities;
    }

    @Override
    public ProductCategory update(ProductCategory category) throws InventoException {
        if (category == null) {
            throw new InventoException("ProductCategory cannot be null");
        }
        return updateAll(List.of(category)).get(0);
    }

    @Override
    public List<ProductCategory> updateAll(List<ProductCategory> entities) throws InventoException {
        if (entities == null || entities.isEmpty()) {
            throw new InventoException("ProductCategories to update cannot be null or empty");
        }
        validateIds(entities);
        validateFirmIds(entities);
        String sql = "UPDATE " + TABLE_NAME
                + " SET name = ?, description = ?, status = ?, firm_id = ? WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, entity) -> {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setString(3, entity.getStatus().name());
            ps.setObject(4, entity.getFirm().getId().asString());
            ps.setObject(5, entity.getId().asString());
        });
        return entities;
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        ProductCategory category = getById(id)
                .orElseThrow(() -> new InventoException("ProductCategory not found with id: " + id));
        category.setStatus(Status.DELETED);
        update(category);
        return true;
    }

    private void validateIds(List<ProductCategory> entities) throws InventoException {
        if (entities.stream().anyMatch(category -> category == null || category.getId() == null)) {
            throw new InventoException("ProductCategory and its ID cannot be null");
        }
    }

    private void validateFirmIds(List<ProductCategory> entities) throws InventoException {
        if (entities.stream().anyMatch(category -> category == null
                || category.getFirm() == null || category.getFirm().getId() == null)) {
            throw new InventoException("ProductCategory/Firm or Firm ID cannot be null");
        }
    }

    @Override
    protected String deriveTableAlias(String entityName, String fieldName) {
        return TABLE_ALIAS;
    }
}
