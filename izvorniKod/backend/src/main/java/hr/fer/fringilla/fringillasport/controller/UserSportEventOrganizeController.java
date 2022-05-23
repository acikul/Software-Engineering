package hr.fer.fringilla.fringillasport.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.hibernate.mapping.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.fer.fringilla.fringillasport.domain.Athlete;
import hr.fer.fringilla.fringillasport.domain.Coach;
import hr.fer.fringilla.fringillasport.domain.Documentation;
import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;
import hr.fer.fringilla.fringillasport.domain.PaidSportEvent;
import hr.fer.fringilla.fringillasport.domain.Person;
import hr.fer.fringilla.fringillasport.domain.Sport;
import hr.fer.fringilla.fringillasport.domain.SportEvent;
import hr.fer.fringilla.fringillasport.domain.User;
import hr.fer.fringilla.fringillasport.payload.request.SportEventRequest;
import hr.fer.fringilla.fringillasport.service.LocationService;
import hr.fer.fringilla.fringillasport.service.SportEventService;
import hr.fer.fringilla.fringillasport.service.SportService;
import hr.fer.fringilla.fringillasport.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users/{user_id}/sportevents/organize")
public class UserSportEventOrganizeController {

	private static final Logger logger = LoggerFactory.getLogger(UserSportEventOrganizeController.class);

	@Autowired
	UserService userService;

	@Autowired
	SportEventService sportEventService;

	@Autowired
	SportService sportService;

	@Autowired
	LocationService locationService;

	@GetMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> getUserOrganizeSportEvents(@PathVariable Long user_id) {
		if (!userService.findById(user_id).isPresent()) {
			logger.error("User id" + user_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		User user = userService.findById(user_id).get();

		if (user instanceof Athlete)
			return ResponseEntity.ok(((Athlete) user).getEventsOrganizer());
		else
			return ResponseEntity.badRequest().build();
	}

	@GetMapping("/{sportevent_id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> getUserOrganizeSportEvent(@PathVariable Long user_id, @PathVariable Long sportevent_id) {

		if (!sportEventService.findById(sportevent_id).isPresent()) {
			logger.error("SportEvent with id: " + sportevent_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		if (!userService.findById(user_id).isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}
		SportEvent sportEvent = sportEventService.findById(sportevent_id).get();
		User athlete = userService.findById(user_id).get();

		if (!(sportEvent.getOrganizer() == athlete)) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(sportEvent);
	}

	@PostMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> organizeNewUserSportEvent(@PathVariable Long user_id,
			@RequestBody SportEventRequest sportEventRequest) {
		if (!userService.findById(user_id).isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}
		User user = userService.findById(user_id).get();

		if (!(user instanceof Athlete)) {
			return ResponseEntity.badRequest().body("must be atleast athlete");
		}

		SportEvent newEvent;

		if (sportEventRequest.getSportEventType().equals("free")) {
			newEvent = new SportEvent();
		} else if (sportEventRequest.getSportEventType().equals("paid")) {
			newEvent = new PaidSportEvent();
		} else {
			logger.error("Sport event type is required (free/paid)!");
			return ResponseEntity.badRequest().build();
		}

		BeanUtils.copyProperties(sportEventRequest, newEvent);

		if (!sportService.findById(sportEventRequest.getSportId()).isPresent()) {
			logger.error("Sport with id: " + sportEventRequest.getSportId() + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		Sport sport = sportService.findById(sportEventRequest.getSportId()).get();

		newEvent.setSport(sport);

		if (newEvent instanceof PaidSportEvent) {
			if (!(user instanceof Coach)) {
				return ResponseEntity.badRequest().body("Only coach can organize paid event");
			}
			Coach coach = (Coach) user;
			if (!coach.getCoachableSports().contains(newEvent.getSport())) {
				return ResponseEntity.badRequest().body("Only certified coach for that sport can organize paid event");
			}
		}

		newEvent.setOrganizer((Athlete) user);

		return ResponseEntity.ok(sportEventService.save(newEvent));
	}

	@DeleteMapping("/{sportevent_id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> deleteUserOrganizeSportEvent(@PathVariable Long user_id,
			@PathVariable Long sportevent_id) {

		if (!sportEventService.findById(sportevent_id).isPresent()) {
			logger.error("SportEvent with id: " + sportevent_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		if (!userService.findById(user_id).isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		sportEventService.deleteById(sportevent_id);
		return ResponseEntity.ok("deleted!");
	}
}