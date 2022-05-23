package hr.fer.fringilla.fringillasport.controller;

import hr.fer.fringilla.fringillasport.domain.*;
import hr.fer.fringilla.fringillasport.payload.request.SignupRequest;
import hr.fer.fringilla.fringillasport.payload.request.UpdateUserRequest;
import hr.fer.fringilla.fringillasport.payload.response.MessageResponse;
import hr.fer.fringilla.fringillasport.repository.*;
import hr.fer.fringilla.fringillasport.security.services.UserDetailsImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	EntityManager em;

	@GetMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.ok(userRepository.findAll());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#id == principal.id)")
	public ResponseEntity<User> getUser(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			logger.error("User id: " + id + " is not existed");
			ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(user.get());
	}

	@GetMapping("/me")
	@PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ATHLETE') or hasRole('COACH') or hasRole('RENTER')")
	public ResponseEntity<?> getMeUser() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		HashMap<String, Object> ret = new HashMap<String, Object>();
		ret.put("user_info", userRepository.findById(userDetails.getId()));
		ret.put("roles", roles);
		return ResponseEntity.ok(ret);
	}

	@PostMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signupRequest) {
		User user;

		switch (signupRequest.getAccountType()) {
		case "admin":
			user = new Administrator();
		case "athlete":
			user = new Athlete();
			break;
		case ("renter"):
			user = new Renter();
			break;
		default:
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Account type doesn't exist!"));
		}
		if (user instanceof Person) {
			((Person) user).setName(signupRequest.getFirstName());
			((Person) user).setSurname(signupRequest.getLastName());
			((Person) user).setEmail(signupRequest.getEmail());
		}
		user.setUsername(signupRequest.getUsername());
		user.setPasswdHash(encoder.encode(signupRequest.getPassword()));
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or  (#id == principal.id)")
	public ResponseEntity<?> updateUser(@PathVariable Long id,
			@Valid @RequestBody UpdateUserRequest updateUserRequest) {
		if (!userRepository.findById(id).isPresent()) {
			logger.error("Sport id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		User user = userRepository.findById(id).get();

		// userRepository.delete(user);

		User newUser;

		if (updateUserRequest.getAccountType() != null) {
			switch (updateUserRequest.getAccountType()) {
			case "renter":
				newUser = new Renter();
				break;
			case "athlete":
				newUser = new Athlete();
				break;
			case "admin":
				newUser = new Administrator();
				break;
			case "coach":
				newUser = new Coach();
				break;
			default:
				return ResponseEntity.badRequest().build();
			}
		} else
			newUser = user;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

		if ((newUser instanceof Administrator || newUser instanceof Coach)
				&& !(userRepository.findById(userDetails.getId()).get() instanceof Administrator)) {
			return ResponseEntity.badRequest().body("Insufficient permissions!");
		}
		BeanUtils.copyProperties(user, newUser, "locationOwner");

		if (newUser instanceof Person) {
			if (updateUserRequest.getEmail() != null)
				((Person) newUser).setEmail(updateUserRequest.getEmail());
			if (updateUserRequest.getFirstName() != null)
				((Person) newUser).setName(updateUserRequest.getFirstName());
			if (updateUserRequest.getLastName() != null)
				((Person) newUser).setSurname(updateUserRequest.getLastName());
		}
		if (newUser instanceof Athlete) {
			if (updateUserRequest.getHeight() != null)
				((Athlete) newUser).setHeight(updateUserRequest.getHeight());
			if (updateUserRequest.getWeight() != null)
				((Athlete) newUser).setWeight(updateUserRequest.getWeight());
			if (updateUserRequest.getGender() != null)
				((Athlete) newUser).setGender(updateUserRequest.getGender());
			if (updateUserRequest.getBirthdayDate() != null)
				((Athlete) newUser).setBirthdayDate(updateUserRequest.getBirthdayDate());
		}
		// em.createNativeQuery("INSERT into t_coach (id) VALUES (" + user.getId() +
		// ")").executeUpdate();
		if (updateUserRequest.getPassword() != null)
			newUser.setPasswdHash(encoder.encode(updateUserRequest.getPassword()));

		if (updateUserRequest.getAccountType() != null)
			userRepository.delete(user);
		userRepository.save(newUser);
		return ResponseEntity.ok("Uspjesna promjena");
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		if (!userRepository.findById(id).isPresent()) {
			logger.error("Sport id" + id + "is not existed");
			return ResponseEntity.badRequest().build();
		}
		userRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
}