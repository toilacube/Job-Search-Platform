package com.training.lehoang.modules.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.training.lehoang.dto.request.UpdateUserRequest;
import com.training.lehoang.dto.response.UserResponse;
import com.training.lehoang.entity.Role;
import com.training.lehoang.entity.User;
import com.training.lehoang.entity.UsersRole;
import com.training.lehoang.exception.AppException;
import com.training.lehoang.exception.ErrorCode;
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

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;


    public String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public UserResponse getUserInfo(){
        String email = this.getEmail();
        User user = this.findByEmail(email);
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(email)
                .contactInfo(user.getContactInfo())
                .build();
    }

    public UserResponse updateUser(int id, UpdateUserRequest updateUserRequest){
        String userEmail = this.getEmail();
        User user = this.findByEmail(userEmail);

        // Check if the id of from the request is the same from user in the token

        if (id != user.getId()){
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }

        User checkUser = this.findByEmail(updateUserRequest.email());
        // if id == checkUser.getid then user is not updating the email
        // if id != checkUser.getid then user is updating the email but already existed
        if (id != checkUser.getId()){
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

            return "File uploaded successfully: " + link;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "ok";
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
