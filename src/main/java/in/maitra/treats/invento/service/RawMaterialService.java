package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.RawMaterial;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.util.Optional;

public interface RawMaterialService {
    PagedResponse<RawMaterial> getAll(PaginationFilter filter) throws InventoException;

    Optional<RawMaterial> get(EntityId id) throws InventoException;

    RawMaterial insert(RawMaterial rawMaterial) throws InventoException;

    RawMaterial update(RawMaterial rawMaterial) throws InventoException;

    boolean delete(EntityId id) throws InventoException;
}

