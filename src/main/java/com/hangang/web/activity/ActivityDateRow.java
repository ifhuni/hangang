package com.hangang.web.activity;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class ActivityDateRow {

    private final LocalDate date;
    private final int capacity;
    private final int appliedCount;
    private final String dayOfWeek;

    public ActivityDateRow(LocalDate date, int capacity, int appliedCount) {
        this.date = date;
        this.capacity = capacity;
        this.appliedCount = appliedCount;
        this.dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
    }

    public LocalDate getDate() {
        return date;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAppliedCount() {
        return appliedCount;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getRemaining() {
        return capacity - appliedCount;
    }

    public boolean isFull() {
        return getRemaining() <= 0;
    }
}
