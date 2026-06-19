package in.maitra.treats.invento.controller;

import in.maitra.treats.invento.entity.RawMaterial;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.service.RawMaterialService;
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
@RequestMapping("/rawmaterial")
@Tag(name = "RawMaterial", description = "Raw Material management APIs")
@RequiredArgsConstructor
@Slf4j
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @PostMapping("/getAll")
    public ResponseEntity<PagedResponse<RawMaterial>> getAll(@RequestBody PaginationFilter filter) throws InventoException {
        log.info("Received request to get all raw materials with filter: {}", filter);
        return ResponseEntity.ok(rawMaterialService.getAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterial> get(@PathVariable String id) throws InventoException {
        log.info("Received request to get raw material with ID: {}", id);
        return rawMaterialService.get(EntityId.fromString(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping("/insert")
    public ResponseEntity<RawMaterial> insert(@RequestBody RawMaterial rawMaterial) throws InventoException {
        log.info("Received request to insert raw material: {}", rawMaterial);
        return ResponseEntity.ok(rawMaterialService.insert(rawMaterial));
    }

    @PostMapping("/update")
    public ResponseEntity<RawMaterial> update(@RequestBody RawMaterial rawMaterial) throws InventoException {
        log.info("Received request to update raw material: {}", rawMaterial);
        return ResponseEntity.ok(rawMaterialService.update(rawMaterial));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) throws InventoException {
        log.info("Received request to delete raw material with ID: {}", id);
        return ResponseEntity.ok(rawMaterialService.delete(EntityId.fromString(id)));
    }
}

