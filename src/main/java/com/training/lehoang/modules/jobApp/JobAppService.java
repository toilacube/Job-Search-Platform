package com.training.lehoang.modules.jobApp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.training.lehoang.dto.response.JobApplicationResponse;
import com.training.lehoang.entity.Job;
import com.training.lehoang.entity.JobApplication;
import com.training.lehoang.entity.User;
import com.training.lehoang.modules.job.JobRepo;
import com.training.lehoang.modules.user.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobAppService {
    private final JobAppRepo jobAppRepo;
    private final UserService userService;
    private final JobRepo jobRepo;

    public JobApplicationResponse sendApplication(Integer jobId, MultipartFile resume, String coverLetter) {
        Job job = jobRepo.findById(jobId).orElse(null);
        User user = this.userService.getUser();
        int id = user.getId();
        String resumeUrl = this.uploadResume(resume, id);

        JobApplication jobApplication = new JobApplication();
        jobApplication.setUser(user);
        jobApplication.setJob(job);
        jobApplication.setResumeUrl(resumeUrl);
        jobApplication.setCoverLetter(coverLetter);
        jobApplication.setAppliedAt(Instant.now());
        jobAppRepo.save(jobApplication);

        var jobAppRes = JobApplicationResponse.builder()
                .coverLetter(coverLetter)
                .resumeUrl(resumeUrl)
                .build();

        return jobAppRes;
    }

    public String uploadResume(MultipartFile resume, Integer id) {

        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        cloudinary.config.secure = true;
        try {
            // Upload the image
            String random = UUID.randomUUID().toString();
            String public_id = "UserId:" + id + "_" + resume.getName() + random;

            Map params1 = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", true,
                    "overwrite", false,
                    "public_id", public_id
            );

            Map uploadResult = cloudinary.uploader().upload(resume.getBytes(), params1);
            System.out.println(uploadResult);
            return cloudinary.url().generate(public_id + ".pdf");

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
