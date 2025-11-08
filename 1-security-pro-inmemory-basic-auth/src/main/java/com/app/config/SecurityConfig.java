package com.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// for authentication info provide configuration
		auth.inMemoryAuthentication().withUser("raja").password("{noop}rani").roles("CUSTOMER");
		auth.inMemoryAuthentication().withUser("rajesh").password("{noop}hyd").roles("MANAGER");
		auth.inMemoryAuthentication().withUser("mahesh").password("{noop}delhi").roles("MANAGER", "CUSTOMER");
		auth.inMemoryAuthentication().withUser("suresh").password("{noop}hero").roles("VISITOR");

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
		.and()
		.logout()
		.logoutSuccessUrl("/bank/")
		.and().exceptionHandling().accessDeniedPage("/bank/denied");// for configuring custom page,for autherization failure
		//; basic authentication uses the browser geneated dailog box for asking user name and password		
//		.httpBasic()
	
	}

}
