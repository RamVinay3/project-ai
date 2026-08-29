package com.example.aidemo.config;

import org.springframework.ai.chat.cache.semantic.SemanticCache;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.redis.cache.semantic.DefaultSemanticCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.RedisClient;

@Configuration
public class RedisSemanticCacheConfig {

    @Bean
    public RedisClient redisClient(
            @Value("${spring.data.redis.host}") String host,
            @Value("${spring.data.redis.port}") int port,
            @Value("${spring.data.redis.username}") String username,
            @Value("${spring.data.redis.password}") String password) {

        DefaultJedisClientConfig config = DefaultJedisClientConfig.builder().
                                            user(username).
                                            password(password).
                                            build();

        return RedisClient.builder()
                .hostAndPort(host, port).
                clientConfig(config)
                .build();
    }

    @Bean
    public SemanticCache semanticCache(
            RedisClient redisClient,
            EmbeddingModel embeddingModel,
            @Value("${spring.ai.vectorstore.redis.semantic-cache.similarity-threshold}")
            double similarityThreshold) {

        return DefaultSemanticCache.builder()
                .jedisClient(redisClient)
                .embeddingModel(embeddingModel)
                .similarityThreshold(similarityThreshold)
                .indexName("qa-semantic-cache")
                .prefix("qa-cache:")
                .build();
    }
}