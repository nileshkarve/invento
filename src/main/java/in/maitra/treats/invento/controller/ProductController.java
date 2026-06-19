package in.maitra.treats.invento.controller;

import in.maitra.treats.invento.entity.Product;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.service.ProductService;
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
@RequestMapping("/product")
@Tag(name = "Product", description = "Product management APIs")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/getAll")
    public ResponseEntity<PagedResponse<Product>> getAll(@RequestBody PaginationFilter filter)
            throws InventoException {
        return ResponseEntity.ok(productService.getAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable String id) throws InventoException {
        return productService.get(EntityId.fromString(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping("/insert")
    public ResponseEntity<Product> insert(@RequestBody Product product) throws InventoException {
        return ResponseEntity.ok(productService.insert(product));
    }

    @PostMapping("/update")
    public ResponseEntity<Product> update(@RequestBody Product product) throws InventoException {
        return ResponseEntity.ok(productService.update(product));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) throws InventoException {
        return ResponseEntity.ok(productService.delete(EntityId.fromString(id)));
    }
}
