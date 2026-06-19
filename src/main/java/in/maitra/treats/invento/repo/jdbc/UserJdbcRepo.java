package in.maitra.treats.invento.repo.jdbc;

import in.maitra.treats.invento.entity.User;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.UserRepo;
import in.maitra.treats.invento.repo.jdbc.extractor.UserExtractor;
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
public class UserJdbcRepo extends AbstractJdbcRepo implements UserRepo<User> {
    public static final String TABLE_ALIAS = "user";
    private static final String TABLE_NAME = "user";
    private static final String SELECT_COLUMNS =
            TABLE_ALIAS + ".id as " + TABLE_ALIAS.concat("_").concat("id") + ", "
                    + TABLE_ALIAS + ".name as " + TABLE_ALIAS.concat("_").concat("name") + ", "
                    + TABLE_ALIAS + ".email as " + TABLE_ALIAS.concat("_").concat("email") + ", "
                    + TABLE_ALIAS + ".contact_number as " + TABLE_ALIAS.concat("_").concat("contact_number") + ", "
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
    public PagedResponse<User> getAll(PaginationFilter filter) throws InventoException {
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
        List<User> users = jdbcTemplate.query(
                "SELECT " + SELECT_COLUMNS + FROM_CLAUSE + queryParts.whereClause()
                        + " ORDER BY " + TABLE_ALIAS + ".name LIMIT ? OFFSET ?",
                new UserExtractor(),
                params.toArray()
        );

        return new PagedResponse<>(users, pageNumber, pageSize, totalElements == null ? 0L : totalElements);
    }

    @Override
    public Optional<User> getById(EntityId id) throws InventoException {
        if(null != id) {
            List<User> users = jdbcTemplate.query("SELECT " + SELECT_COLUMNS + FROM_CLAUSE + " WHERE " + TABLE_ALIAS + ".id = ?", new UserExtractor(), id.asString());
            if (null == users || users.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(users.get(0));
            }
        }
        throw new InventoException("Id cannot be null");
    }

    @Override
    public User insert(User user) throws InventoException {
        if(null != user) {
            return insertAll(List.of(user)).get(0);
        }
        throw new InventoException("User cannot be null");
    }

    @Override
    public List<User> insertAll(List<User> entities) throws InventoException {
        if (entities == null || entities.isEmpty()) {
            throw new InventoException("User cannot be null");
        }
        String sql = "INSERT INTO " + TABLE_NAME + " (id, name, email, contact_number, status, firm_id) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, entity) -> {
            EntityId id = idGenerator.generate();
            entity.setId(id);
            entity.setStatus(AuditableEntityRegistry.isEntityAuditable(entity.getEntityName()) ? Status.NEW : Status.APPROVED);
            ps.setObject(1, id.asString());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getContactNumber());
            ps.setString(5, entity.getStatus().name());
            ps.setObject(6, entity.getFirm().getId().asString());
        });
        return entities;
    }

    @Override
    public User update(User user) throws InventoException {
        if (user == null || user.getId() == null || user.getFirm() == null || user.getFirm().getId() == null) {
            throw new InventoException("User/Firm or its ID cannot be null");
        }
        return updateAll(List.of(user)).get(0);
    }

    @Override
    public List<User> updateAll(List<User> entities) throws InventoException {
        if (entities == null || entities.isEmpty()) {
            throw new InventoException("Users to update cannot be null");
        }
        String sql = "UPDATE " + TABLE_NAME + " SET name = ?, email = ?, contact_number = ?, status = ?, firm_id = ? WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, entities, maxBatchSize, (ps, entity) -> {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setString(3, entity.getContactNumber());
            ps.setString(4, entity.getStatus().name());
            ps.setObject(5, entity.getFirm().getId().asString());
            ps.setObject(6, entity.getId().asString());
        });
        return entities;
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        User user = getById(id).orElseThrow(() -> new InventoException("User not found with id: " + id));
        user.setStatus(Status.DELETED);
        update(user);
        return true;
    }

    @Override
    protected String deriveTableAlias(String entityName, String fieldName) {
        return TABLE_ALIAS;
    }
}
