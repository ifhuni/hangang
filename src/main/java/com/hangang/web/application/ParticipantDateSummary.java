package com.hangang.web.application;

import java.time.LocalDate;

public class ParticipantDateSummary {

    private final LocalDate date;
    private final int capacity;
    private final int approvedCount;
    private final int pendingCount;

    public ParticipantDateSummary(LocalDate date, int capacity, int approvedCount, int pendingCount) {
        this.date = date;
        this.capacity = capacity;
        this.approvedCount = approvedCount;
        this.pendingCount = pendingCount;
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

    public int getPendingCount() {
        return pendingCount;
    }
}
