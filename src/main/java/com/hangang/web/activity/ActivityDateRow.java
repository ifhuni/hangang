package com.hangang.web.activity;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class ActivityDateRow {

    private final LocalDate date;
    private final int capacity;
    private final int approvedCount;
    private final String dayOfWeek;

    public ActivityDateRow(LocalDate date, int capacity, int approvedCount) {
        this.date = date;
        this.capacity = capacity;
        this.approvedCount = approvedCount;
        this.dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
    }

    public LocalDate getDate() {
        return date;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getApprovedCount() {
        return approvedCount;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getRemaining() {
        return capacity - approvedCount;
    }

    public boolean isFull() {
        return getRemaining() <= 0;
    }
}
