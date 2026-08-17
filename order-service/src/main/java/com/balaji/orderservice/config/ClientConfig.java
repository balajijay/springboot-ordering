package com.balaji.orderservice.config;

import com.balaji.orderservice.client.ProductClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import java.time.Duration;

@Configuration
public class ClientConfig {

	@Bean
	ProductClient productClient() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout((int) Duration.ofSeconds(2).toMillis());
		factory.setReadTimeout((int) Duration.ofSeconds(3).toMillis());
		RestClient restClient = RestClient.builder()
				.baseUrl("http://localhost:8085")
				.requestFactory(factory)
				// 💡 CRITICAL: Intercept the call and inject the incoming Bearer Token downstream
				.requestInterceptor((request, body, execution) -> {
					Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					if (authentication instanceof JwtAuthenticationToken jwtAuth) {
						String tokenValue = jwtAuth.getToken().getTokenValue();
						request.getHeaders().setBearerAuth(tokenValue);
					}

					return execution.execute(request, body);
				})
				.build();

		RestClientAdapter adapter = RestClientAdapter.create(restClient);
		HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
				.builder()
				.exchangeAdapter(adapter)
				.build();
		return proxyFactory.createClient(ProductClient.class);
	}
}
