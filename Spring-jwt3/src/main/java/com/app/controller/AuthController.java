package com.app.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.ERole;
import com.app.model.Roles;
import com.app.model.User;
import com.app.security.payload.request.SignupRequest;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.security.jwt.JwtUtils;
import com.app.security.payload.request.LoginRequest;
import com.app.security.payload.response.JwtResponse;
import com.app.security.payload.response.MessageResponse;
import com.app.security.services.UserDetailsImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

	@Autowired
	private final RoleRepository roleRepository;
	@Autowired
	private final JwtUtils jwtUtils;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;

	

	AuthController(JwtUtils jwtUtils, RoleRepository roleRepository) {
		this.jwtUtils = jwtUtils;
		this.roleRepository = roleRepository;
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetail = (UserDetailsImpl) authentication.getPrincipal();

		List<String> roles = userDetail.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

//	    String token,
//	    String type,
//	    Long id,
//	    String username,
//	    String email,
//	    List<String> roles

		return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", userDetail.getId(), userDetail.getUsername(),
				userDetail.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

		if (userRepository.existsByUsername(signupRequest.getUsername())) {

			return ResponseEntity.badRequest().body(new MessageResponse("Error !: Username already taken"));

		}

		if (userRepository.existsByEmail(signupRequest.getEmail())) {

			return ResponseEntity.badRequest().body(new MessageResponse("Error ! : Email already taken"));
		}

		User user = new User();
		user.setUsername(signupRequest.getUsername());
		user.setPassword(encoder.encode(signupRequest.getPassword()));
		user.setEmail(signupRequest.getEmail());

		Set<String> strRoles = signupRequest.getRole();

		Set<Roles> rolesSet = new HashSet<>();

		if (strRoles == null) {
			Roles userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not Found"));
			rolesSet.add(userRole);
		} else {

			strRoles.forEach(role -> {

				switch (role) {

				case "admin":
					Roles adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error : Role not Found"));
					rolesSet.add(adminRole);
					break;
				case "mod":
					Roles modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error : Role not Found"));
					rolesSet.add(modRole);
					break;

				default:
					Roles userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error : Role not Found"));
					rolesSet.add(userRole);
				}

			});
		}
		user.setRoles(rolesSet);
		userRepository.save(user);
	
		return  ResponseEntity.ok(new MessageResponse("User Registered Successfully"));

	}

}
