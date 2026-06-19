package in.maitra.treats.invento.service;

import in.maitra.treats.invento.entity.Firm;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.util.Optional;

public interface FirmService {
    PagedResponse<Firm> getAll(PaginationFilter filter) throws InventoException;

    Optional<Firm> get(EntityId id) throws InventoException;

    Firm insert(Firm firm) throws InventoException;

    Firm update(Firm firm) throws InventoException;

    boolean delete(EntityId id) throws InventoException;
}
