package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.Firm;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.FirmRepo;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirmServiceImpl implements FirmService {

    private final FirmRepo<Firm> firmRepo;

    @Override
    public PagedResponse<Firm> getAll(PaginationFilter filter) throws InventoException {
        log.info("Getting all firms matching filter {}", filter);
        try {
            return firmRepo.getAll(filter);
        } catch (Exception e) {
            log.error("Error getting firms with filter {}", filter, e);
            throw new InventoException("Failed to get firms: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Firm> get(EntityId id) throws InventoException {
        log.info("Getting firm with id {}", id);
        try {
            return firmRepo.getById(id);
        } catch (Exception e) {
            log.error("Error getting firm with id {}", id, e);
            throw new InventoException("Failed to get firm: " + e.getMessage(), e);
        }
    }

    @Override
    public Firm insert(Firm firm) throws InventoException {
        log.info("Inserting firm {}", firm);
        try {
            return firmRepo.insert(firm);
        }
        catch (Exception e) {
            log.error("Error inserting firm {}: ", firm, e);
            throw new InventoException("Failed to insert firm: " + e.getMessage(), e);
        }
    }

    @Override
    public Firm update(Firm firm) throws InventoException {
        log.info("Updating firm {}", firm);
        try {
            return firmRepo.update(firm);
        } catch (Exception e) {
            log.error("Error updating firm {}: ", firm, e);
            throw new InventoException("Failed to update firm: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        log.info("Deleting firm with id {}", id);
        try {
            return firmRepo.delete(id);
        } catch (Exception e) {
            log.error("Error deleting firm {}: ", id, e);
            throw new InventoException("Failed to delete firm: " + e.getMessage(), e);
        }
    }
}
