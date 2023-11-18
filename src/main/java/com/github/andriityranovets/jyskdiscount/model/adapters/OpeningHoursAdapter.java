package com.github.andriityranovets.jyskdiscount.model.adapters;

import com.github.andriityranovets.jyskdiscount.model.OpeningHours;
import com.github.andriityranovets.jyskdiscount.model.OpeningHoursBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class OpeningHoursAdapter extends TypeAdapter<OpeningHours> {
    private static final String KEY_DAY = "day";
    private static final String KEY_STARTHOURS = "starthours";
    private static final String KEY_ENDHOURS = "endhours";
    private static final String KEY_COMMENT = "comment";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void write(JsonWriter writer, OpeningHours value) {
        throw new RuntimeException("OpeningHoursAdapter#write() not yet implemented.");
    }

    @Override
    public OpeningHours read(JsonReader reader) throws IOException {
        var builder = OpeningHoursBuilder.builder();
        reader.beginObject();
        var token = reader.peek();
        String name;
        while (token != JsonToken.END_OBJECT) {
            if(token == JsonToken.NAME) {
                name = reader.nextName();
                switch(name) {
                    case KEY_DAY -> builder.day(DayOfWeek.of((reader.nextInt() + 6) % 7 + 1));
                    case KEY_STARTHOURS -> builder.startHours(militaryTimeToLocalTime(reader.nextInt()));
                    case KEY_ENDHOURS -> builder.endHours(militaryTimeToLocalTime(reader.nextInt()));
                    case KEY_COMMENT -> builder.date(LocalDate.parse(reader.nextString(), DATE_TIME_FORMATTER));
                    default -> reader.skipValue();
                }
            }
            token = reader.peek();
        }
        reader.endObject();

        return builder.build();
    }

    private LocalTime militaryTimeToLocalTime(int militaryTime) {
        return LocalTime.of(militaryTime / 100, militaryTime % 100);
    }
}
