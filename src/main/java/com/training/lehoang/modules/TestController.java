package com.training.lehoang.modules;

import com.training.lehoang.modules.rabbitmq.JobProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final JobProducer jobProducer;
    @GetMapping()
    public String test(){
//        jobProducer.sendJobMessage("New job posted: Software Engineer");
        return "Job message sent!";
    }
}


