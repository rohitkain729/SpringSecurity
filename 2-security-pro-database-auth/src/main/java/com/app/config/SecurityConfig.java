package com.app.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource ds;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		try {
			auth.jdbcAuthentication().dataSource(ds).passwordEncoder(new BCryptPasswordEncoder())
					.usersByUsernameQuery("select UNAME,PWD,STATUS from USERS where UNAME=?")
					.authoritiesByUsernameQuery("select UNAME,ROLE from USERS_ROLES where UNAME=?");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// CONFIGURE AUTHETICATION + AUTHORIZATION INFO PROVIDER
		http.authorizeHttpRequests().antMatchers("/bank/").permitAll().antMatchers("/bank/offers").authenticated()
				.antMatchers("/bank/balance").hasAnyAuthority("VISITOR", "MANAGER").antMatchers("/bank/loanApprove")
				.hasAuthority("MANAGER").anyRequest().authenticated().and().formLogin()// for form based authentication
//		.and()
//		.rememberMe()  // not working bug in spring security error
				.and().logout().logoutSuccessUrl("/bank/").and().exceptionHandling().accessDeniedPage("/bank/denied")// for
																														// configuring
																														// custom
																														// page,for
																														// autherization
																														// failure
				.and().sessionManagement().maximumSessions(5).maxSessionsPreventsLogin(true); // keep it at the end
		// ; basic authentication uses the browser geneated dailog box for asking user
		// name and password
//		.httpBasic()

//  .and().sessionManagement().maximumSessions(2).maxSessionsPreventsLogin(true);  maximum 2 places login can take place,2 time we can login differnet places

	}

}
