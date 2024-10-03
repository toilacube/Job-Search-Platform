package com.training.lehoang.modules.notification;

import com.training.lehoang.entity.JobNotification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobNotificationRepo extends JpaRepository<JobNotification, Integer> {
    List<JobNotification> findAll();
}
