package com.training.lehoang.modules.notification;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.User;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.training.lehoang.entity.SavedJob;
import com.training.lehoang.modules.job.SavedJobRepo;
import com.training.lehoang.modules.mail.MailService;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ScheduledNoti {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    // private static final long RATE_IN_MINUTES = TimeUnit.MINUTES.toMillis(5);
    // private static final long RATE_IN_HOURS = TimeUnit.HOURS.toMillis(8);

    private final MailService mailService;
    private final SavedJobRepo savedJobRepo;

//    @Scheduled(fixedRate = 5000) // 5 minute
//    public void reportCurrentTime() {
//        System.out.println(("The time is now {}" + dateFormat.format(new Date())));
//    }

    @Scheduled(fixedRate = 15000 * 100000)
    @Transactional
    public void sendSavedJobReminder() {
        System.out.println("Start to send saved job reminder");
        ArrayList<SavedJob> savedJobs = savedJobRepo.findAllByIsAppliedIsFalse();
        savedJobs.forEach(savedJob -> {
            // CompletableFuture.runAsync(() ->{
            User user = savedJob.getUser();
            Job job = savedJob.getJob();
            mailService.sendSavedJobReminder(job, user);
            // });
        });
        System.out.println("End to send saved job reminder");
    }
}
