package com.example.aidemo.services;

import com.example.aidemo.DTO.requests.ChatDTO;
import com.example.aidemo.DTO.requests.UserContext;
import com.example.aidemo.DTO.responses.Response;
import com.example.aidemo.tools.DateTimeTools;
import com.example.aidemo.tools.EmailTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.cache.semantic.SemanticCache;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIService {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final EmailTools emailTools;
    private final DocumentSearch documentSearch;
    private final SemanticCache semanticCache;

    public String buildUserContext(UserContext user) {

        return """
        User context:

        User ID: %s
        Name: %s
        Email: %s
        Timezone: %s
        Preferred language: %s
        Default meeting duration: %s
        attitude: %s

        Use this information when appropriate.
        Do not ask the user for information that is already
        available here unless clarification is necessary.
        """
                .formatted(
                        user.userId(),
                        user.name(),
                        user.email(),
                        user.timezone(),
                        user.preferredLanguage(),
                        user.defaultMeetingDuration(),
                        user.attitude()
                );
    }

    public Flux<String> chatWithAIFlux(ChatDTO request) {


//    return this.chatClient.prompt().user(request.getMessage()).call().content();

//    return this.chatClient.prompt().user(request.getMessage()).call().entity(Response.class).toString();

        return this.chatClient.prompt().advisors(a->a.
                        advisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).param(ChatMemory.CONVERSATION_ID,request.getConversationId())).
                user(u -> u.text(request.getMessage()).
                metadata("messageId", "msg-123").
                metadata("userId", "user-456").
                metadata("priority", "high")).stream().content();

    }

    public String chatWithAIContent(ChatDTO request) {

         String userContext = this.buildUserContext(request.getUser());

//        List<Message> messages =
//                chatMemory.get(request.getConversationId());
//        System.out.println("messages start");
//        messages.forEach(System.out::println);
//        System.out.println("messages ends");
        ChatClientResponse cr = this.chatClient.prompt().
                tools(new DateTimeTools(),emailTools).
                advisors(a->a.
                advisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).
                        param(ChatMemory.CONVERSATION_ID,request.getConversationId())).
                system(userContext).
                user(request.getMessage()).call().chatClientResponse();


        return cr.chatResponse().getResult().getOutput().getText();

//    return this.chatClient.prompt().user(request.getMessage()).call().entity(Response.class).toString();
//        return this.chatClient.prompt().user(request.getMessage()).stream().content();

    }


    public String ask(String question){





        List<Document> docs = this.documentSearch.search(question);

        String context = docs.stream().map(Document::getText).collect(Collectors.joining("\n\n"));

        return chatClient.prompt().system("""
                    You are a helpful assistant.

                    Answer the user's question using only
                    the provided context.

                    If the answer is not present in the context,
                    say that you don't have enough information.
                    """).
                    user("""
                    Context:
                    %s

                    Question:
                    %s
                    """.formatted(context, question)).call().content();

    }

    public String askCache(String question){
        //1.embeds question , 2. do a redis similarity search , 3.checks if similarity > threshold
        var cached = semanticCache.get(question);

        //check if it is hit ( if question is available)
        if(cached.isPresent()){
            System.out.println("Hit from cache");
            return cached.get().getResult().getOutput().getText();
        }

        List<Document> docs = this.documentSearch.search(question);

        String context = docs.stream().map(Document::getText).collect(Collectors.joining("\n\n"));


        var response = chatClient.prompt().system("""
                    You are a helpful assistant.
                    Your name is donna ai.
                    Answer the user's question using only
                    the provided context.

                    If the answer is not present in the context,
                    say that you don't have enough information.
                    """).
                user("""
                    Context:
                    %s

                    Question:
                    %s
                    """.formatted(context, question)).call().chatResponse();


        //store in cache
        semanticCache.set(question, response, Duration.ofDays(1));//since I am gonna use the  for resume static data can have longer times.

        return response.getResult().getOutput().getText();
    }


}
