package com.training.lehoang.modules.rabbitmq;
import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.JobNotification;
import com.training.lehoang.entity.JobSubscription;
import com.training.lehoang.modules.jobSubscription.JobSubscriptionRepo;
import com.training.lehoang.modules.jobSubscription.JobToSubscriber;
import com.training.lehoang.modules.mail.MailService;
import com.training.lehoang.modules.mail.UserAndJob;
import com.training.lehoang.modules.notification.JobNotificationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobConsumer {
    private final MailService mailService;
    private final JobSubscriptionRepo jobSubscriptionRepo;
    private final JobNotificationRepo jobNotificationRepo;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(UserAndJob u)  {
        if(u == null)
            return;
        this.mailService.sendEmailWithTemplating(u);
    }
/*
* table job notification:
(email service will base on this to send email notification)
- userID
- JobID
- createdAt (limit 10 latest jobs only cuz im lazy)

How to decide if newly created job will be added to job subscription: newJob: jobId: [1, 1, 2]
- For each user:
	- Query jobSubscriptions table
	- get the object:
		- userId: int id
		- localId: array int id
		- job tag id: array int id
		- companyId: array int id
	- if newJob.compId in ... or compId[].len == 0
	- if one of newJob.jobTagId is in jobtagId or jobtagId.len == 0
	- if one of locaId is in locaId or locaId.len == 0
		then:
			add job to job-subscription table
* */
    @RabbitListener(queues = RabbitMQConfig.JOB_NOTIFICATION_QUEUE_NAME)
    public void addJobToSubscriber(JobToSubscriber job) {

        System.out.println("Start to check if this job can be added to job notification for user");

        ArrayList<JobSubscription> jobSubscriptions = this.jobSubscriptionRepo.findAll();


        jobSubscriptions.forEach(jobSubscription -> {
            boolean isCompanyMatch = jobSubscription.getCompanyIds().contains(job.getCompanyId())
                    || jobSubscription.getCompanyIds().isEmpty();

            boolean hasMatchingJobTags = job.getJobTags().stream()
                    .anyMatch(jobTag -> jobSubscription.getJobTagIds().contains(jobTag))
                    || jobSubscription.getJobTagIds().isEmpty();

            boolean isLocationMatch = jobSubscription.getCompanyIds().contains(job.getCompanyId())
                    || jobSubscription.getCompanyIds().isEmpty();

            System.out.println(isCompanyMatch + " " + hasMatchingJobTags + " " + isLocationMatch);

            if (isCompanyMatch && hasMatchingJobTags && isLocationMatch) {
                // add this job to this user job notification
                JobNotification jobNotification = this.jobNotificationRepo.findByUser(jobSubscription.getUser()).orElse(null);
                if (jobNotification != null) {
                    jobNotification.getJobIds().add(job.getId());
                    this.jobNotificationRepo.save(jobNotification);
                }
                else {
                    // create new JobApplication
                    JobNotification newJobNotification = new JobNotification();
                    newJobNotification.setUser(jobSubscription.getUser());
                    ArrayList<Integer> jobIds = new ArrayList<>();
                    jobIds.add(job.getId());
                    newJobNotification.setJobIds(jobIds);
                    this.jobNotificationRepo.save(newJobNotification);
                }

            }
        });
    }
}