package in.maitra.treats.invento.controller;

import in.maitra.treats.invento.entity.User;
import in.maitra.treats.invento.exception.InventoException;
import in.maitra.treats.invento.model.PagedResponse;
import in.maitra.treats.invento.service.UserService;
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
@RequestMapping("/usr")
@Tag(name = "User", description = "User management APIs")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/getAll")
    public ResponseEntity<PagedResponse<User>> getAll(@RequestBody PaginationFilter filter) throws InventoException {
        log.info("Received request to get all users with filter: {}", filter);
        return ResponseEntity.ok(userService.getAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable String id) throws InventoException {
        log.info("Received request to get user with ID: {}", id);
        return userService.get(EntityId.fromString(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(null));
    }

    @PostMapping("/insert")
    public ResponseEntity<User> insert(@RequestBody User user) throws InventoException {
        log.info("Received request to insert user: {}", user);
        return ResponseEntity.ok(userService.insert(user));
    }

    @PostMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) throws InventoException {
        log.info("Received request to update user: {}", user);
        return ResponseEntity.ok(userService.update(user));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) throws InventoException {
        log.info("Received request to delete user with ID: {}", id);
        return ResponseEntity.ok(userService.delete(EntityId.fromString(id)));
    }
}
