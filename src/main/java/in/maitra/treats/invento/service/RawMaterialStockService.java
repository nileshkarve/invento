package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.RawMaterialStock;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.util.Optional;

public interface RawMaterialStockService {
    PagedResponse<RawMaterialStock> getAll(PaginationFilter filter) throws InventoException;
    Optional<RawMaterialStock> get(EntityId id) throws InventoException;
    RawMaterialStock insert(RawMaterialStock inventory) throws InventoException;
    RawMaterialStock update(RawMaterialStock inventory) throws InventoException;
    boolean delete(EntityId id) throws InventoException;
}
