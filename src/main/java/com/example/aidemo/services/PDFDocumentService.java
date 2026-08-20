package com.example.aidemo.services;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class PDFDocumentService {
    private List<Document> docPages;

    public List<Document> readPdf(String filePath){

        PagePdfDocumentReader reader = new PagePdfDocumentReader(filePath);
        List<Document> docPages = reader.read();

        TokenTextSplitter splitter = TokenTextSplitter.builder()
                                    .withChunkSize(300)
                                    .withMinChunkSizeChars(50)
                                    .withMinChunkLengthToEmbed(10)
                                    .withMaxNumChunks(5000)
                                    .withKeepSeparator(true)
                                    .withPunctuationMarks(List.of('.', '!', '?', '\n'))
                                    .build();



        return   splitter.apply(docPages);

    }

    public List<Document> readPdfFromMultiPart(MultipartFile file) throws IOException {

        Path tempFile = Files.createTempFile("uploaded",".pdf");
        try{
            Files.copy(
                    file.getInputStream(),
                    tempFile,
                    StandardCopyOption.REPLACE_EXISTING
            );

            PagePdfDocumentReader reader =
                    new PagePdfDocumentReader(
                            tempFile.toUri().toString()
                    );
            List<Document> docPages = reader.read();


            // 4. Add metadata
            for (Document document : docPages) {

                document.getMetadata()
                        .put(
                                "fileName",
                                file.getOriginalFilename()!=null?file.getOriginalFilename():"default"
                        );
            }


            TokenTextSplitter splitter = TokenTextSplitter.builder().build();
//                    .withChunkSize(300)
//                    .withMinChunkSizeChars(50)
//                    .withMinChunkLengthToEmbed(10)
//                    .withMaxNumChunks(5000)
//                    .withKeepSeparator(true)
//                    .withPunctuationMarks(List.of('.', '!', '?', '\n'))
//                    .build();



            return   splitter.apply(docPages);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            Files.deleteIfExists(tempFile);
        }
        return null;
    }
}
