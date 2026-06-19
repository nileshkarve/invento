package in.maitra.treats.invento.util.persistence;

import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.util.filter.PaginationFilter;

import java.util.List;
import java.util.Optional;

public interface PersistableEntityRepo<T extends PersistableEntity> {
    PagedResponse<T> getAll(PaginationFilter filter) throws InventoException;
    Optional<T> getById(EntityId id) throws InventoException;
    T insert(T entity) throws InventoException;
    List<T> insertAll(List<T> entities) throws InventoException;
    T update(T entity) throws InventoException;
    List<T> updateAll(List<T> entities) throws InventoException;
    boolean delete(EntityId id) throws InventoException;
}
