package com.example.aidemo.config;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.cache.semantic.SemanticCacheAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {
private final SemanticCacheAdvisor semanticCacheAdvisor;

    @Bean
    ChatClient customisedChatClient(ChatClient.Builder builder){

        return builder.defaultSystem("you are an helpful assistant who answers only regarding the insurance.If users ask anything out of this scope.don't make up things ,simply say you don't know").build();


    }
    @Primary
    @Bean
    ChatClient defaultChatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    //if we make this as primary ,only ask method will be able to cache without anything written explicitly
    @Bean
    ChatClient chatClientWithCacheConfigured(ChatClient.Builder builder) {

        return builder.defaultAdvisors(semanticCacheAdvisor).build();
    }

    @Bean
    ChatMemory chatMemory (){

        //this will use the in Memory
        //we can update to the databsae as well.
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
        return chatMemory;
    }



}
