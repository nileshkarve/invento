package in.maitra.treats.invento.util.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PaginationFilter extends Filter {

    /** Page number for pagination (0-based) */
    private final Integer pageNumber;

    /** Number of items per page */
    private final Integer pageSize;

    @JsonCreator
    public PaginationFilter(@JsonProperty("entityName") String entityName, @JsonProperty("pageNumber") Integer pageNumber, @JsonProperty("pageSize") Integer pageSize, @JsonProperty("filterTuples") List<FilterTuple> filterTuples) {
        super(entityName, filterTuples);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
