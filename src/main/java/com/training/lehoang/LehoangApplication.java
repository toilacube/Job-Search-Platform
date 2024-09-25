package com.training.lehoang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.Map;
@SpringBootApplication
public class LehoangApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(LehoangApplication.class, args);

        // Set your Cloudinary credentials

        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        cloudinary.config.secure = true;
        System.out.println(cloudinary.config.cloudName);
//
//
//// Upload the image
//        Map params1 = ObjectUtils.asMap(
//                "use_filename", true,
//                "unique_filename", false,
//                "overwrite", true
//        );
//
//        System.out.println(
//                cloudinary.uploader().upload("https://cloudinary-devs.github.io/cld-docs-assets/assets/images/coffee_cup.jpg", params1));
    }

}
