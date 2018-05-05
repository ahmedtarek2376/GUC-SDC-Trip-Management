package com.sdc.callingapp.tripcarmanagement;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;

@Configuration
public class MongoLabConfiguration {

  public @Bean MongoDbFactory mongoDbFactory() throws MongoException, UnknownHostException {
    return new SimpleMongoDbFactory(new MongoClientURI(System.getenv("MONGODB_URI")));
  }
  public @Bean
    MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
  }
}