package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.Vendor;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.VendorRepo;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepo<Vendor> vendorRepo;

    @Override
    public PagedResponse<Vendor> getAll(PaginationFilter filter) throws InventoException {
        log.info("Getting all vendors matching filter {}", filter);
        try {
            return vendorRepo.getAll(filter);
        } catch (Exception e) {
            log.error("Error getting vendors with filter {}", filter, e);
            throw new InventoException("Failed to get vendors: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Vendor> get(EntityId id) throws InventoException {
        log.info("Getting vendor with id {}", id);
        try {
            return vendorRepo.getById(id);
        } catch (Exception e) {
            log.error("Error getting vendor with id {}", id, e);
            throw new InventoException("Failed to get vendor: " + e.getMessage(), e);
        }
    }

    @Override
    public Vendor insert(Vendor vendor) throws InventoException {
        log.info("Inserting vendor {}", vendor);
        try {
            return vendorRepo.insert(vendor);
        }
        catch (Exception e) {
            log.error("Error inserting vendor {}: ", vendor, e);
            throw new InventoException("Failed to insert vendor: " + e.getMessage(), e);
        }
    }

    @Override
    public Vendor update(Vendor vendor) throws InventoException {
        log.info("Updating vendor {}", vendor);
        try {
            return vendorRepo.update(vendor);
        } catch (Exception e) {
            log.error("Error updating vendor {}: ", vendor, e);
            throw new InventoException("Failed to update vendor: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        log.info("Deleting vendor with id {}", id);
        try {
            return vendorRepo.delete(id);
        } catch (Exception e) {
            log.error("Error deleting vendor {}: ", id, e);
            throw new InventoException("Failed to delete vendor: " + e.getMessage(), e);
        }
    }
}
