package in.maitra.treats.invento.repo.jdbc.extractor;

import in.maitra.treats.invento.util.persistence.PersistableEntity;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;

public abstract class AbstractJdbcExtractor<T extends PersistableEntity> implements ResultSetExtractor<List<T>> {
}
