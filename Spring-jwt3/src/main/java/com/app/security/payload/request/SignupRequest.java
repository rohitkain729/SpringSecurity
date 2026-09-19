package com.app.security.payload.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequest {

	@NotBlank
	@Size(min = 6, max = 100)
	private String username;
	@NotBlank
	@Size(min = 6, max = 100)
	private String password;
	@NotBlank
	@Size(max = 30)
	private String email;
	private Set<String> role;
}
