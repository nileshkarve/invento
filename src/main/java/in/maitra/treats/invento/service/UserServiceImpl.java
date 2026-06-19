package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.User;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.UserRepo;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo<User> userRepo;

    @Override
    public PagedResponse<User> getAll(PaginationFilter filter) throws InventoException {
        log.info("Getting all users matching filter {}", filter);
        try {
            return userRepo.getAll(filter);
        } catch (Exception e) {
            log.error("Error getting users with filter {}", filter, e);
            throw new InventoException("Failed to get users: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> get(EntityId id) throws InventoException {
        log.info("Getting user with id {}", id);
        try {
            return userRepo.getById(id);
        } catch (Exception e) {
            log.error("Error getting user with id {}", id, e);
            throw new InventoException("Failed to get user: " + e.getMessage(), e);
        }
    }

    @Override
    public User insert(User user) throws InventoException {
        log.info("Inserting firm {}", user);
        try {
            return userRepo.insert(user);
        }
        catch (Exception e) {
            log.error("Error inserting user {}: ", user, e);
            throw new InventoException("Failed to insert user: " + e.getMessage(), e);
        }
    }

    @Override
    public User update(User user) throws InventoException {
        log.info("Updating firm {}", user);
        try {
            return userRepo.update(user);
        } catch (Exception e) {
            log.error("Error updating firm {}: ", user, e);
            throw new InventoException("Failed to update user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        log.info("Deleting user with id {}", id);
        try {
            return userRepo.delete(id);
        } catch (Exception e) {
            log.error("Error deleting user {}: ", id, e);
            throw new InventoException("Failed to delete user: " + e.getMessage(), e);
        }
    }
}
