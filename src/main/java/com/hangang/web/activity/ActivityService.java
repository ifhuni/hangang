package com.hangang.web.activity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    private final ActivityMapper activityMapper;
    private final ActivityContentBlockMapper contentBlockMapper;
    private final ActivityImageStorage imageStorage;

    public ActivityService(ActivityMapper activityMapper, ActivityContentBlockMapper contentBlockMapper,
                            ActivityImageStorage imageStorage) {
        this.activityMapper = activityMapper;
        this.contentBlockMapper = contentBlockMapper;
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

    public List<ActivityContentBlock> getContentBlocks(Long activityId) {
        return contentBlockMapper.findByActivityId(activityId);
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
        saveContentBlocks(activity.getActivityId(), form.getContentBlocks());
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

        contentBlockMapper.deleteByActivityId(activityId);
        saveContentBlocks(activityId, form.getContentBlocks());
    }

    private void saveContentBlocks(Long activityId, List<ContentBlockForm> blockForms) {
        if (blockForms == null) {
            return;
        }

        int position = 0;
        for (ContentBlockForm blockForm : blockForms) {
            if (blockForm.getType() == null) {
                continue;
            }

            ActivityContentBlock block = new ActivityContentBlock();
            block.setActivityId(activityId);
            block.setBlockType(blockForm.getType());

            if ("TEXT".equals(blockForm.getType())) {
                if (blockForm.isRemoved() || blockForm.getText() == null || blockForm.getText().isBlank()) {
                    continue;
                }
                block.setTextContent(blockForm.getText());
            } else if ("IMAGE".equals(blockForm.getType())) {
                if (blockForm.isRemoved()) {
                    imageStorage.delete(blockForm.getExistingImagePath());
                    continue;
                }
                if (blockForm.getImage() != null && !blockForm.getImage().isEmpty()) {
                    block.setImagePath(imageStorage.save(blockForm.getImage()));
                    imageStorage.delete(blockForm.getExistingImagePath());
                } else if (blockForm.getExistingImagePath() != null && !blockForm.getExistingImagePath().isBlank()) {
                    block.setImagePath(blockForm.getExistingImagePath());
                } else {
                    continue;
                }
            } else {
                continue;
            }

            block.setPosition(position++);
            contentBlockMapper.insertBlock(block);
        }
    }

    private void applyForm(Activity activity, ActivityForm form) {
        activity.setTitle(form.getTitle());
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
