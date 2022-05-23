package hr.fer.fringilla.fringillasport.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.fringilla.fringillasport.domain.Athlete;
import hr.fer.fringilla.fringillasport.domain.SportEvent;
import hr.fer.fringilla.fringillasport.domain.User;
import hr.fer.fringilla.fringillasport.repository.UserRepository;
import hr.fer.fringilla.fringillasport.service.SportEventService;
import hr.fer.fringilla.fringillasport.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users/{user_id}/sportevents/participate")
public class UserSportEventParticipateController {

	private static final Logger logger = LoggerFactory.getLogger(UserSportEventParticipateController.class);

	@Autowired
	UserService userService;

	@Autowired
	SportEventService sportEventService;

	@GetMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> getUserParticipateSportEvents(@PathVariable Long user_id) {
		if (!userService.findById(user_id).isPresent()) {
			logger.error("User id" + user_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		User user = userService.findById(user_id).get();

		if (user instanceof Athlete)
			return ResponseEntity.ok(((Athlete) user).getEventsParticipation());
		else
			return ResponseEntity.badRequest().build();
	}

	@GetMapping("/{sportevent_id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> getUserParticipateSportEvent(@PathVariable Long user_id,
			@PathVariable Long sportevent_id) {

		Optional<SportEvent> sportEvent = sportEventService.findById(sportevent_id);
		if (!sportEvent.isPresent()) {
			logger.error("SportEvent with id: " + sportevent_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		Optional<User> athlete = userService.findById(user_id);
		if (!athlete.isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}
		if (!sportEvent.get().getParticipents().contains(athlete.get())) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(sportEvent.get());
	}

	@PostMapping("/{sportevent_id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> createUserParticipateSportEvent(@PathVariable Long user_id,
			@PathVariable Long sportevent_id) {

		if (!sportEventService.findById(sportevent_id).isPresent()) {
			logger.error("SportEvent with id: " + sportevent_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		if (!userService.findById(user_id).isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		SportEvent sportEvent = sportEventService.findById(sportevent_id).get();
		User applicant = userService.findById(user_id).get();

		if (!(applicant instanceof Athlete)) {
			return ResponseEntity.badRequest().body("must be atleast athlete");
		}

		if (sportEvent.getParticipents().size() + 1 > sportEvent.getMaxNumberOfParticpents()) {
			return ResponseEntity.badRequest().body("sport event is full");
		}

		Set<Athlete> participants = sportEvent.getParticipents();

		if (!participants.contains(applicant)) {
			participants.add((Athlete) applicant);
			sportEvent.setParticipents(participants);
		}

		return ResponseEntity.ok(sportEventService.save(sportEvent));
	}

	@DeleteMapping("{sportevent_id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> deleteUserParticipateSportEvent(@PathVariable Long user_id,
			@PathVariable Long sportevent_id) {

		if (!sportEventService.findById(sportevent_id).isPresent()) {
			logger.error("SportEvent with id: " + sportevent_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		if (!userService.findById(user_id).isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		SportEvent sportEvent = sportEventService.findById(sportevent_id).get();
		User applicant = userService.findById(user_id).get();

		Set<Athlete> participants = sportEvent.getParticipents();

		if (participants.contains(applicant)) {
			participants.remove((Athlete) applicant);
			sportEvent.setParticipents(participants);
		}

		return ResponseEntity.ok(sportEventService.save(sportEvent));
	}

}
