package com.hangang.web.activity;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class ActivityForm {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String description;

    @NotNull(message = "금액을 입력해주세요.")
    @Min(value = 0, message = "금액은 0 이상이어야 합니다.")
    private Integer price;

    @NotNull(message = "등록 시작일을 입력해주세요.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate registrationStartDate;

    @NotNull(message = "등록 종료일을 입력해주세요.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate registrationEndDate;

    @NotNull(message = "활동 시작일을 입력해주세요.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate activityStartDate;

    @NotNull(message = "활동 종료일을 입력해주세요.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate activityEndDate;

    @NotNull(message = "하루당 정원을 입력해주세요.")
    @Min(value = 1, message = "하루당 정원은 1명 이상이어야 합니다.")
    private Integer dailyCapacity;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDate getRegistrationStartDate() {
        return registrationStartDate;
    }

    public void setRegistrationStartDate(LocalDate registrationStartDate) {
        this.registrationStartDate = registrationStartDate;
    }

    public LocalDate getRegistrationEndDate() {
        return registrationEndDate;
    }

    public void setRegistrationEndDate(LocalDate registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
    }

    public LocalDate getActivityStartDate() {
        return activityStartDate;
    }

    public void setActivityStartDate(LocalDate activityStartDate) {
        this.activityStartDate = activityStartDate;
    }

    public LocalDate getActivityEndDate() {
        return activityEndDate;
    }

    public void setActivityEndDate(LocalDate activityEndDate) {
        this.activityEndDate = activityEndDate;
    }

    public Integer getDailyCapacity() {
        return dailyCapacity;
    }

    public void setDailyCapacity(Integer dailyCapacity) {
        this.dailyCapacity = dailyCapacity;
    }
}
