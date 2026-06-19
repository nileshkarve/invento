package in.maitra.treats.invento.util.persistence;

import in.maitra.treats.invento.exception.InventoException;

import java.util.HashSet;
import java.util.Set;

public class AuditableEntityRegistry {
    private AuditableEntityRegistry() {}

    private static final Set<String> entityFilterableFieldsMap = new HashSet<>();

    public static void registerAuditableEntity(String entityName) throws InventoException {
        if(entityFilterableFieldsMap.contains(entityName)) {
            throw new InventoException("Auditable entity with name " + entityName + " is already registered in the registry. Please check your code for duplicate @Auditable annotations.");
        }
        entityFilterableFieldsMap.add(entityName);
    }

    public static boolean isEntityAuditable(String entityName) {
        return entityFilterableFieldsMap.contains(entityName);
    }
}
