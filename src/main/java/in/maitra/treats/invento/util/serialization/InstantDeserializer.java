/**
 * 
 */
package in.maitra.treats.invento.util.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import in.maitra.treats.invento.util.InventoConstant;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


/**
 * Jackson deserializer for Instant using Muktashastra date format.
 * 
 * @author Nilesh
 */
public class InstantDeserializer extends JsonDeserializer<Instant> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(InventoConstant.LOCAL_DATE_TIME_FORMAT);

    /**
     * Deserializes JSON string to Instant.
     * 
     * @param p the JSON parser
     * @param ctxt the deserialization context
     * @return parsed Instant
     * @throws IOException if parsing fails
     * @throws JacksonException if JSON processing fails
     */
    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateStr = p.getText();
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
        return localDateTime.toInstant(ZoneOffset.UTC);
    }
}
