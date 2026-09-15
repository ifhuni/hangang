package com.hangang.web.activity;

import java.time.YearMonth;
import java.util.List;

public class CalendarMonth {

    private final YearMonth yearMonth;
    private final List<CalendarDay> days;

    public CalendarMonth(YearMonth yearMonth, List<CalendarDay> days) {
        this.yearMonth = yearMonth;
        this.days = days;
    }

    public int getYear() {
        return yearMonth.getYear();
    }

    public int getMonth() {
        return yearMonth.getMonthValue();
    }

    public List<CalendarDay> getDays() {
        return days;
    }
}
