package com.training.lehoang.modules.jobSubscription;

import com.training.lehoang.entity.JobSubscription;
import com.training.lehoang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobSubscriptionRepo extends JpaRepository<JobSubscription, Integer> {
    Optional<JobSubscription> findByUser(User user);
}
