package in.maitra.treats.invento.repo.jdbc.mapper;

import in.maitra.treats.invento.entity.Vendor;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VendorMapper extends AbstractMapper<Vendor> {

    public VendorMapper(String tableAliasInQuery) {
        super(tableAliasInQuery);
    }

    private static final String ID_COLUMN_NAME = "id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String DESCRIPTION_COLUMN_NAME = "description";
    private static final String CONTACT_PERSON_COLUMN_NAME = "contact_person";
    private static final String CONTACT_NUMBER_COLUMN_NAME = "contact_number";
    private static final String EMAIL_COLUMN_NAME = "email";
    private static final String GST_NUMBER_COLUMN_NAME = "gst_number";
    private static final String ADDRESS1_COLUMN_NAME = "address1";
    private static final String ADDRESS2_COLUMN_NAME = "address2";
    private static final String CITY_COLUMN_NAME = "city";
    private static final String STATE_COLUMN_NAME = "state";
    private static final String COUNTRY_COLUMN_NAME = "country";
    private static final String POSTAL_CODE_COLUMN_NAME = "postal_code";
    private static final String STATUS_COLUMN_NAME = "status";

    @Override
    public Vendor createEntityFromResultSet(ResultSet resultSet) throws SQLException {
        Vendor vendor = new Vendor();
        EntityId vendorId = mapEntityId(resultSet.getObject(deriveResultSetColumnName(ID_COLUMN_NAME)));
        vendor.setId(vendorId);
        vendor.setName(resultSet.getString(deriveResultSetColumnName(NAME_COLUMN_NAME)));
        vendor.setDescription(resultSet.getString(deriveResultSetColumnName(DESCRIPTION_COLUMN_NAME)));
        vendor.setContactPerson(resultSet.getString(deriveResultSetColumnName(CONTACT_PERSON_COLUMN_NAME)));
        vendor.setContactNumber(resultSet.getString(deriveResultSetColumnName(CONTACT_NUMBER_COLUMN_NAME)));
        vendor.setEmail(resultSet.getString(deriveResultSetColumnName(EMAIL_COLUMN_NAME)));
        vendor.setGstNumber(resultSet.getString(deriveResultSetColumnName(GST_NUMBER_COLUMN_NAME)));
        vendor.setAddress1(resultSet.getString(deriveResultSetColumnName(ADDRESS1_COLUMN_NAME)));
        vendor.setAddress2(resultSet.getString(deriveResultSetColumnName(ADDRESS2_COLUMN_NAME)));
        vendor.setCity(resultSet.getString(deriveResultSetColumnName(CITY_COLUMN_NAME)));
        vendor.setState(resultSet.getString(deriveResultSetColumnName(STATE_COLUMN_NAME)));
        vendor.setCountry(resultSet.getString(deriveResultSetColumnName(COUNTRY_COLUMN_NAME)));
        vendor.setPostalCode(resultSet.getString(deriveResultSetColumnName(POSTAL_CODE_COLUMN_NAME)));
        vendor.setStatus(mapStatus(resultSet.getString(deriveResultSetColumnName(STATUS_COLUMN_NAME))));
        return vendor;
    }
}

