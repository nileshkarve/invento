package in.maitra.treats.invento.controller;

import in.maitra.treats.invento.entity.RawMaterialStock;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.service.RawMaterialStockService;
import in.maitra.treats.invento.util.filter.PaginationFilter;
import in.maitra.treats.invento.util.persistence.EntityId;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rawMaterialStock")
@Tag(name = "Raw Material Stock", description = "Raw Material Stock management APIs")
@RequiredArgsConstructor
public class RawMaterialStockController {
    private final RawMaterialStockService stockService;

    @PostMapping("/getAll")
    public ResponseEntity<PagedResponse<RawMaterialStock>> getAll(@RequestBody PaginationFilter filter)
            throws InventoException {
        return ResponseEntity.ok(stockService.getAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterialStock> get(@PathVariable String id) throws InventoException {
        return stockService.get(EntityId.fromString(id))
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping("/insert")
    public ResponseEntity<RawMaterialStock> insert(@RequestBody RawMaterialStock inventory)
            throws InventoException {
        return ResponseEntity.ok(stockService.insert(inventory));
    }

    @PostMapping("/update")
    public ResponseEntity<RawMaterialStock> update(@RequestBody RawMaterialStock inventory)
            throws InventoException {
        return ResponseEntity.ok(stockService.update(inventory));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) throws InventoException {
        return ResponseEntity.ok(stockService.delete(EntityId.fromString(id)));
    }
}
