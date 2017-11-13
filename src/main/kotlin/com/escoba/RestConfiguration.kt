package com.escoba

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate

@Configuration
class RestConfiguration {
    @Bean
    fun restOperations(): RestOperations {
        val rest = RestTemplate()
        //this is crucial!
        rest.messageConverters.add(0, mappingJacksonHttpMessageConverter())
        return rest
    }

    @Bean
    fun mappingJacksonHttpMessageConverter(): MappingJackson2HttpMessageConverter {
        val mappingJackson2HttpMessageConverter = MappingJackson2HttpMessageConverter()
        mappingJackson2HttpMessageConverter.objectMapper = objectMapper()
        return mappingJackson2HttpMessageConverter
    }

    @Bean
    fun objectMapper() = ObjectMapper().registerKotlinModule()
}
