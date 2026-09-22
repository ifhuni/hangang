package com.hangang.web.activity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    private final ActivityMapper activityMapper;
    private final ActivityImageStorage imageStorage;

    public ActivityService(ActivityMapper activityMapper, ActivityImageStorage imageStorage) {
        this.activityMapper = activityMapper;
        this.imageStorage = imageStorage;
    }

    public List<Activity> listPublicActivities(String keyword, LocalDate date, Integer headcount, String sort) {
        return activityMapper.search(keyword, date, headcount, sort);
    }

    public Activity getActivity(Long activityId) {
        Activity activity = activityMapper.findById(activityId);
        if (activity == null) {
            throw new IllegalArgumentException("존재하지 않는 활동입니다.");
        }
        return activity;
    }

    public List<Activity> listVendorActivities(Long vendorId) {
        return activityMapper.findByVendorId(vendorId);
    }

    public Activity getVendorActivity(Long vendorId, Long activityId) {
        Activity activity = getActivity(activityId);
        if (!activity.getVendorId().equals(vendorId)) {
            throw new IllegalStateException("본인이 등록한 활동만 수정할 수 있습니다.");
        }
        return activity;
    }

    public void registerActivity(Long vendorId, ActivityForm form) {
        validateDates(form);

        Activity activity = new Activity();
        activity.setVendorId(vendorId);
        applyForm(activity, form);
        if (form.getImage() != null && !form.getImage().isEmpty()) {
            activity.setImagePath(imageStorage.save(form.getImage()));
        }

        activityMapper.insertActivity(activity);
    }

    public void updateActivity(Long vendorId, Long activityId, ActivityForm form) {
        validateDates(form);
        Activity existing = getVendorActivity(vendorId, activityId);

        Activity activity = new Activity();
        activity.setActivityId(activityId);
        activity.setVendorId(vendorId);
        applyForm(activity, form);

        if (form.getImage() != null && !form.getImage().isEmpty()) {
            activity.setImagePath(imageStorage.save(form.getImage()));
            imageStorage.delete(existing.getImagePath());
        } else {
            activity.setImagePath(existing.getImagePath());
        }

        activityMapper.updateActivity(activity);
    }

    private void applyForm(Activity activity, ActivityForm form) {
        activity.setTitle(form.getTitle());
        activity.setDescription(form.getDescription());
        activity.setLocation(form.getLocation());
        activity.setPrice(form.getPrice());
        activity.setRegistrationStartDate(form.getRegistrationStartDate());
        activity.setRegistrationEndDate(form.getRegistrationEndDate());
        activity.setActivityStartDate(form.getActivityStartDate());
        activity.setActivityEndDate(form.getActivityEndDate());
        activity.setDailyCapacity(form.getDailyCapacity());
    }

    private void validateDates(ActivityForm form) {
        if (form.getRegistrationStartDate().isAfter(form.getRegistrationEndDate())) {
            throw new IllegalArgumentException("등록 시작일은 종료일보다 늦을 수 없습니다.");
        }
        if (form.getActivityStartDate().isAfter(form.getActivityEndDate())) {
            throw new IllegalArgumentException("활동 시작일은 종료일보다 늦을 수 없습니다.");
        }
    }
}
