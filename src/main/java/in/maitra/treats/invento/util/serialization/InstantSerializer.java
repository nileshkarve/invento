/**
 * 
 */
package in.maitra.treats.invento.util.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import in.maitra.treats.invento.util.InventoConstant;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Jackson serializer for Instant using Muktashastra date format.
 * 
 * @author Nilesh
 */
public class InstantSerializer extends JsonSerializer<Instant> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(InventoConstant.LOCAL_DATE_TIME_FORMAT);

    /**
     * Serializes Instant to JSON string.
     * 
     * @param value the Instant to serialize
     * @param gen the JSON generator
     * @param serializers the serializer provider
     * @throws IOException if serialization fails
     */
    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(value, ZoneOffset.UTC);
        gen.writeString(localDateTime.format(formatter));
    }
}