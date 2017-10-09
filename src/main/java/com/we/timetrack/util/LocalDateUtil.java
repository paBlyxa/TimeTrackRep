package com.we.timetrack.util;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.we.timetrack.service.model.DateRange;

public class LocalDateUtil {
	
	public static class LocalDateSerializer extends JsonSerializer<LocalDate> {
	    @Override
	    public void serialize(LocalDate arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException, JsonProcessingException {
	        arg1.writeString(arg0.format(DateRange.dateFormat));
	    }
	}

	public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
	    @Override
	    public LocalDate deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException, JsonProcessingException {
	        return LocalDate.parse(arg0.getText(), DateRange.dateFormat);
	    }
	}
}