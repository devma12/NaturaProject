package com.natura.web.server.configuration;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.httpclient5.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient5.LogbookHttpResponseInterceptor;

@Configuration
public class LogbookConfiguration {

    @Bean
    public Logbook logbook() {
        return Logbook.create();
    }
}
