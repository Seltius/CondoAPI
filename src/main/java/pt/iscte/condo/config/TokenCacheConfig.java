package pt.iscte.condo.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.iscte.condo.proxy.request.AuthTokenRequest;
import pt.iscte.condo.proxy.ZoomAPI;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class TokenCacheConfig {

    private final ZoomAPI zoomAPI;
    private final String clientId = "88Pd4LPHQiuSJ18FeeT6tA"; //todo remove hardcoded values
    private final String clientSecret = "K2M7oOpaBcg2CmJUTNTd2l0PzQtDfYmf"; //todo remove hardcoded values
    private final String accountId = "03ab2nd9QEGkf0A390ks4A"; //todo remove hardcoded values

    @Bean
    public LoadingCache<String, String> tokenCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS) // zoom token expires after 1 hour
                .build(new CacheLoader<>() {
                    @Override
                    public @Nullable String load(String key) {
                        return getNewToken();
                    }
                });
    }

    public void setToken(String key, String token, LoadingCache<String, String> cache) {
        cache.put(key, token);
    }

    public String getToken(String key, LoadingCache<String, String> cache) {
        return cache.get(key);
    }

    private String getNewToken() {
        String credentials = clientId + ":" + clientSecret;
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        AuthTokenRequest request = AuthTokenRequest.builder()
                .grant_type("account_credentials")
                .account_id(accountId)
                .build();

        Map<String, Object> response = zoomAPI.getAccessToken("Basic " + base64Credentials, request);
        return (String) response.get("access_token");
    }

}
