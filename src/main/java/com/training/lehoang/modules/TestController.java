package com.training.lehoang.modules;

import com.training.lehoang.modules.rabbitmq.JobProducer;
import com.training.lehoang.modules.role.UsersRolesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final UsersRolesRepo usersRolesRepo;
    @GetMapping()
    public String test(){
        System.out.println(usersRolesRepo.findAll());
        return "Job message sent!";
    }
}


