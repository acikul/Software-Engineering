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

import hr.fer.fringilla.fringillasport.domain.Sport;

import hr.fer.fringilla.fringillasport.service.SportService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/sports")
public class SportController {
	@Autowired
	SportService sportService;

	private static final Logger logger = LoggerFactory.getLogger(SportController.class);

	@GetMapping("")
	public ResponseEntity<List<Sport>> getSports() {
		return ResponseEntity.ok(sportService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Sport> getSport(@PathVariable Long id) {
		Optional<Sport> sport = sportService.findById(id);
		if (!sport.isPresent()) {
			logger.error("Sport id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(sport.get());
	}

	@PostMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> createSport(@Valid @RequestBody Sport sport) {
		return ResponseEntity.ok(sportService.save(sport));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> updateSport(@PathVariable Long id, @Valid @RequestBody Sport sport) {
		if (!sportService.findById(id).isPresent()) {
			logger.error("Sport id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(sportService.save(sport));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> deleteSport(@PathVariable Long id) {
		if (!sportService.findById(id).isPresent()) {
			logger.error("Sport id" + id + "is not existed");
			return ResponseEntity.badRequest().build();
		}

		sportService.deleteById(id);

		return ResponseEntity.ok().build();
	}

}
