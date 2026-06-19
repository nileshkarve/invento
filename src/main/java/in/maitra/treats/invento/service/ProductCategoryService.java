package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.ProductCategory;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.util.Optional;

public interface ProductCategoryService {
    PagedResponse<ProductCategory> getAll(PaginationFilter filter) throws InventoException;
    Optional<ProductCategory> get(EntityId id) throws InventoException;
    ProductCategory insert(ProductCategory productCategory) throws InventoException;
    ProductCategory update(ProductCategory productCategory) throws InventoException;
    boolean delete(EntityId id) throws InventoException;
}
