package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.Vendor;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.util.Optional;

public interface VendorService {
    PagedResponse<Vendor> getAll(PaginationFilter filter) throws InventoException;

    Optional<Vendor> get(EntityId id) throws InventoException;

    Vendor insert(Vendor vendor) throws InventoException;

    Vendor update(Vendor vendor) throws InventoException;

    boolean delete(EntityId id) throws InventoException;
}
