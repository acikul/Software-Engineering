package hr.fer.fringilla.fringillasport.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.Opening;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;
import hr.fer.fringilla.fringillasport.domain.Person;
import hr.fer.fringilla.fringillasport.domain.Renter;
import hr.fer.fringilla.fringillasport.domain.SportEvent;
import hr.fer.fringilla.fringillasport.domain.User;
import hr.fer.fringilla.fringillasport.payload.request.LocationRequest;
import hr.fer.fringilla.fringillasport.payload.request.LocationTypeRequest;
import hr.fer.fringilla.fringillasport.payload.request.OpeningRequest;
import hr.fer.fringilla.fringillasport.service.LocationService;
import hr.fer.fringilla.fringillasport.service.OpeningService;
import hr.fer.fringilla.fringillasport.service.SportEventService;
import hr.fer.fringilla.fringillasport.service.SportService;
import hr.fer.fringilla.fringillasport.service.UserService;
import hr.fer.fringilla.fringillasport.service.UtilitiesService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users/{user_id}/locations/{location_id}/reservations")
public class UserLocationsReservationsController {
	@Autowired
	LocationService locationService;

	@Autowired
	SportEventService sportEventService;

	private static final Logger logger = LoggerFactory.getLogger(UserLocationsController.class);

	@GetMapping("")
	@PreAuthorize("hasRole('RENTER') and (#user_id == principal.id)")
	public ResponseEntity<?> getUserLocationReservations(@PathVariable Long user_id, @PathVariable Long location_id) {
		if (!locationService.findByIdAndCreatorId(location_id, user_id).isPresent()) {
			logger.error("Location id" + location_id + "is not existed");
			ResponseEntity.badRequest().build();
		}

		Location location = locationService.findByIdAndCreatorId(location_id, user_id).get();

		if (location instanceof PaidLocation) {
			return ResponseEntity.ok(((PaidLocation) location).getReservationQueue());
		} else {
			return ResponseEntity.badRequest().body("This location is not paid one!");
		}
	}

	@GetMapping("/{sportevent_id}")
	@PreAuthorize("hasRole('RENTER') and (#user_id == principal.id)")
	public ResponseEntity<?> getUserLocationReservation(@PathVariable Long user_id, @PathVariable Long location_id,
			@PathVariable Long sportevent_id) {
		if (!locationService.findByIdAndCreatorId(location_id, user_id).isPresent()) {
			logger.error("Location id" + location_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		if (!sportEventService.findById(sportevent_id).isPresent()) {
			logger.error("SportEvent id" + sportevent_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		SportEvent sportEvent = sportEventService.findById(sportevent_id).get();

		Location location = locationService.findByIdAndCreatorId(location_id, user_id).get();

		if (location instanceof PaidLocation && (((PaidLocation) location).getReservationQueue().contains(sportEvent)
				|| ((PaidLocation) location).getHoldsSportEvent().contains(sportEvent))) {
			return ResponseEntity.ok(sportEvent);
		} else {
			return ResponseEntity.badRequest().body("error!");
		}
	}

	@PostMapping("/accept/{sportevent_id}")
	@PreAuthorize("hasRole('RENTER') and (#user_id == principal.id)")
	public ResponseEntity<?> approveUserLocationReservation(@PathVariable Long user_id, @PathVariable Long location_id,
			@PathVariable Long sportevent_id) {
		if (!locationService.findByIdAndCreatorId(location_id, user_id).isPresent()) {
			logger.error("Location id" + location_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		if (!sportEventService.findById(sportevent_id).isPresent()) {
			logger.error("SportEvent id" + sportevent_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		SportEvent sportEvent = sportEventService.findById(sportevent_id).get();

		Location location = locationService.findByIdAndCreatorId(location_id, user_id).get();

		if (location instanceof PaidLocation && ((PaidLocation) location).getReservationQueue().contains(sportEvent)
				&& (sportEvent).getLocation() == null) {
			sportEvent.setLocation(location);
			sportEvent.getLocationReservationInquery().clear();
			sportEventService.save(sportEvent);
			return ResponseEntity.ok(sportEvent);
		} else {
			return ResponseEntity.badRequest().body("error!");
		}
	}

	@PostMapping("/decline/{sportevent_id}")
	@PreAuthorize("hasRole('RENTER') and (#user_id == principal.id)")
	public ResponseEntity<?> declineUserLocationReservation(@PathVariable Long user_id, @PathVariable Long location_id,
			@PathVariable Long sportevent_id) {
		if (!locationService.findByIdAndCreatorId(location_id, user_id).isPresent()) {
			logger.error("Location id" + location_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		if (!sportEventService.findById(sportevent_id).isPresent()) {
			logger.error("SportEvent id" + sportevent_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		SportEvent sportEvent = sportEventService.findById(sportevent_id).get();

		Location location = locationService.findByIdAndCreatorId(location_id, user_id).get();

		location.getHoldsSportEvent().remove(sportEvent);

		if (location instanceof PaidLocation) {
			((PaidLocation) location).getReservationQueue().remove(sportEvent);
		}
		sportEvent.getLocationReservationInquery().remove(location);

		if (sportEvent.getLocation() != null && sportEvent.getLocation().equals(location))
			sportEvent.setLocation(null);
		sportEventService.save(sportEvent);
		locationService.save(location);
		return ResponseEntity.ok(location);
	}
}
