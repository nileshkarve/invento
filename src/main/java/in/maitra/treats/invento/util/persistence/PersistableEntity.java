package in.maitra.treats.invento.util.persistence;

import java.io.Serializable;

/**
 * Base interface for all persistable entities in the system.
 * Provides common functionality for entity identification.
 * 
 * @author Nilesh
 */
public interface PersistableEntity extends Serializable {
    
    /**
     * Sets the unique identifier for this entity.
     * 
     * @param id the unique identifier
     */
    void setId(EntityId id);
    
    /**
     * Sets the status for this entity.
     * 
     * @param status the status
     */
    void setStatus(Status status);

    default String getEntityName() {
        return this.getClass().getSimpleName();
    }
}
