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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements PersistableEntity {
    private EntityId id;
    @Filterable(columnName = "name")
    private String name;
    @Filterable(columnName = "email")
    private String email;
    @Filterable(columnName = "contact_number")
    private String contactNumber;
    @Filterable(columnName = "status")
    private Status status;
    @Filterable(columnName = "firm_id")
    private Firm firm;
}
