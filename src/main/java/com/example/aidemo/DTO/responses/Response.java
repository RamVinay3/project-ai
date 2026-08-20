package com.example.aidemo.DTO.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
@Getter
@Setter
public class Response {
    private String error;

    Map<String, List<String>> products;
}
