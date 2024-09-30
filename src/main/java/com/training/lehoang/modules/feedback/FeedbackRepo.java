package com.training.lehoang.modules.feedback;

import com.training.lehoang.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepo extends JpaRepository<Feedback, Integer> {

    Feedback findFirstByApplicationId(Integer feedbackId);
}
