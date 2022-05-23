package hr.fer.fringilla.fringillasport.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.aspectj.lang.annotation.DeclareWarning;
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

import hr.fer.fringilla.fringillasport.domain.Administrator;
import hr.fer.fringilla.fringillasport.domain.Coach;
import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;
import hr.fer.fringilla.fringillasport.domain.Person;
import hr.fer.fringilla.fringillasport.domain.Renter;
import hr.fer.fringilla.fringillasport.domain.User;
import hr.fer.fringilla.fringillasport.payload.request.LocationRequest;
import hr.fer.fringilla.fringillasport.payload.request.LocationTypeRequest;
import hr.fer.fringilla.fringillasport.service.LocationService;
import hr.fer.fringilla.fringillasport.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users/{user_id}/locations")
public class UserLocationsController {
	@Autowired
	LocationService locationService;

	@Autowired
	UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

	@GetMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<List<Location>> getUserLocations(@PathVariable Long user_id,
			@RequestBody(required = false) LocationTypeRequest locationTypeRequest) {
		List<Location> locations_raw = locationService.findAllCreatorId(user_id);

		if (locationTypeRequest != null && locationTypeRequest.locationType != null)
			return ResponseEntity.ok(locations_raw.stream()
					.filter(location -> location.getLocationStringType().equals(locationTypeRequest.locationType))
					.collect(Collectors.toList()));

		return ResponseEntity.ok(locations_raw);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<Location> getUserLocation(@PathVariable Long id, @PathVariable Long user_id,
			@RequestBody(required = false) LocationTypeRequest locationTypeRequest) {
		Optional<Location> location = locationService.findByIdAndCreatorId(id, user_id);
		if (!location.isPresent()) {
			logger.error("Location id" + id + "is not existed");
			return ResponseEntity.badRequest().build();
		}
		if (locationTypeRequest != null && locationTypeRequest.locationType != null
				&& !location.get().getLocationStringType().equals(locationTypeRequest.locationType)) {
			logger.error("Location has different type!");
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(location.get());
	}

	@PostMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> createUserLocation(@PathVariable Long user_id,
			@Valid @RequestBody LocationRequest locationRequest) {
		if (!userService.findById(user_id).isPresent()) {
			logger.error("User with id: " + user_id + "does not exist");
			return ResponseEntity.badRequest().build();
		}
		User user = userService.findById(user_id).get();

		Location newLocation;

		if (locationRequest.getLocationType().equals("free")) {
			newLocation = new Location();
		} else if (locationRequest.getLocationType().equals("paid")) {
			newLocation = new PaidLocation();
			if (!(user instanceof Renter)) {
				return ResponseEntity.badRequest().body("Only renter can create paid locations");
			}
		} else {
			logger.error("Location type is required (free/paid)!");
			return ResponseEntity.badRequest().build();
		}
		BeanUtils.copyProperties(locationRequest, newLocation);

		newLocation.setCreator((Person) userService.findById(user_id).get());

		return ResponseEntity.ok(locationService.save(newLocation));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> updateUserLocation(@PathVariable Long user_id, @PathVariable Long id,
			@Valid @RequestBody LocationRequest locationRequest) {
		if (!locationService.findByIdAndCreatorId(id, user_id).isPresent()) {
			logger.error("Location id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}

		Location location = locationService.findByIdAndCreatorId(id, user_id).get();

		if (location instanceof PaidLocation && locationRequest.getName() != null)
			((PaidLocation) location).setName(locationRequest.getName());

		if (locationRequest.getGpxCoordinates() != null)
			location.setGpxCoordinates(locationRequest.getGpxCoordinates());

		return ResponseEntity.ok(locationService.save(location));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR') or (#user_id == principal.id)")
	public ResponseEntity<?> deleteUserLocation(@PathVariable Long user_id, @PathVariable Long id) {
		if (!locationService.findByIdAndCreatorId(id, user_id).isPresent()) {
			logger.error("Location id" + id + "is not existed");
			return ResponseEntity.badRequest().build();
		}
		locationService.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
