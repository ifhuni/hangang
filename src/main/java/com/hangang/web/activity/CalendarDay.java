package com.hangang.web.activity;

import java.time.LocalDate;

public class CalendarDay {

    private final LocalDate date;
    private final ActivityDateRow row;

    private CalendarDay(LocalDate date, ActivityDateRow row) {
        this.date = date;
        this.row = row;
    }

    public static CalendarDay blank() {
        return new CalendarDay(null, null);
    }

    public static CalendarDay of(LocalDate date, ActivityDateRow row) {
        return new CalendarDay(date, row);
    }

    public boolean isBlank() {
        return date == null;
    }

    public boolean isSelectable() {
        return row != null;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDayOfMonth() {
        return date != null ? date.getDayOfMonth() : 0;
    }

    public ActivityDateRow getRow() {
        return row;
    }
}
