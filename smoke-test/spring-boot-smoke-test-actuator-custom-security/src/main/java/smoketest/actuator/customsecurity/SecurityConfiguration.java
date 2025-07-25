/*
 * Copyright 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package smoketest.actuator.customsecurity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.actuate.web.mappings.MappingsEndpoint;
import org.springframework.boot.security.autoconfigure.actuate.servlet.EndpointRequest;
import org.springframework.boot.security.autoconfigure.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration(proxyBeanMethods = false)
public class SecurityConfiguration {

	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		List<UserDetails> userDetails = new ArrayList<>();
		userDetails.add(createUserDetails("user", "password", "ROLE_USER"));
		userDetails.add(createUserDetails("beans", "beans", "ROLE_BEANS"));
		userDetails.add(createUserDetails("admin", "admin", "ROLE_ACTUATOR", "ROLE_USER"));
		return new InMemoryUserDetailsManager(userDetails);
	}

	@SuppressWarnings("deprecation")
	private UserDetails createUserDetails(String username, String password, String... authorities) {
		UserBuilder builder = User.withDefaultPasswordEncoder();
		builder.username(username);
		builder.password(password);
		builder.authorities(authorities);
		return builder.build();
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> {
			requests.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/actuator/beans"))
				.hasRole("BEANS");
			requests.requestMatchers(EndpointRequest.to("health")).permitAll();
			requests.requestMatchers(EndpointRequest.toAnyEndpoint().excluding(MappingsEndpoint.class))
				.hasRole("ACTUATOR");
			requests.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
			requests.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/foo")).permitAll();
			requests.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/error")).permitAll();
			requests.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/**")).hasRole("USER");
		});
		http.cors(withDefaults());
		http.httpBasic(withDefaults());
		return http.build();
	}

}
