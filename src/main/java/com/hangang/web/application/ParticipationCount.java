package com.hangang.web.application;

import java.time.LocalDate;

public class ParticipationCount {

    private LocalDate participationDate;
    private int count;

    public LocalDate getParticipationDate() {
        return participationDate;
    }

    public void setParticipationDate(LocalDate participationDate) {
        this.participationDate = participationDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
