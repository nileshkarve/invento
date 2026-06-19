package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.User;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.util.Optional;

public interface UserService {
    PagedResponse<User> getAll(PaginationFilter filter) throws InventoException;

    Optional<User> get(EntityId id) throws InventoException;

    User insert(User user) throws InventoException;

    User update(User user) throws InventoException;

    boolean delete(EntityId id) throws InventoException;
}
