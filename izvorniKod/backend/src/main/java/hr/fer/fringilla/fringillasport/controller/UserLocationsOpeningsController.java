package hr.fer.fringilla.fringillasport.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hr.fer.fringilla.fringillasport.domain.Documentation;
import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.Opening;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;
import hr.fer.fringilla.fringillasport.domain.Sport;
import hr.fer.fringilla.fringillasport.domain.Documentation.DocumentationType;
import hr.fer.fringilla.fringillasport.payload.request.DocumentationRequest;
import hr.fer.fringilla.fringillasport.payload.request.OpeningRequest;
import hr.fer.fringilla.fringillasport.service.LocationService;
import hr.fer.fringilla.fringillasport.service.OpeningService;
import hr.fer.fringilla.fringillasport.service.UtilitiesService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users/{user_id}/locations/{location_id}/openings")
public class UserLocationsOpeningsController {
	@Autowired
	OpeningService openingService;

	@Autowired
	LocationService locationService;

	@Autowired
	UtilitiesService utilitiesService;

	private static final Logger logger = LoggerFactory.getLogger(UserLocationsController.class);

	@GetMapping("")
	@PreAuthorize("hasRole('RENTER') and (#user_id == principal.id)")
	public ResponseEntity<?> getUserLocationOpenings(@PathVariable Long user_id, @PathVariable Long location_id) {
		if (!locationService.findByIdAndCreatorId(location_id, user_id).isPresent()) {
			logger.error("Location id" + location_id + "is not existed");
			ResponseEntity.badRequest().build();
		}

		Location location = locationService.findByIdAndCreatorId(location_id, user_id).get();

		if (location instanceof PaidLocation) {
			return ResponseEntity.ok(((PaidLocation) location).getOpenings());
		} else {
			return ResponseEntity.badRequest().body("This location is not paid one!");
		}
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('RENTER') and (#user_id == principal.id)")
	public ResponseEntity<?> getUserLocationOpening(@PathVariable Long user_id, @PathVariable Long location_id,
			@PathVariable Long id) {
		if (!locationService.findByIdAndCreatorId(location_id, user_id).isPresent()) {
			logger.error("Location id" + location_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		if (!openingService.findById(id).isPresent()) {
			logger.error("Opening id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		Opening opening = openingService.findById(id).get();

		Location location = locationService.findByIdAndCreatorId(location_id, user_id).get();

		if (location instanceof PaidLocation && ((PaidLocation) location).getOpenings().contains(opening)) {
			return ResponseEntity.ok(opening);
		} else {
			return ResponseEntity.badRequest().body("error!");
		}
	}

	@PostMapping("")
	@PreAuthorize("hasRole('RENTER') and (#user_id == principal.id)")
	public ResponseEntity<?> createUserLocationOpening(@PathVariable Long user_id, @PathVariable Long location_id,
			@RequestBody OpeningRequest openingRequest) {
		Opening newOpening = new Opening();

		BeanUtils.copyProperties(openingRequest, newOpening);

		if (!locationService.findByIdAndCreatorId(location_id, user_id).isPresent()
				&& locationService.findByIdAndCreatorId(location_id, user_id).get() instanceof PaidLocation) {
			logger.error("Location id" + location_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		PaidLocation location = (PaidLocation) locationService.findById(location_id).get();

		List<Opening> openings = location.getOpenings();

		for (Opening opening : openings) {
			if (opening.getWeekday().equals(newOpening.getWeekday())
					&& utilitiesService.isOverlapping(newOpening.getFromDate(), newOpening.getToDate(),
							opening.getFromDate(), opening.getToDate())
					&& utilitiesService.isOverlapping(newOpening.getStartTime(), newOpening.getEndTime(),
							opening.getStartTime(), opening.getEndTime())) {
				logger.error("New opening overlaps with old one");
				return ResponseEntity.badRequest().body("New opening overlaps with old one");
			}
		}
		location.getOpenings().add(newOpening);
		locationService.save(location);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('RENTER') and (#user_id == principal.id)")
	public ResponseEntity<?> deleteUserLocationOpening(@PathVariable Long user_id, @PathVariable Long location_id,
			@PathVariable Long id) {
		if (!locationService.findByIdAndCreatorId(location_id, user_id).isPresent()) {
			logger.error("Location id" + location_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		if (!openingService.findById(id).isPresent()) {
			logger.error("Opening id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		Opening opening = openingService.findById(id).get();

		Location location = locationService.findByIdAndCreatorId(location_id, user_id).get();

		if (location instanceof PaidLocation && ((PaidLocation) location).getOpenings().contains(opening)) {
			((PaidLocation) location).getOpenings().remove(opening);
			locationService.save(location);
			return ResponseEntity.ok("Removed");
		} else {
			return ResponseEntity.badRequest().body("error!");
		}
	}
}
