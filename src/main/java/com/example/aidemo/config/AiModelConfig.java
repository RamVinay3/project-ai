package com.example.aidemo.config;

import org.springframework.ai.chat.cache.semantic.SemanticCache;
import org.springframework.ai.chat.cache.semantic.SemanticCacheAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class AiModelConfig {

    @Bean
    @Primary
    @Profile("local")
    public ChatModel ollamaPrimaryChatModel(OllamaChatModel ollamaChatModel) {
        return ollamaChatModel;
    }

    @Bean
    @Primary
    @Profile("prod")
    public ChatModel geminiPrimaryChatModel(GoogleGenAiChatModel googleGenAiChatModel) {
        return googleGenAiChatModel;
    }


    @Bean
    @Primary
    @Profile("prod")
    public EmbeddingModel geminiEmbeddingModel(GoogleGenAiTextEmbeddingModel embedModel){
        return embedModel;
    }

    @Bean
    @Primary
    @Profile("local")
    public EmbeddingModel ollamaEmbeddingModel(OllamaEmbeddingModel ollamaEmbeddingModel){
        return ollamaEmbeddingModel;
    }


    @Bean
    public SemanticCacheAdvisor redisCacheAdvisor(SemanticCache semanticCache){
        return SemanticCacheAdvisor.builder().cache(semanticCache).build();
    }




}
