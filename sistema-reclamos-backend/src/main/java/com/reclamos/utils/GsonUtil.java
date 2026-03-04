package com.reclamos.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GsonUtil {

  private static final Gson INSTANCE;

  static {
    INSTANCE = new GsonBuilder()
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
      .serializeNulls()
      .create();
  }

  public static Gson getGson() {
    return INSTANCE;
  }

  private static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
      if (value == null) {
        out.nullValue();
      } else {
        out.value(value.format(formatter));
      }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
      String value = in.nextString();
      return value != null ? LocalDateTime.parse(value, formatter) : null;
    }
  }

}
