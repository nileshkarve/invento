package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.Product;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.ProductRepo;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo<Product> productRepo;

    @Override
    public PagedResponse<Product> getAll(PaginationFilter filter) throws InventoException {
        try {
            return productRepo.getAll(filter);
        } catch (Exception e) {
            log.error("Error getting products with filter {}", filter, e);
            throw new InventoException("Failed to get products: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Product> get(EntityId id) throws InventoException {
        try {
            return productRepo.getById(id);
        } catch (Exception e) {
            log.error("Error getting product with id {}", id, e);
            throw new InventoException("Failed to get product: " + e.getMessage(), e);
        }
    }

    @Override
    public Product insert(Product product) throws InventoException {
        try {
            return productRepo.insert(product);
        } catch (Exception e) {
            log.error("Error inserting product {}", product, e);
            throw new InventoException("Failed to insert product: " + e.getMessage(), e);
        }
    }

    @Override
    public Product update(Product product) throws InventoException {
        try {
            return productRepo.update(product);
        } catch (Exception e) {
            log.error("Error updating product {}", product, e);
            throw new InventoException("Failed to update product: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        try {
            return productRepo.delete(id);
        } catch (Exception e) {
            log.error("Error deleting product with id {}", id, e);
            throw new InventoException("Failed to delete product: " + e.getMessage(), e);
        }
    }
}
