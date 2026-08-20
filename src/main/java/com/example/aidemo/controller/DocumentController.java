package com.example.aidemo.controller;

import com.example.aidemo.services.DocumentIngestionService;
import com.example.aidemo.services.DocumentSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentIngestionService documentIngestionService;
    private final DocumentSearch docSearch;

    @PostMapping(value="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file){

        documentIngestionService.ingest(file);
        return ResponseEntity.ok(
                "PDF uploaded and indexed successfully"
        );
    }

    @PostMapping("/search")
    public List<String> search(@RequestBody String question){

        return docSearch.search(question).stream().map(Document::getText).toList();


    }
}
