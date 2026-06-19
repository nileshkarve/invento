package in.maitra.treats.invento.repo.jdbc.extractor;

import in.maitra.treats.invento.entity.Firm;
import in.maitra.treats.invento.entity.ProductCategory;
import in.maitra.treats.invento.repo.jdbc.FirmJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.ProductCategoryJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.mapper.FirmMapper;
import in.maitra.treats.invento.repo.jdbc.mapper.ProductCategoryMapper;
import in.maitra.treats.invento.util.persistence.EntityId;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductCategoryExtractor extends AbstractJdbcExtractor<ProductCategory> {

    @Override
    public List<ProductCategory> extractData(ResultSet rs) throws DataAccessException, SQLException {
        Map<EntityId, ProductCategory> categories = new LinkedHashMap<>();
        while (rs.next()) {
            ProductCategory category =
                    new ProductCategoryMapper(ProductCategoryJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
            ProductCategory mappedCategory = categories.computeIfAbsent(category.getId(), id -> category);
            if (mappedCategory.getFirm() == null) {
                Firm firm = new FirmMapper(FirmJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
                mappedCategory.setFirm(firm);
            }
        }
        return categories.values().stream().toList();
    }
}
