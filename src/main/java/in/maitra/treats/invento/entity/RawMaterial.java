package in.maitra.treats.invento.entity;

import in.maitra.treats.invento.util.MeasuringUnit;
import in.maitra.treats.invento.util.RawMaterialType;
import in.maitra.treats.invento.util.filter.Filterable;
import in.maitra.treats.invento.util.persistence.EntityId;
import in.maitra.treats.invento.util.persistence.PersistableEntity;
import in.maitra.treats.invento.util.persistence.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RawMaterial implements PersistableEntity {
    private EntityId id;
    @Filterable(columnName = "name")
    private String name;
    private String description;
    private MeasuringUnit measuringUnit;
    private RawMaterialType rawMaterialType;
    @Filterable(columnName = "status")
    private Status status;
    @Filterable(columnName = "firm_id")
    private Firm firm;
}
