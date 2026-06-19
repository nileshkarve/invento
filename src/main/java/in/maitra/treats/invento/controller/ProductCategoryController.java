package in.maitra.treats.invento.controller;

import in.maitra.treats.invento.entity.ProductCategory;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.service.ProductCategoryService;
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
@RequestMapping("/productCategory")
@Tag(name = "Product Category", description = "Product Category management APIs")
@RequiredArgsConstructor
@Slf4j
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping("/getAll")
    public ResponseEntity<PagedResponse<ProductCategory>> getAll(@RequestBody PaginationFilter filter)
            throws InventoException {
        return ResponseEntity.ok(productCategoryService.getAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> get(@PathVariable String id) throws InventoException {
        return productCategoryService.get(EntityId.fromString(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping("/insert")
    public ResponseEntity<ProductCategory> insert(@RequestBody ProductCategory productCategory)
            throws InventoException {
        return ResponseEntity.ok(productCategoryService.insert(productCategory));
    }

    @PostMapping("/update")
    public ResponseEntity<ProductCategory> update(@RequestBody ProductCategory productCategory)
            throws InventoException {
        return ResponseEntity.ok(productCategoryService.update(productCategory));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) throws InventoException {
        return ResponseEntity.ok(productCategoryService.delete(EntityId.fromString(id)));
    }
}
