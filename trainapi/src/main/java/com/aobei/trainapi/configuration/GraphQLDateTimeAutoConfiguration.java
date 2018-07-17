package com.aobei.trainapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aobei.trainapi.schema.datetime.GraphQLDate;
import com.aobei.trainapi.schema.datetime.GraphQLLocalDate;
import com.aobei.trainapi.schema.datetime.GraphQLLocalDateTime;
import com.aobei.trainapi.schema.datetime.GraphQLLocalTime;

/**
 * 定义 graphql scalar 类型
 * @author 30903
 *
 */
@Configuration
public class GraphQLDateTimeAutoConfiguration {

    @Bean
    public GraphQLDate graphQLDate() {
        return new GraphQLDate();
    }

    @Bean
    public GraphQLLocalDate graphQLLocalDate() {
        return new GraphQLLocalDate();
    }

    @Bean
    public GraphQLLocalDateTime graphQLLocalDateTime() {
        return new GraphQLLocalDateTime();
    }

    @Bean
    public GraphQLLocalTime graphQLLocalTime() {
        return new GraphQLLocalTime();
    }

}
