package com.training.lehoang.modules.feedback;

import com.training.lehoang.entity.Feedback;
import com.training.lehoang.entity.JobApplication;
import com.training.lehoang.entity.User;
import com.training.lehoang.exception.AppException;
import com.training.lehoang.exception.ErrorCode;
import com.training.lehoang.modules.job.JobRepo;
import com.training.lehoang.modules.jobApp.JobAppRepo;
import com.training.lehoang.modules.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepo feedbackRepo;
    private final UserService userService;
    private final JobAppRepo jobAppRepo;
    private final JobRepo jobRepo;
    public Feedback save(Feedback feedback) {
        User user = this.userService.getUser();
        // check if the application belongs to this recruiter posted job
        JobApplication jobApplication = this.jobAppRepo.findFirstByJobId(feedback.getApplicationId())
                .orElseThrow(() -> new AppException(ErrorCode.APPLICATION_NOT_FOUND));
        if (!jobApplication.getJob().getRecruiter().equals(user)) throw new AppException(ErrorCode.UNAUTHORIZED);
        return feedbackRepo.save(feedback);
    }
}
