package in.maitra.treats.invento.util.persistence;

/**
 * Enumeration representing the various states of an entity lifecycle.
 * 
 * @author Nilesh
 */
public enum Status {
    /** Entity is newly created */
    NEW,
    
    /** Entity has been approved */
    APPROVED,
    
    /** Entity is active and operational */
    ACTIVE,
    
    /** Entity is temporarily inactive */
    INACTIVE,
    
    /** Entity has been rejected */
    REJECTED,
    
    /** Entity is disabled */
    DISABLED,
    
    /** Entity is marked for deletion */
    DELETED
}
