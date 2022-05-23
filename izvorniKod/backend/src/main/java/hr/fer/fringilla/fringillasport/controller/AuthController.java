package hr.fer.fringilla.fringillasport.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import hr.fer.fringilla.fringillasport.domain.Administrator;
import hr.fer.fringilla.fringillasport.domain.Athlete;
import hr.fer.fringilla.fringillasport.domain.Person;
import hr.fer.fringilla.fringillasport.domain.Renter;
import hr.fer.fringilla.fringillasport.domain.User;
import hr.fer.fringilla.fringillasport.payload.request.LoginRequest;
import hr.fer.fringilla.fringillasport.payload.request.SignupRequest;
import hr.fer.fringilla.fringillasport.payload.response.JwtResponse;
import hr.fer.fringilla.fringillasport.payload.response.MessageResponse;
import hr.fer.fringilla.fringillasport.repository.UserRepository;
import hr.fer.fringilla.fringillasport.security.jwt.JwtUtils;
import hr.fer.fringilla.fringillasport.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		Person user;
		switch (signUpRequest.getAccountType()) {
		case ("athlete"):
			user = new Athlete();
			break;
		case ("renter"):
			user = new Renter();
			break;
		default:
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Account type doesn't exist!"));
		}
		user.setEmail(signUpRequest.getEmail());
		user.setName(signUpRequest.getFirstName());
		user.setSurname(signUpRequest.getLastName());
		user.setUsername(signUpRequest.getUsername());
		user.setPasswdHash(encoder.encode(signUpRequest.getPassword()));
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

}
