package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.Product;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.util.Optional;

public interface ProductService {
    PagedResponse<Product> getAll(PaginationFilter filter) throws InventoException;
    Optional<Product> get(EntityId id) throws InventoException;
    Product insert(Product product) throws InventoException;
    Product update(Product product) throws InventoException;
    boolean delete(EntityId id) throws InventoException;
}
