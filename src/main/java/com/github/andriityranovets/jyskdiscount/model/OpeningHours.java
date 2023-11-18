package com.github.andriityranovets.jyskdiscount.model;

import com.google.gson.annotations.SerializedName;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@RecordBuilder
public record OpeningHours(
        DayOfWeek day,
        @SerializedName("starthours") LocalTime startHours,
        @SerializedName("endhours") LocalTime endHours,
        @SerializedName("comment") LocalDate date
        ) {
}
