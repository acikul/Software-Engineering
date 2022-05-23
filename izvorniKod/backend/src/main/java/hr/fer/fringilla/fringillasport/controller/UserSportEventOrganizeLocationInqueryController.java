package hr.fer.fringilla.fringillasport.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;
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
@RequestMapping("/api/v1/users/{user_id}/sportevents/organize/{sportevent_id}/location_inquery")
public class UserSportEventOrganizeLocationInqueryController {

	private static final Logger logger = LoggerFactory.getLogger(UserSportEventOrganizeLocationInqueryController.class);

	@Autowired
	UserService userService;

	@Autowired
	SportEventService sportEventService;

	@Autowired
	SportService sportService;

	@Autowired
	LocationService locationService;

	@GetMapping("/suggest")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> suggestLocationForInquery(@PathVariable Long user_id, @PathVariable Long sportevent_id) {

		if (!sportEventService.findById(sportevent_id).isPresent()) {
			logger.error("SportEvent with id: " + sportevent_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		if (!userService.findById(user_id).isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		SportEvent sportEvent = sportEventService.findById(sportevent_id).get();

		List<Location> available_locations = locationService.getAvailableLocations(sportEvent);

		available_locations.removeAll(sportEvent.getLocationReservationInquery());

		return ResponseEntity.ok(available_locations);
	}

	@PostMapping("/presuggest")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> presuggestLocationForInquery(@PathVariable Long user_id, @PathVariable Long sportevent_id,
			@Valid @RequestBody SportEventRequest sportEventRequest) {

		if (!userService.findById(user_id).isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}
		SportEvent sportEvent = new SportEvent();

		BeanUtils.copyProperties(sportEventRequest, sportEvent);

		if (!sportService.findById(sportEventRequest.getSportId()).isPresent()) {
			logger.error("Sport with id: " + sportEventRequest.getSportId() + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		Sport sport = sportService.findById(sportEventRequest.getSportId()).get();

		sportEvent.setSport(sport);

		List<Location> available_locations = locationService.getAvailableLocations(sportEvent);

		return ResponseEntity.ok(available_locations);
	}

	@GetMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> getLocationsInquery(@PathVariable Long user_id, @PathVariable Long sportevent_id) {
		if (!sportEventService.findById(sportevent_id).isPresent()) {
			logger.error("SportEvent with id: " + sportevent_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		if (!userService.findById(user_id).isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		SportEvent sportEvent = sportEventService.findById(sportevent_id).get();

		User user = userService.findById(user_id).get();

		if (!sportEvent.getOrganizer().equals(user)) {
			return ResponseEntity.badRequest().body("User sportEvent mismatch!");
		}

		return ResponseEntity.ok(sportEvent.getLocationReservationInquery());
	}

	@GetMapping("/{location_id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> getLocationInquery(@PathVariable Long user_id, @PathVariable Long sportevent_id,
			@PathVariable Long location_id) {
		if (!sportEventService.findById(sportevent_id).isPresent()) {
			logger.error("SportEvent with id: " + sportevent_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		if (!userService.findById(user_id).isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		if (!locationService.findById(location_id).isPresent()) {
			logger.error("Location with id: " + location_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		SportEvent sportEvent = sportEventService.findById(sportevent_id).get();

		User user = userService.findById(user_id).get();

		Location location = locationService.findById(location_id).get();

		if (!sportEvent.getOrganizer().equals(user)) {
			return ResponseEntity.badRequest().body("User sportEvent mismatch!");
		}
		if (!sportEvent.getLocationReservationInquery().contains(location)) {
			return ResponseEntity.badRequest().body("This event does not contain this location reservation inquery!");
		}

		return ResponseEntity.ok(location);
	}

	@PostMapping("/{location_id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> createLocationInquery(@PathVariable Long user_id, @PathVariable Long sportevent_id,
			@PathVariable Long location_id) {
		if (!sportEventService.findById(sportevent_id).isPresent()) {
			logger.error("SportEvent with id: " + sportevent_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		if (!userService.findById(user_id).isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		if (!locationService.findById(location_id).isPresent()) {
			logger.error("Location with id: " + location_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		SportEvent sportEvent = sportEventService.findById(sportevent_id).get();

		User user = userService.findById(user_id).get();

		Location location = locationService.findById(location_id).get();

		if (!sportEvent.getOrganizer().equals(user)) {
			return ResponseEntity.badRequest().body("User sportEvent mismatch!");
		}

		if (!locationService.checkIfLocationIsFree(location, sportEvent)) {
			return ResponseEntity.badRequest().body("Location is not for that sport/Location is already reserved!");
		}

		if (location instanceof PaidLocation) {
			sportEvent.getLocationReservationInquery().add((PaidLocation) location);
		} else if (location instanceof Location) {
			sportEvent.getLocationReservationInquery().clear();
			sportEvent.setLocation(location);
		}

		return ResponseEntity.ok(sportEventService.save(sportEvent));
	}

	@DeleteMapping("/{location_id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> deleteLocationInquery(@PathVariable Long user_id, @PathVariable Long sportevent_id,
			@PathVariable Long location_id) {
		if (!sportEventService.findById(sportevent_id).isPresent()) {
			logger.error("SportEvent with id: " + sportevent_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		if (!userService.findById(user_id).isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		if (!locationService.findById(location_id).isPresent()) {
			logger.error("Location with id: " + location_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}

		SportEvent sportEvent = sportEventService.findById(sportevent_id).get();

		User user = userService.findById(user_id).get();

		Location location = locationService.findById(location_id).get();

		if (!sportEvent.getOrganizer().equals(user)) {
			return ResponseEntity.badRequest().body("User sportEvent mismatch!");
		}
		if (!sportEvent.getLocationReservationInquery().contains(location)) {
			return ResponseEntity.badRequest().body("This event does not contain this location reservation inquery!");
		}
		sportEvent.getLocationReservationInquery().remove(location);
		sportEventService.save(sportEvent);

		return ResponseEntity.ok(location);
	}
}
