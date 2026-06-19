package in.maitra.treats.invento.util.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import in.maitra.treats.invento.util.persistence.EntityId;

import java.io.IOException;

public class EntityIdDeserializer extends JsonDeserializer<EntityId> {

    @Override
    public EntityId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return EntityId.fromString(p.getValueAsString());
    }
}