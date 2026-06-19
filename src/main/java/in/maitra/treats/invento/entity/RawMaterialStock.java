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
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RawMaterialStock implements PersistableEntity {
    private EntityId id;
    @Filterable(columnName = "raw_material_id")
    private RawMaterial rawMaterial;
    @Filterable(columnName = "vendor_id")
    private Vendor vendor;
    @Filterable(columnName = "quantity")
    private BigDecimal quantity;
    @Filterable(columnName = "price_per_unit")
    private BigDecimal pricePerUnit;
    @Filterable(columnName = "batch_code")
    private String batchCode;
    @Filterable(columnName = "manufacturing_date")
    private LocalDate manufacturingDate;
    @Filterable(columnName = "purchase_date")
    private LocalDate purchaseDate;
    @Filterable(columnName = "expiry_date")
    private LocalDate expiryDate;
    @Filterable(columnName = "status")
    private Status status;
}
