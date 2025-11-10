package com.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		// for authentication info provide configuration
//		auth.inMemoryAuthentication().withUser("raja").password("{noop}rani").roles("CUSTOMER");
//		auth.inMemoryAuthentication().withUser("rajesh").password("{noop}hyd").roles("MANAGER");
//		auth.inMemoryAuthentication().withUser("mahesh").password("{noop}delhi").roles("MANAGER", "CUSTOMER");
//		auth.inMemoryAuthentication().withUser("suresh").password("{noop}hero").roles("VISITOR");
		
		
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("raja").password("$2a$12$VqOeGVBHI7wLMiERATodlusiiAey.loNtMqpFGFdSlK4auv6/h08K").roles("CUSTOMER").accountLocked(true);
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("rajesh").password("$2a$10$TxXHRo1/1BG9roW/SIjZcO8h2DVkt6xyfuGHJEYVhAdtM7TmHJumS").roles("MANAGER");
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("mahesh").password("$2a$10$iAEHHDVOtOOHM1GuMA84BOwcoRrV7NDPrvyZdDChRjFoi9FZs0aJa").roles("MANAGER", "CUSTOMER");
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("suresh").password("$2a$10$rIPwlwoUCyNs0ieev5Pq/.HeGfvmcd/tayFgO4goXZaGZna/ojxAi").roles("VISITOR");

		
//	this will lock the user from login ,this will disable the username and password	
//	auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
//	.withUser("raja").password("$2a$12$VqOeGVBHI7wLMiERATodlusiiAey.loNtMqpFGFdSlK4auv6/h08K")
//		.roles("CUSTOMER").accountLocked(true);

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// CONFIGURE AUTHETICATION + AUTHORIZATION INFO PROVIDER
		http.authorizeHttpRequests().antMatchers("/bank/").permitAll()
		.antMatchers("/bank/offers").authenticated()
		.antMatchers("/bank/balance").hasAnyRole("VISITOR","MANAGER")
		.antMatchers("/bank/loanApprove").hasRole("MANAGER")
		.anyRequest().authenticated()
		.and()
		.formLogin()// for form based authentication
//		.and()
//		.rememberMe()  // not working bug in spring security error
		.and()
		.logout()
		.logoutSuccessUrl("/bank/")
		.and().exceptionHandling().accessDeniedPage("/bank/denied")// for configuring custom page,for autherization failure
		.and().sessionManagement().maximumSessions(5).maxSessionsPreventsLogin(true); // keep it at the end
		//; basic authentication uses the browser geneated dailog box for asking user name and password		
//		.httpBasic()
	
//  .and().sessionManagement().maximumSessions(2).maxSessionsPreventsLogin(true);  maximum 2 places login can take place,2 time we can login differnet places
		
	}

}
