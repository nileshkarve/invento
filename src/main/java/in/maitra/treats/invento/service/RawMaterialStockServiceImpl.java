package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.RawMaterialStock;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.RawMaterialStockRepo;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RawMaterialStockServiceImpl implements RawMaterialStockService {
    private final RawMaterialStockRepo<RawMaterialStock> stockRepo;

    @Override
    public PagedResponse<RawMaterialStock> getAll(PaginationFilter filter) throws InventoException {
        try { return stockRepo.getAll(filter); }
        catch (Exception e) {
            log.error("Error getting raw material inventory with filter {}", filter, e);
            throw new InventoException("Failed to get raw material inventory: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<RawMaterialStock> get(EntityId id) throws InventoException {
        try { return stockRepo.getById(id); }
        catch (Exception e) {
            log.error("Error getting raw material inventory with id {}", id, e);
            throw new InventoException("Failed to get raw material inventory: " + e.getMessage(), e);
        }
    }

    @Override
    public RawMaterialStock insert(RawMaterialStock inventory) throws InventoException {
        try { return stockRepo.insert(inventory); }
        catch (Exception e) {
            log.error("Error inserting raw material inventory {}", inventory, e);
            throw new InventoException("Failed to insert raw material inventory: " + e.getMessage(), e);
        }
    }

    @Override
    public RawMaterialStock update(RawMaterialStock inventory) throws InventoException {
        try { return stockRepo.update(inventory); }
        catch (Exception e) {
            log.error("Error updating raw material inventory {}", inventory, e);
            throw new InventoException("Failed to update raw material inventory: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        try { return stockRepo.delete(id); }
        catch (Exception e) {
            log.error("Error deleting raw material inventory with id {}", id, e);
            throw new InventoException("Failed to delete raw material inventory: " + e.getMessage(), e);
        }
    }
}
