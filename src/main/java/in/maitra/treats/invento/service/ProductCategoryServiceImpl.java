package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.ProductCategory;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.repo.ProductCategoryRepo;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepo<ProductCategory> productCategoryRepo;

    @Override
    public PagedResponse<ProductCategory> getAll(PaginationFilter filter) throws InventoException {
        try {
            return productCategoryRepo.getAll(filter);
        } catch (Exception e) {
            log.error("Error getting product categories with filter {}", filter, e);
            throw new InventoException("Failed to get product categories: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<ProductCategory> get(EntityId id) throws InventoException {
        try {
            return productCategoryRepo.getById(id);
        } catch (Exception e) {
            log.error("Error getting product category with id {}", id, e);
            throw new InventoException("Failed to get product category: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductCategory insert(ProductCategory productCategory) throws InventoException {
        try {
            return productCategoryRepo.insert(productCategory);
        } catch (Exception e) {
            log.error("Error inserting product category {}", productCategory, e);
            throw new InventoException("Failed to insert product category: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductCategory update(ProductCategory productCategory) throws InventoException {
        try {
            return productCategoryRepo.update(productCategory);
        } catch (Exception e) {
            log.error("Error updating product category {}", productCategory, e);
            throw new InventoException("Failed to update product category: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(EntityId id) throws InventoException {
        try {
            return productCategoryRepo.delete(id);
        } catch (Exception e) {
            log.error("Error deleting product category with id {}", id, e);
            throw new InventoException("Failed to delete product category: " + e.getMessage(), e);
        }
    }
}
