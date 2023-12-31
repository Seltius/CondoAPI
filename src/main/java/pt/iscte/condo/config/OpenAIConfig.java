package pt.iscte.condo.config;

import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Value("${openai.token}")
    private String apiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + apiKey);
            requestTemplate.header("OpenAI-Beta", "assistants=v1");
        };
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(30000, 30000, 3);
    }

}
