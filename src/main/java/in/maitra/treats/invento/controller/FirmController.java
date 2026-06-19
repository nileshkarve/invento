package in.maitra.treats.invento.controller;

import in.maitra.treats.invento.entity.Firm;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.service.FirmService;
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
@RequestMapping("/firm")
@Tag(name = "Firm", description = "Firm management APIs")
@RequiredArgsConstructor
@Slf4j
public class FirmController {

    private final FirmService firmService;

    @PostMapping("/getAll")
    public ResponseEntity<PagedResponse<Firm>> getAll(@RequestBody PaginationFilter filter) throws InventoException {
        log.info("Received request to get all firms with filter: {}", filter);
        return ResponseEntity.ok(firmService.getAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Firm> get(@PathVariable String id) throws InventoException {
        log.info("Received request to get firm with ID: {}", id);
        return firmService.get(EntityId.fromString(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping("/insert")
    public ResponseEntity<Firm> insert(@RequestBody Firm firm) throws InventoException {
        log.info("Received request to insert firm: {}", firm);
        return ResponseEntity.ok(firmService.insert(firm));
    }

    @PostMapping("/update")
    public ResponseEntity<Firm> update(@RequestBody Firm firm) throws InventoException {
        log.info("Received request to update firm: {}", firm);
        return ResponseEntity.ok(firmService.update(firm));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) throws InventoException {
        log.info("Received request to delete firm with ID: {}", id);
        return ResponseEntity.ok(firmService.delete(EntityId.fromString(id)));
    }
}
