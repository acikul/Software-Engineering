package hr.fer.fringilla.fringillasport.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import hr.fer.fringilla.fringillasport.domain.Sport;
import hr.fer.fringilla.fringillasport.service.LocationService;
import hr.fer.fringilla.fringillasport.service.SportService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
	@Autowired
	LocationService locationService;

	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

	@GetMapping("")
	public ResponseEntity<List<Location>> getLocations() {
		return ResponseEntity.ok(locationService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Location> getLocation(@PathVariable Long id) {
		Optional<Location> location = locationService.findById(id);
		if (!location.isPresent()) {
			logger.error("Location id" + id + "is not existed");
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(location.get());
	}

	@PostMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> createLocation(@Valid @RequestBody Location location) {
		return ResponseEntity.ok(locationService.save(location));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> updateLocation(@PathVariable Long id, @Valid @RequestBody Location location) {
		if (!locationService.findById(id).isPresent()) {
			logger.error("Location id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(locationService.save(location));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
		if (!locationService.findById(id).isPresent()) {
			logger.error("Location id" + id + "is not existed");
			return ResponseEntity.badRequest().build();
		}

		locationService.deleteById(id);

		return ResponseEntity.ok().build();
	}

}
