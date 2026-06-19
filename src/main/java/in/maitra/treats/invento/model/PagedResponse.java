package in.maitra.treats.invento.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Generic paged response wrapper for paginated data.
 * 
 * @param <T> the type of content in the response
 * @author Nilesh
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PagedResponse<T> implements Serializable {
    /** List of content items for current page */
    private List<T> content;
    
    /** Current page number (0-based) */
    private Integer pageNumber;
    
    /** Number of items per page */
    private Integer pageSize;
    
    /** Total number of elements across all pages */
    private Long totalElements;
    
    /** Total number of pages */
    private Integer totalPages;
    
    /** Whether this is the first page */
    private boolean first;
    
    /** Whether this is the last page */
    private boolean last;

    /**
     * Creates a paged response with pagination metadata.
     * 
     * @param content the list of items for current page
     * @param pageNumber current page number (0-based)
     * @param pageSize number of items per page
     * @param totalElements total number of elements
     */
    public PagedResponse(List<T> content, int pageNumber, int pageSize, Long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.first = pageNumber == 0;
        this.last = pageNumber >= totalPages - 1;
    }
}