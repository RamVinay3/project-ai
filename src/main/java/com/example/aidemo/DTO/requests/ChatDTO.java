package com.example.aidemo.DTO.requests;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class ChatDTO {

    private final  String message;
    private String conversationId;
    private UserContext user;
    private String question;
}
