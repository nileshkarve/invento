package in.maitra.treats.invento.repo.jdbc.extractor;

import in.maitra.treats.invento.entity.Firm;
import in.maitra.treats.invento.entity.Product;
import in.maitra.treats.invento.entity.ProductCategory;
import in.maitra.treats.invento.repo.jdbc.FirmJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.ProductCategoryJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.ProductJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.mapper.FirmMapper;
import in.maitra.treats.invento.repo.jdbc.mapper.ProductCategoryMapper;
import in.maitra.treats.invento.repo.jdbc.mapper.ProductMapper;
import in.maitra.treats.invento.util.persistence.EntityId;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductExtractor extends AbstractJdbcExtractor<Product> {
    @Override
    public List<Product> extractData(ResultSet rs) throws DataAccessException, SQLException {
        Map<EntityId, Product> products = new LinkedHashMap<>();
        while (rs.next()) {
            Product product = new ProductMapper(ProductJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
            Product mappedProduct = products.computeIfAbsent(product.getId(), id -> product);
            if (mappedProduct.getCategory() == null) {
                ProductCategory category =
                        new ProductCategoryMapper(ProductCategoryJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
                Firm firm = new FirmMapper(FirmJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
                category.setFirm(firm);
                mappedProduct.setCategory(category);
            }
        }
        return products.values().stream().toList();
    }
}
