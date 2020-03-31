package com.marvastsi.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.marvastsi.demo.auth.JwtAuthenticationFilter;
import com.marvastsi.demo.auth.UnauthorizedAuth;

@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("unauthorizedAuth")
	private UnauthorizedAuth unauthorizedHandler;

	@Bean
	public JwtAuthenticationFilter authFilterConfig() throws Exception {
		return new JwtAuthenticationFilter();
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Value("${server.denied.paths:''}")
	private String[] deniedPaths;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
			.and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(deniedPaths).denyAll()
			.antMatchers("**").permitAll()
			.anyRequest().authenticated()
			.and()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authFilterConfig(), UsernamePasswordAuthenticationFilter.class);
		http.headers().frameOptions().disable();
		http.httpBasic().disable();
	}

}