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
import hr.fer.fringilla.fringillasport.domain.Opening;
import hr.fer.fringilla.fringillasport.service.LocationService;
import hr.fer.fringilla.fringillasport.service.OpeningService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/openings")
public class OpeningController {
	@Autowired
	OpeningService openingService;

	private static final Logger logger = LoggerFactory.getLogger(OpeningController.class);

	@GetMapping("")
	public ResponseEntity<List<Opening>> getOpenings() {
		return ResponseEntity.ok(openingService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Opening> getOpening(@PathVariable Long id) {
		Optional<Opening> opening = openingService.findById(id);
		if (!opening.isPresent()) {
			logger.error("Opening id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(opening.get());
	}

	@PostMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> createOpening(@Valid @RequestBody Opening opening) {
		return ResponseEntity.ok(openingService.save(opening));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> updateOpening(@PathVariable Long id, @Valid @RequestBody Opening opening) {
		if (!openingService.findById(id).isPresent()) {
			logger.error("Location id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(openingService.save(opening));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> deleteOpening(@PathVariable Long id) {
		if (!openingService.findById(id).isPresent()) {
			logger.error("Opening id" + id + "is not existed");
			return ResponseEntity.badRequest().build();
		}

		openingService.deleteById(id);

		return ResponseEntity.ok().build();
	}
}
