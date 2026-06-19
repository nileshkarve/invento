package in.maitra.treats.invento.entity;

import in.maitra.treats.invento.util.filter.Filterable;
import in.maitra.treats.invento.util.persistence.EntityId;
import in.maitra.treats.invento.util.persistence.PersistableEntity;
import in.maitra.treats.invento.util.persistence.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product implements PersistableEntity {
    private EntityId id;
    @Filterable(columnName = "name")
    private String name;
    private String description;
    private BigDecimal mrp;
    @Filterable(columnName = "category_id")
    private ProductCategory category;
    private Status status;
}
