package in.maitra.treats.invento.util.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@ToString
public class Filter implements Serializable {

    /** Entity on which filter is to be applied */
    private final String entityName;

    private final List<FilterTuple> filterTuples;

    @JsonCreator
    public Filter(@JsonProperty("entityName") String entityName, @JsonProperty("filterTuples") List<FilterTuple> filterTuples) {
        this.entityName = entityName;
        this.filterTuples = filterTuples;
    }
}
