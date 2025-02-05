package com.training.lehoang.modules.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.training.lehoang.dto.request.SkillRequest;
import com.training.lehoang.dto.request.UpdateUserRequest;
import com.training.lehoang.dto.response.*;
import com.training.lehoang.entity.*;
import com.training.lehoang.exception.AppException;
import com.training.lehoang.exception.ErrorCode;
import com.training.lehoang.modules.feedback.FeedbackRepo;
import com.training.lehoang.modules.job.JobMapper;
import com.training.lehoang.modules.job.JobRepo;
import com.training.lehoang.modules.jobApp.JobAppRepo;
import com.training.lehoang.modules.skill.SkillRepo;
import com.training.lehoang.modules.skill.UserSkillRepo;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;
    private final SkillRepo skillRepo;
    private final UserSkillRepo userSkillRepo;
    private final JobRepo jobRepo;
    private final JobMapper jobMapper;
    private final JobAppRepo jobAppRepo;
    private final FeedbackRepo feedbackRepo;


    public String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getUser(){
        String email = this.getEmail();
        return this.findByEmail(email);
    }

    public UserResponse updateUser(int id, UpdateUserRequest updateUserRequest){
        String userEmail = this.getEmail();
        System.out.println(userEmail);

        User user = this.findByEmail(userEmail);

        // Check if the id of from the request is the same from user in the token

        if (id != user.getId()){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Boolean checkUser = userRepo.existsByEmail(updateUserRequest.email());
        // if id == checkUser.getid then user is not updating the email
        // if id != checkUser.getid then user is updating the email but already existed
        if (checkUser){
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        user.setContactInfo(updateUserRequest.contactInfo());
        user.setName(updateUserRequest.name());
        user.setEmail(updateUserRequest.email());

        User updatedUser = this.userRepo.save(user);
        return new UserResponse(updatedUser);
    }

    public User findByEmail(String email){
        return this.userRepo.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public User findById(int id){
        return this.userRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public String uploadResume(MultipartFile file){
        User user = this.findByEmail(getEmail());
        int id = user.getId();
        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        cloudinary.config.secure = true;
        try {
        // Upload the image
            String random = UUID.randomUUID().toString();
            String public_id = "UserId:"  + id + "_" + file.getName() + random;

            Map params1 = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", true,
                    "overwrite", false,
                    "public_id", public_id
            );

            Map  uploadResult = cloudinary.uploader().upload(file.getBytes(), params1);
            String link = cloudinary.url().generate(public_id + ".pdf");

            user.setResumeUrl(link);
            this.userRepo.save(user);

            return link;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "ok";
    }


    public ArrayList<SkillResponse> addSkill(SkillRequest skillRequest){

        ArrayList<SkillResponse> skillResponses = new ArrayList<>();

        User user = this.findByEmail(getEmail());
        ArrayList<Skill> newSkills = this.skillRepo.findByIdIn(skillRequest.getSkills());

        if (newSkills.size() != skillRequest.getSkills().size()){
            throw new AppException(ErrorCode.SKILL_NOT_EXISTED);
        }
        
        // check if there are any duplicate skills and then
         // add new skills to the user using userskill
        ArrayList<UsersSkill> userSkills = this.userSkillRepo.findAllByUser(user);
        ArrayList<Skill> currentSkills = userSkills.stream()
                                    .map(UsersSkill::getSkill)
                                    .collect(Collectors.toCollection(ArrayList::new));
        
        for (Skill skill : newSkills){
            if (!currentSkills.contains(skill)){
                UsersSkill usersSkill = new UsersSkill();
                usersSkill.setUser(user);
                usersSkill.setSkill(skill);
                userSkills.add(usersSkill);
                this.userSkillRepo.save(usersSkill);

                SkillResponse skillResponse = SkillResponse.builder()
                        .id(skill.getId())
                        .name(skill.getName())
                        .build();

                skillResponses.add(skillResponse);
            }
        }

        return skillResponses;

    }


    public ArrayList<SkillResponse> getSkills(){
        User user = this.findByEmail(getEmail());
        ArrayList<UsersSkill> userSkills = this.userSkillRepo.findAllByUser(user);
        ArrayList<SkillResponse> skillResponses = new ArrayList<>();

        userSkills.forEach(usersSkill -> {
            Skill skill = usersSkill.getSkill();
            SkillResponse skillResponse = SkillResponse.builder()
                    .id(skill.getId())
                    .name(skill.getName())
                    .build();
            skillResponses.add(skillResponse);
        });

        return skillResponses;
    }

    public  ArrayList<JobResponse> getJobRecommendation(){
        ArrayList<SkillResponse> skills = this.getSkills();
        String skills_string = skills.stream()
                .map(SkillResponse::getName)
                .collect(Collectors.joining(" | "));
        ArrayList<Job> jobs = this.jobRepo.findJobsBySkills(skills_string);
        ArrayList<JobResponse> jobResponses = new ArrayList<>();
        jobs.forEach(job -> {
            jobResponses.add(jobMapper.toJobResponse(job));
        });
        return jobResponses;
    }

    public ArrayList<UserFeedbackResponse> getApplicationFeedback(int userid){
        ArrayList<JobApplication> jobApplications = this.jobAppRepo.findByUserId(userid);
        if (jobApplications.isEmpty()){
            throw new AppException(ErrorCode.NO_APPLICATION_FOUND);
        }

        ArrayList<UserFeedbackResponse> userFeedbackResponses = new ArrayList<>();
        for (JobApplication jobApplication : jobApplications){

            Feedback fb = this.feedbackRepo.findFirstByApplicationId(jobApplication.getId());
            JobApplicationResponse jobAppRes = JobApplicationResponse.builder()
                    .id(jobApplication.getId())
                    .coverLetter(jobApplication.getCoverLetter())
                    .resumeUrl(jobApplication.getResumeUrl())
                    .build();


            UserFeedbackResponse temp = UserFeedbackResponse.builder()
                    .jobApplication(jobAppRes)
                    .feedback(fb)
                    .build();
            userFeedbackResponses.add(temp);
        }
        return userFeedbackResponses;
    }

    public void set2FA(int isEnable){
        User user = this.findByEmail(getEmail());
        user.setIs2FA(isEnable == 1);
        this.userRepo.save(user);
    }


    public String test(){
        User user = this.findByEmail(getEmail());
        System.out.println(user.getUsersRoles());
        Set<UsersRole> usersRole = user.getUsersRoles();
        ArrayList<String> roles = new ArrayList<>();
        usersRole.forEach(usersRole1 -> {
            roles.add(usersRole1.getRole().getName());

        });
        return roles.toString();
    }
}
