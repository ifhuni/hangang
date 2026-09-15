package com.hangang.web.activity;

import java.time.LocalDate;

public class ActivityDateRow {

    private final LocalDate date;
    private final int capacity;
    private final int appliedCount;

    public ActivityDateRow(LocalDate date, int capacity, int appliedCount) {
        this.date = date;
        this.capacity = capacity;
        this.appliedCount = appliedCount;
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
}
