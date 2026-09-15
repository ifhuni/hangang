package com.hangang.web.application;

import java.time.LocalDate;
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

    public Map<LocalDate, Integer> getAppliedCounts(Long activityId) {
        return applicationMapper.countByActivity(activityId).stream()
                .collect(Collectors.toMap(ParticipationCount::getParticipationDate, ParticipationCount::getCount));
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
