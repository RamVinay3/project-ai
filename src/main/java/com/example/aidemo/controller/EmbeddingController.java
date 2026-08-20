package com.example.aidemo.controller;


import com.example.aidemo.services.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/embed")
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    @PostMapping("/text")
    public float[] embedText(@RequestBody  String text){
        System.out.println("Hi "+ text);
        return embeddingService.generateEmbedding(text);
    }



}
