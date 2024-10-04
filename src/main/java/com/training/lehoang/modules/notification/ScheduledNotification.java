package com.training.lehoang.modules.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.training.lehoang.entity.*;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.training.lehoang.modules.job.JobRepo;
import com.training.lehoang.modules.job.SavedJobRepo;
import com.training.lehoang.modules.mail.MailService;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ScheduledNotification {

    private final MailService mailService;
    private final SavedJobRepo savedJobRepo;
    private final JobNotificationRepo jobNotificationRepo;
    private final JobRepo jobRepo;

    @Scheduled(fixedRate = 300 * 5000)
    @Profile("test")
    @Transactional
    public void runForTest() {
        this.runScheduledJobReminder();
    }
    @Scheduled(cron = "0 0 8 * * *")
    @Scheduled(cron = "0 0 17 * * *")
    @Profile("prod")
    @Transactional
    public void runForProd() {
        this.runScheduledJobReminder();
    }

    // Job Subscription Schedules
    @Scheduled(fixedRate = 300 * 5000)
    @Profile("test")
    @Transactional
    public void runSubscriptionForTest() {
        this.runDailyJobSubscription();
    }

    @Scheduled(cron = "0 0 12 * * *")
    @Profile("prod")
    @Transactional
    public void runSubscriptionForProd() {
        this.runDailyJobSubscription();
    }

    @Transactional
    public void runScheduledJobReminder() {

        System.out.println("Start to send saved job reminder");
        ArrayList<SavedJob> savedJobs = savedJobRepo.findAllByIsAppliedIsFalse();
        savedJobs.forEach(savedJob -> {
                User user = savedJob.getUser();
                Job job = savedJob.getJob();
                mailService.sendSavedJobReminder(job, user);
        });
        System.out.println("End to send saved job reminder");
    }

    @Transactional
    public void runDailyJobSubscription() {

        List<JobNotification> jobNotifications = jobNotificationRepo.findAll();

        jobNotifications.forEach(jobNotification -> {
                User user = jobNotification.getUser();
                ArrayList<Job> jobs =this.jobRepo.findAllByIsDeletedIsFalseAndIdIn(jobNotification.getJobIds());
                System.out.println(user.getEmail());
                jobs.forEach(job -> {
                    System.out.println(job.getJobTitle());
                });
                mailService.sendDailyJobSubscription(user, jobs);
        });


    }
}
