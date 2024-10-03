package com.training.lehoang.modules.jobSubscription;

import com.training.lehoang.entity.Job;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

@Data
public class JobToSubscriber implements Serializable {

    private Integer id;

    private Integer recruiterId; // Store only the ID to simplify serialization

    private Instant expiryDate;

    private Integer companyId; // Store only the ID to simplify serialization

    private Integer locationId; // Store only the ID to simplify serialization

    private ArrayList<Integer> jobTags;

    public JobToSubscriber(Job job) {
        if (job != null) {
            this.id = job.getId();
            this.recruiterId = job.getRecruiter() != null ? job.getRecruiter().getId() : null;
            this.expiryDate = job.getExpiryDate();
            this.companyId = job.getCompany() != null ? job.getCompany().getId() : null;
            this.locationId = job.getLocation() != null ? job.getProvince().getId() : null;
            this.jobTags = new ArrayList<>(job.getJobTags());
        }
    }
    // Default constructor
    public JobToSubscriber() {
        // This constructor is necessary for Jackson to deserialize
    }

}
