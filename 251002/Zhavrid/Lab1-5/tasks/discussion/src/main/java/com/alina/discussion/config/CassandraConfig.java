package com.alina.discussion.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraConfig {
    public @Bean CqlSession session() {

        return CqlSession.builder().withKeyspace("distcomp").build();
    }
}
