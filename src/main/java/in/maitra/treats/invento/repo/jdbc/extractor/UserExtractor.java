package in.maitra.treats.invento.repo.jdbc.extractor;

import in.maitra.treats.invento.entity.Firm;
import in.maitra.treats.invento.entity.User;
import in.maitra.treats.invento.repo.jdbc.FirmJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.UserJdbcRepo;
import in.maitra.treats.invento.repo.jdbc.mapper.FirmMapper;
import in.maitra.treats.invento.repo.jdbc.mapper.UserMapper;
import in.maitra.treats.invento.util.persistence.EntityId;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserExtractor extends AbstractJdbcExtractor<User> {

    @Override
    public List<User> extractData(ResultSet rs) throws DataAccessException, SQLException {
        Map<EntityId, User> userIdMap = new LinkedHashMap<>();

        while (rs.next()) {
            User user = new UserMapper(UserJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
            User alreadyCreatedUser = userIdMap.computeIfAbsent(user.getId(), id -> user);
            if(alreadyCreatedUser.getFirm() != null) {
                Firm firm = new FirmMapper(FirmJdbcRepo.TABLE_ALIAS).createEntityFromResultSet(rs);
                alreadyCreatedUser.setFirm(firm);
            }
        }
        return userIdMap.values().stream().toList();
    }
}
