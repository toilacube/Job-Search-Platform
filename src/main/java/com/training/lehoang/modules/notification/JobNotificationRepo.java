package com.training.lehoang.modules.notification;

import com.training.lehoang.entity.JobNotification;

import java.util.List;
import java.util.Optional;

import com.training.lehoang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;

public interface JobNotificationRepo extends JpaRepository<JobNotification, Integer> {
    List<JobNotification> findAll();
    Optional<JobNotification> findByUser(User user);
}
