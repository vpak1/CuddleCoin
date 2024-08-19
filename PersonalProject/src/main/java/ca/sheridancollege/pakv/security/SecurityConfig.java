package ca.sheridancollege.pakv.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	UserDetailsService inMemoryUserDetailsService(PasswordEncoder passwordEncoder) {
	    UserDetails admin = User.withUsername("pakv@sheridancollege.ca")
	                            .password(passwordEncoder.encode("1234"))
	                            .roles("ADMIN").build();
	    UserDetails user = User.withUsername("user@sheridancollege.ca")
	                           .password(passwordEncoder.encode("123"))
	                           .roles("USER").build();
	    return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);
        return http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(mvc.pattern("/secure/**")).hasAnyRole("USER", "ADMIN")
                .requestMatchers(mvc.pattern("/admin/**")).hasRole("ADMIN")
                .requestMatchers(mvc.pattern("/")).permitAll()
                .requestMatchers(mvc.pattern("/js/**")).permitAll()
                .requestMatchers(mvc.pattern("/css/**")).permitAll()
                .requestMatchers(mvc.pattern("/images/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/register")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/register")).permitAll()
                .requestMatchers(mvc.pattern("/permission-denied")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                .requestMatchers(mvc.pattern("/**")).permitAll()
                .requestMatchers(mvc.pattern("/secure/goal/**")).permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).disable())
            .csrf(csrf -> csrf.ignoringRequestMatchers(mvc.pattern("/secure/api/v1/goal/**")).disable())
            .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
            .formLogin(form -> form.loginPage("/login").permitAll().defaultSuccessUrl("/secure/transaction", true))
            .exceptionHandling(exception -> exception.accessDeniedPage("/permission-denied"))
            .logout(logout -> logout.permitAll())
            .userDetailsService(userDetailsService) // Set the custom UserDetailsService
            .build();
    }


	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
