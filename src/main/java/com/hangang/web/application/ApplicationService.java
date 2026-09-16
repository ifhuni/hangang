package com.hangang.web.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hangang.web.activity.Activity;
import com.hangang.web.activity.ActivityService;

@Service
public class ApplicationService {

    private final ApplicationMapper applicationMapper;
    private final ActivityService activityService;

    public ApplicationService(ApplicationMapper applicationMapper, ActivityService activityService) {
        this.applicationMapper = applicationMapper;
        this.activityService = activityService;
    }

    public String submitApplication(Long activityId, ApplicationForm form) {
        Activity activity = activityService.getActivity(activityId);
        validate(activity, form.getParticipationDate());

        Application application = new Application();
        application.setActivityId(activityId);
        application.setApplicantName(form.getApplicantName());
        application.setPhone(form.getPhone());
        application.setEmail(form.getEmail());
        application.setGender(form.getGender());
        application.setParticipationDate(form.getParticipationDate());
        application.setToken(UUID.randomUUID().toString());

        applicationMapper.insertApplication(application);
        return application.getToken();
    }

    public Map<LocalDate, Integer> getApprovedCounts(Long activityId) {
        return applicationMapper.findByActivityId(activityId).stream()
                .filter(a -> "APPROVED".equals(a.getStatus()))
                .collect(Collectors.groupingBy(Application::getParticipationDate, Collectors.summingInt(a -> 1)));
    }

    public Application getByToken(String token) {
        Application application = applicationMapper.findByToken(token);
        if (application == null) {
            throw new IllegalArgumentException("존재하지 않는 신청 내역입니다.");
        }
        return application;
    }

    public void cancel(String token) {
        Application application = getByToken(token);
        if ("CANCELLED".equals(application.getStatus())) {
            throw new IllegalStateException("이미 취소된 신청입니다.");
        }
        applicationMapper.cancel(token);
    }

    public List<Application> listByActivityForVendor(Long vendorId, Long activityId) {
        activityService.getVendorActivity(vendorId, activityId);
        return applicationMapper.findByActivityId(activityId);
    }

    public List<ParticipantDateSummary> summarizeByDate(Activity activity, List<Application> applications) {
        Map<LocalDate, List<Application>> byDate = applications.stream()
                .collect(Collectors.groupingBy(Application::getParticipationDate));

        return byDate.entrySet().stream()
                .map(entry -> {
                    long approved = entry.getValue().stream().filter(a -> "APPROVED".equals(a.getStatus())).count();
                    long pending = entry.getValue().stream().filter(a -> "PENDING".equals(a.getStatus())).count();
                    return new ParticipantDateSummary(entry.getKey(), activity.getDailyCapacity(), (int) approved, (int) pending);
                })
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .toList();
    }

    public void approveForVendor(Long vendorId, Long activityId, Long applicationId) {
        Activity activity = activityService.getVendorActivity(vendorId, activityId);

        Application application = applicationMapper.findById(applicationId);
        if (application == null || !application.getActivityId().equals(activityId)) {
            throw new IllegalArgumentException("존재하지 않는 신청입니다.");
        }
        if (!"PENDING".equals(application.getStatus())) {
            throw new IllegalStateException("대기 중인 신청만 승인할 수 있습니다.");
        }

        long approvedCount = applicationMapper.findByActivityId(activityId).stream()
                .filter(a -> a.getParticipationDate().equals(application.getParticipationDate()))
                .filter(a -> "APPROVED".equals(a.getStatus()))
                .count();
        if (approvedCount >= activity.getDailyCapacity()) {
            throw new IllegalStateException("해당 날짜는 이미 정원이 찼습니다.");
        }

        applicationMapper.approve(applicationId);
    }

    private void validate(Activity activity, LocalDate participationDate) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(activity.getRegistrationStartDate()) || today.isAfter(activity.getRegistrationEndDate())) {
            throw new IllegalStateException("신청 가능 기간이 아닙니다.");
        }
        if (participationDate.isBefore(activity.getActivityStartDate()) || participationDate.isAfter(activity.getActivityEndDate())) {
            throw new IllegalArgumentException("참여 희망 날짜가 활동 기간을 벗어났습니다.");
        }
    }
}
