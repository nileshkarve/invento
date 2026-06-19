package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.RawMaterial;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.RawMaterialRepo;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RawMaterialServiceImpl implements RawMaterialService {

    private final RawMaterialRepo<RawMaterial> rawMaterialRepo;

    @Override
    public PagedResponse<RawMaterial> getAll(PaginationFilter filter) throws InventoException {
        log.info("Getting all raw materials matching filter {}", filter);
        try {
            return rawMaterialRepo.getAll(filter);
        } catch (Exception e) {
            log.error("Error getting raw materials with filter {}", filter, e);
            throw new InventoException("Failed to get raw materials: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<RawMaterial> get(EntityId id) throws InventoException {
        log.info("Getting raw material with id {}", id);
        try {
            return rawMaterialRepo.getById(id);
        } catch (Exception e) {
            log.error("Error getting raw material with id {}", id, e);
            throw new InventoException("Failed to get raw material: " + e.getMessage(), e);
        }
    }

    @Override
    public RawMaterial insert(RawMaterial rawMaterial) throws InventoException {
        log.info("Inserting raw material {}", rawMaterial);
        try {
            return rawMaterialRepo.insert(rawMaterial);
        }
        catch (Exception e) {
            log.error("Error inserting raw material {}: ", rawMaterial, e);
            throw new InventoException("Failed to insert raw material: " + e.getMessage(), e);
        }
    }

    @Override
    public RawMaterial update(RawMaterial rawMaterial) throws InventoException {
        log.info("Updating raw material {}", rawMaterial);
        try {
            return rawMaterialRepo.update(rawMaterial);
        } catch (Exception e) {
            log.error("Error updating raw material {}: ", rawMaterial, e);
            throw new InventoException("Failed to update raw material: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        log.info("Deleting raw material with id {}", id);
        try {
            return rawMaterialRepo.delete(id);
        } catch (Exception e) {
            log.error("Error deleting raw material {}: ", id, e);
            throw new InventoException("Failed to delete raw material: " + e.getMessage(), e);
        }
    }
}

