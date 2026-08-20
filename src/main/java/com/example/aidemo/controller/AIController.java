package com.example.aidemo.controller;

import com.example.aidemo.DTO.requests.ChatDTO;
import com.example.aidemo.services.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class AIController {

private final AIService aiservice;

    @GetMapping("/test")
    public String testThis(){
        return "hi this is working";
    }

    @PostMapping(value="/testString",consumes = "text/plain")
    public String test(@RequestBody String param){
        return param;
    }

    @PostMapping(value = "/chat")
    public String chat(@RequestBody ChatDTO req){

        return this.aiservice.chatWithAIContent(req);
    }


    @PostMapping(value = "/chatForFlux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)

    public Flux<String> chatForFlux(@RequestBody ChatDTO req){

        return this.aiservice.chatWithAIFlux(req);
    }


    @PostMapping("/ask")
    public String ask(@RequestBody ChatDTO req ){
        return this.aiservice.ask(req.getQuestion());
    }

}
