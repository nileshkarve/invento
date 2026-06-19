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
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Vendor implements PersistableEntity {
    private EntityId id;
    @Filterable(columnName = "name")
    private String name;
    private String description;
    @Filterable(columnName = "contact_person")
    private String contactPerson;
    private String contactNumber;
    @Filterable(columnName = "email")
    private String email;
    @Filterable(columnName = "gst_number")
    private String gstNumber;
    private String address1;
    private String address2;
    @Filterable(columnName = "city")
    private String city;
    @Filterable(columnName = "state")
    private String state;
    @Filterable(columnName = "country")
    private String country;
    private String postalCode;
    @Filterable(columnName = "status")
    private Status status;
    @Filterable(columnName = "firm_id")
    private Firm firm;
}
