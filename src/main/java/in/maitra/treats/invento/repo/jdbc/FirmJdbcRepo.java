package in.maitra.treats.invento.repo.jdbc;

import in.maitra.treats.invento.entity.Firm;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.FirmRepo;
import in.maitra.treats.invento.repo.jdbc.extractor.FirmExtractor;
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
public class FirmJdbcRepo extends AbstractJdbcRepo implements FirmRepo<Firm> {

    public static final String TABLE_NAME = "firm";
    public static final String TABLE_ALIAS = "firm";
    private static final String SELECT_COLUMNS =
            TABLE_ALIAS + ".id as " + TABLE_ALIAS.concat("_").concat("id") + ", "
                    + TABLE_ALIAS + ".name as " + TABLE_ALIAS.concat("_").concat("name") + ", "
                    + TABLE_ALIAS + ".status as " + TABLE_ALIAS.concat("_").concat("status");
    private static final String FROM_CLAUSE = " FROM " + TABLE_NAME + " " + TABLE_ALIAS;


    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final JdbcTemplate jdbcTemplate;
    private final EntityIdIdGenerator idGenerator;

    @Override
    public PagedResponse<Firm> getAll(PaginationFilter filter) throws InventoException {
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
        List<Firm> firms = jdbcTemplate.query(
                "SELECT " + SELECT_COLUMNS + FROM_CLAUSE + queryParts.whereClause()
                        + " ORDER BY " + TABLE_ALIAS + ".name LIMIT ? OFFSET ?",
                new FirmExtractor(),
                params.toArray()
        );

        return new PagedResponse<>(firms, pageNumber, pageSize, totalElements == null ? 0L : totalElements);
    }

    @Override
    public Optional<Firm> getById(EntityId id) throws InventoException {
        if(null != id) {
            List<Firm> firms = jdbcTemplate.query("SELECT " + SELECT_COLUMNS + FROM_CLAUSE + " WHERE " + TABLE_ALIAS + ".id = ?", new FirmExtractor(), id.asString());
            if (null == firms || firms.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(firms.get(0));
            }
        }
        throw new InventoException("Id cannot be null");

    }

    @Override
    public Firm insert(Firm firm) throws InventoException {
        if(null != firm) {
            return insertAll(List.of(firm)).get(0);
        }
        throw new InventoException("Firm cannot be null");
    }

    @Override
    public List<Firm> insertAll(List<Firm> entities) throws InventoException {
        if (entities == null || entities.isEmpty()) {
            throw new InventoException("Firm cannot be null");
        }
        String sql = "INSERT INTO " + TABLE_NAME + " (id, name, status) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, entity) -> {
            EntityId id = idGenerator.generate();
            entity.setId(id);
            entity.setStatus(AuditableEntityRegistry.isEntityAuditable(entity.getEntityName()) ? Status.NEW : Status.APPROVED);
            ps.setObject(1, id.asString());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getStatus().name());
        });
        return entities;
    }

    @Override
    public Firm update(Firm firm) throws InventoException {
        if (firm == null || firm.getId() == null) {
            throw new InventoException("Firm or its ID cannot be null");
        }
        return updateAll(List.of(firm)).get(0);
    }

    @Override
    public List<Firm> updateAll(List<Firm> entities) throws InventoException {
        if (entities == null || entities.isEmpty()) {
            throw new InventoException("Firms to update cannot be null");
        }
        String sql = "UPDATE " + TABLE_NAME + " SET status = ?, name = ? WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, entity) -> {
            ps.setString(1, entity.getStatus().name());
            ps.setString(2, entity.getName());
            ps.setObject(3, entity.getId().asString());
        });
        return entities;
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        Firm firm = getById(id).orElseThrow(() -> new InventoException("Firm not found with id: " + id));
        firm.setStatus(Status.DELETED);
        update(firm);
        return true;
    }

    @Override
    protected String deriveTableAlias(String entityName, String fieldName) {
        return TABLE_ALIAS;
    }
}
