package hr.fer.fringilla.fringillasport.controller;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.fringilla.fringillasport.payload.request.LoginRequest;
import hr.fer.fringilla.fringillasport.payload.response.JwtResponse;
import hr.fer.fringilla.fringillasport.payload.response.MessageResponse;
import hr.fer.fringilla.fringillasport.repository.UserRepository;
import hr.fer.fringilla.fringillasport.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ProfileController {
	@Autowired
	UserRepository userRepository;

	@GetMapping("/me")
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ATHLETE') or hasRole('COACH') or hasRole('RENTER')")
	public ResponseEntity<?> meUser() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		if (!userRepository.existsByUsername(userDetails.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Token is invalid!"));
		}

		HashMap<String, Object> ret = new HashMap<String, Object>();
		ret.put("user_info", userRepository.findByUsername(userDetails.getUsername()));
		ret.put("roles", roles);
		return ResponseEntity.ok(ret);
	}
}
