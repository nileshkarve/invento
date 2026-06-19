package in.maitra.treats.invento.controller;

import in.maitra.treats.invento.entity.Vendor;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.service.VendorService;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendor")
@Tag(name = "Vendor", description = "Vendor management APIs")
@RequiredArgsConstructor
@Slf4j
public class VendorController {

    private final VendorService vendorService;

    @PostMapping("/getAll")
    public ResponseEntity<PagedResponse<Vendor>> getAll(@RequestBody PaginationFilter filter) throws InventoException {
        log.info("Received request to get all vendors with filter: {}", filter);
        return ResponseEntity.ok(vendorService.getAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> get(@PathVariable String id) throws InventoException {
        log.info("Received request to get vendor with ID: {}", id);
        return vendorService.get(EntityId.fromString(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping("/insert")
    public ResponseEntity<Vendor> insert(@RequestBody Vendor vendor) throws InventoException {
        log.info("Received request to insert vendor: {}", vendor);
        return ResponseEntity.ok(vendorService.insert(vendor));
    }

    @PostMapping("/update")
    public ResponseEntity<Vendor> update(@RequestBody Vendor vendor) throws InventoException {
        log.info("Received request to update vendor: {}", vendor);
        return ResponseEntity.ok(vendorService.update(vendor));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) throws InventoException {
        log.info("Received request to delete vendor with ID: {}", id);
        return ResponseEntity.ok(vendorService.delete(EntityId.fromString(id)));
    }
}
