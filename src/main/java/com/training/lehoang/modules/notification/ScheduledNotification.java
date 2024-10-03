package com.training.lehoang.modules.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.training.lehoang.entity.*;
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

    // @Scheduled(fixedRate = 5000) // 5 second
    // public void reportCurrentTime() {
    // System.out.println(("The time is now {}" + dateFormat.format(new Date())));
    // }

    // Job reminder at 8am and 5pm everyday
    @Scheduled(cron = "0 0 8 * * *")
    @Scheduled(cron = "0 0 17 * * *")
    @Transactional
    public void sendSavedJobReminder() {

        System.out.println("Start to send saved job reminder");
        ArrayList<SavedJob> savedJobs = savedJobRepo.findAllByIsAppliedIsFalse();
        savedJobs.forEach(savedJob -> {
//            CompletableFuture.runAsync(() -> {
                User user = savedJob.getUser();
                Job job = savedJob.getJob();
                mailService.sendSavedJobReminder(job, user);
//            });
        });
        System.out.println("End to send saved job reminder");
    }

    // Daily emails with jobs that match user subscription
//    @Scheduled(fixedRate = 15000) // 15 second
    @Scheduled(cron = "0 0 12 * * *")
    @Transactional
    public void sendDailyJobSubscription() {

        List<JobNotification> jobNotifications = jobNotificationRepo.findAll();

        jobNotifications.forEach(jobNotification -> {
//            CompletableFuture.runAsync(() -> {
                User user = jobNotification.getUser();
                ArrayList<Job> jobs =this.jobRepo.findAllByIsDeletedIsFalseAndIdIn(jobNotification.getJobIds());
                System.out.println(user.getEmail());
                jobs.forEach(job -> {
                    System.out.println(job.getJobTitle());
                });
                mailService.sendDailyJobSubscription(user, jobs);
//            });
        });


    }
}
