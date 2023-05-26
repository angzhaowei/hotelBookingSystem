package com.fdmgroup.hotelBookingProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fdmgroup.hotelBookingProject.security.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	@Autowired
	private MyUserDetailsService myUserDetailService;
	
	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(myUserDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeHttpRequests((authz) -> {
			try {
				authz.requestMatchers("/","index" ,"/login", "/register", "/h2/**", "/css/**", "/js/**", "/images/**")
						.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll()
						.defaultSuccessUrl("/userHomePage")
						.successHandler((request, response, authentication) -> {
			                response.sendRedirect("/userHomePage");
			            })
			            .permitAll()
						.and().logout().invalidateHttpSession(true).clearAuthentication(true)
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index")
						.permitAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).httpBasic();
		http.headers().frameOptions().disable();
		
		// this makes every reload go to "/index" instead of "/login"
		http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"));
		
		return http.build();
	}
}
