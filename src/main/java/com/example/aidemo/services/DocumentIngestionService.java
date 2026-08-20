package com.example.aidemo.services;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentIngestionService {

private final VectorStore vectorStore;
private final PDFDocumentService pdfDocumentService;


public void ingest(MultipartFile file){

    try{
        vectorStore.add(pdfDocumentService.readPdfFromMultiPart(file));
    }
    catch (Exception e ){
        e.printStackTrace();
    }


}



}
