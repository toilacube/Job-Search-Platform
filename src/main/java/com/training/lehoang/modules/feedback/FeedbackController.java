package com.training.lehoang.modules.feedback;

import com.training.lehoang.dto.request.FeedbackRequest;
import com.training.lehoang.dto.response.ApiResponse;
import com.training.lehoang.entity.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;


    @PostMapping("{applicationId}")
    public ApiResponse<Feedback> sendFeedback(@PathVariable Integer applicationId, @RequestBody FeedbackRequest feedbackRequest) {

        Feedback feedback = new Feedback();
        feedback.setApplicationId(applicationId);
        feedback.setComment(feedbackRequest.getFeedback());
        Feedback newFeedback = this.feedbackService.save(feedback);
        return ApiResponse.<Feedback>builder().data(newFeedback).build();
    }
}
