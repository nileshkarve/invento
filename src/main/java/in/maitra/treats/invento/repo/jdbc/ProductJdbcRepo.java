package in.maitra.treats.invento.repo.jdbc;

import in.maitra.treats.invento.entity.Product;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.ProductRepo;
import in.maitra.treats.invento.repo.jdbc.extractor.ProductExtractor;
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
public class ProductJdbcRepo extends AbstractJdbcRepo implements ProductRepo<Product> {
    public static final String TABLE_ALIAS = "product";
    public static final String TABLE_NAME = "product";
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private static final String SELECT_COLUMNS =
            TABLE_ALIAS + ".id as " + TABLE_ALIAS + "_id, "
                    + TABLE_ALIAS + ".name as " + TABLE_ALIAS + "_name, "
                    + TABLE_ALIAS + ".description as " + TABLE_ALIAS + "_description, "
                    + TABLE_ALIAS + ".mrp as " + TABLE_ALIAS + "_mrp, "
                    + TABLE_ALIAS + ".status as " + TABLE_ALIAS + "_status, "
                    + ProductCategoryJdbcRepo.TABLE_ALIAS + ".id as " + ProductCategoryJdbcRepo.TABLE_ALIAS + "_id, "
                    + ProductCategoryJdbcRepo.TABLE_ALIAS + ".name as " + ProductCategoryJdbcRepo.TABLE_ALIAS + "_name, "
                    + ProductCategoryJdbcRepo.TABLE_ALIAS + ".description as " + ProductCategoryJdbcRepo.TABLE_ALIAS + "_description, "
                    + ProductCategoryJdbcRepo.TABLE_ALIAS + ".status as " + ProductCategoryJdbcRepo.TABLE_ALIAS + "_status, "
                    + FirmJdbcRepo.TABLE_ALIAS + ".id as " + FirmJdbcRepo.TABLE_ALIAS + "_id, "
                    + FirmJdbcRepo.TABLE_ALIAS + ".status as " + FirmJdbcRepo.TABLE_ALIAS + "_status, "
                    + FirmJdbcRepo.TABLE_ALIAS + ".name as " + FirmJdbcRepo.TABLE_ALIAS + "_name";

    private static final String FROM_CLAUSE = " FROM " + TABLE_NAME + " " + TABLE_ALIAS
            + " JOIN " + ProductCategoryJdbcRepo.TABLE_NAME + " " + ProductCategoryJdbcRepo.TABLE_ALIAS
            + " ON " + TABLE_ALIAS + ".category_id = " + ProductCategoryJdbcRepo.TABLE_ALIAS + ".id"
            + " JOIN " + FirmJdbcRepo.TABLE_NAME + " " + FirmJdbcRepo.TABLE_ALIAS
            + " ON " + ProductCategoryJdbcRepo.TABLE_ALIAS + ".firm_id = " + FirmJdbcRepo.TABLE_ALIAS + ".id";

    private final JdbcTemplate jdbcTemplate;
    private final EntityIdIdGenerator idGenerator;

    @Override
    public PagedResponse<Product> getAll(PaginationFilter filter) throws InventoException {
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
        List<Product> products = jdbcTemplate.query(
                "SELECT " + SELECT_COLUMNS + FROM_CLAUSE + queryParts.whereClause()
                        + " ORDER BY " + TABLE_ALIAS + ".name LIMIT ? OFFSET ?",
                new ProductExtractor(), params.toArray());
        return new PagedResponse<>(products, pageNumber, pageSize, totalElements == null ? 0L : totalElements);
    }

    @Override
    public Optional<Product> getById(EntityId id) throws InventoException {
        if (id == null) {
            throw new InventoException("Id cannot be null");
        }
        List<Product> products = jdbcTemplate.query("SELECT " + SELECT_COLUMNS + FROM_CLAUSE + " WHERE " + TABLE_ALIAS + ".id = ?", new ProductExtractor(), id.asString());
        return null != products && !products.isEmpty() ? Optional.of(products.get(0)) : Optional.empty();
    }

    @Override
    public Product insert(Product product) throws InventoException {
        if (product == null) {
            throw new InventoException("Product cannot be null");
        }
        return insertAll(List.of(product)).get(0);
    }

    @Override
    public List<Product> insertAll(List<Product> entities) throws InventoException {
        validateEntities(entities, false);
        String sql = "INSERT INTO " + TABLE_NAME
                + " (id, name, description, mrp, category_id, status) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, entity) -> {
            entity.setId(idGenerator.generate());
            entity.setStatus(AuditableEntityRegistry.isEntityAuditable(entity.getEntityName())
                    ? Status.NEW : Status.APPROVED);
            ps.setObject(1, entity.getId().asString());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getDescription());
            ps.setBigDecimal(4, entity.getMrp());
            ps.setObject(5, entity.getCategory().getId().asString());
            ps.setString(6, entity.getStatus().name());
        });
        return entities;
    }

    @Override
    public Product update(Product product) throws InventoException {
        if (product == null) {
            throw new InventoException("Product cannot be null");
        }
        return updateAll(List.of(product)).get(0);
    }

    @Override
    public List<Product> updateAll(List<Product> entities) throws InventoException {
        validateEntities(entities, true);
        String sql = "UPDATE " + TABLE_NAME
                + " SET name = ?, description = ?, mrp = ?, category_id = ?, status = ? WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, entity) -> {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setBigDecimal(3, entity.getMrp());
            ps.setObject(4, entity.getCategory().getId().asString());
            ps.setString(5, entity.getStatus().name());
            ps.setObject(6, entity.getId().asString());
        });
        return entities;
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        Product product = getById(id)
                .orElseThrow(() -> new InventoException("Product not found with id: " + id));
        product.setStatus(Status.DELETED);
        update(product);
        return true;
    }

    private void validateEntities(List<Product> entities, boolean requireId) throws InventoException {
        if (entities == null || entities.isEmpty()) {
            throw new InventoException("Products cannot be null or empty");
        }
        if (entities.stream().anyMatch(product -> product == null
                || (requireId && product.getId() == null)
                || product.getCategory() == null || product.getCategory().getId() == null)) {
            throw new InventoException("Product, Product ID, Category or Category ID cannot be null");
        }
    }

    @Override
    protected String deriveTableAlias(String entityName, String fieldName) {
        return TABLE_ALIAS;
    }
}
