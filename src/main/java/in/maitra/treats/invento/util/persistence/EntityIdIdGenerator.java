package in.maitra.treats.invento.util.persistence;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("entityIdIdGenerator")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityIdIdGenerator  {

    public EntityId generate() {
        return new EntityId(UUID.randomUUID());
    }

//    public List<EntityId> generate(int count) {
//        List<EntityId> ids = new ArrayList<>(count);
//        for(int i = 0; i < count; i++) {
//            ids.add(generate());
//        }
//        return ids;
//    }
}
