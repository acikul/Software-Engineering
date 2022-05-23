package hr.fer.fringilla.fringillasport.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
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
import hr.fer.fringilla.fringillasport.domain.Documentation.DocumentationType;
import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;
import hr.fer.fringilla.fringillasport.domain.Renter;
import hr.fer.fringilla.fringillasport.domain.Sport;
import hr.fer.fringilla.fringillasport.payload.request.DocumentationRequest;
import hr.fer.fringilla.fringillasport.repository.DocumentationRepository;
import hr.fer.fringilla.fringillasport.service.DocumentationService;
import hr.fer.fringilla.fringillasport.service.FilesStorageService;
import hr.fer.fringilla.fringillasport.service.LocationService;
import hr.fer.fringilla.fringillasport.service.SportService;
import hr.fer.fringilla.fringillasport.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users/{user_id}/locations/{location_id}/documentations")
public class UserLocationsDocumentationsController {

	@Autowired
	DocumentationService documentationService;

	@Autowired
	UserService userService;

	@Autowired
	LocationService locationService;

	@Autowired
	FilesStorageService fileStorageService;

	@Autowired
	SportService sportService;

	private static final Logger logger = LoggerFactory.getLogger(UserLocationsDocumentationsController.class);

	@GetMapping("")
	@PreAuthorize("hasRole('RENTER') and (#user_id == principal.id)")
	public ResponseEntity<?> getUserLocationDocumentations(@PathVariable Long user_id, @PathVariable Long location_id) {
		if (!locationService.findByIdAndCreatorId(location_id, user_id).isPresent()) {
			logger.error("Location id" + location_id + "is not existed");
			ResponseEntity.badRequest().build();
		}

		Location location = locationService.findByIdAndCreatorId(location_id, user_id).get();

		if (location instanceof PaidLocation) {
			return ResponseEntity.ok(((PaidLocation) location).getOwnershipCertificate());
		} else {
			return ResponseEntity.badRequest().body("This location is not paid one!");
		}
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('RENTER') and (#user_id == principal.id)")
	public ResponseEntity<?> getUserLocationDocumentation(@PathVariable Long user_id, @PathVariable Long location_id,
			@PathVariable Long id) {
		if (!locationService.findByIdAndCreatorId(location_id, user_id).isPresent()) {
			logger.error("Location id" + location_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		if (!documentationService.findById(id).isPresent()) {
			logger.error("Documentation id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		Documentation documentation = documentationService.findById(id).get();

		Location location = locationService.findByIdAndCreatorId(location_id, user_id).get();

		if (location instanceof PaidLocation
				&& ((PaidLocation) location).getOwnershipCertificate().contains(documentation)) {
			return ResponseEntity.ok(documentation);
		} else {
			return ResponseEntity.badRequest().body("error!");
		}
	}

	@PostMapping("")
	@PreAuthorize("hasRole('RENTER') and (#user_id == principal.id)")
	public ResponseEntity<?> createUserLocationDocumentation(@PathVariable Long user_id, @PathVariable Long location_id,
			@RequestPart("documentation_info") DocumentationRequest documentationRequest,
			@RequestPart("file") MultipartFile file) {
		Documentation documentation = new Documentation();

		Optional<String> fileExtension = Optional.ofNullable(file.getOriginalFilename()).filter(f -> f.contains("."))
				.map(f -> f.substring(file.getOriginalFilename().lastIndexOf(".") + 1));

		String filename = UUID.randomUUID().toString() + "." + fileExtension.get();

		fileStorageService.save(file, filename);

		documentation.setDocumentInternUri(filename);
		documentation.setType(DocumentationType.OWNERSHIP);

		List<Sport> sports = new ArrayList<Sport>();

		for (Long sportId : documentationRequest.getSportsIds()) {
			if (sportService.findById(sportId).isPresent())
				sports.add(sportService.findById(sportId).get());
		}

		documentation.setSports(sports);

		if (!locationService.findByIdAndCreatorId(location_id, user_id).isPresent()
				&& locationService.findByIdAndCreatorId(location_id, user_id).get() instanceof PaidLocation) {
			logger.error("Location id" + location_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		PaidLocation location = (PaidLocation) locationService.findById(location_id).get();
		location.getOwnershipCertificate().add(documentation);
		locationService.save(location);

		return ResponseEntity.ok(documentation);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('RENTER') and (#user_id == principal.id)")
	public ResponseEntity<?> deleteUserLocationDocumentation(@PathVariable Long user_id, @PathVariable Long location_id,
			@PathVariable Long id) {
		if (!locationService.findByIdAndCreatorId(location_id, user_id).isPresent()) {
			logger.error("Location id" + location_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		if (!documentationService.findById(id).isPresent()) {
			logger.error("Documentation id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		Documentation documentation = documentationService.findById(id).get();

		Location location = locationService.findByIdAndCreatorId(location_id, user_id).get();

		if (location instanceof PaidLocation
				&& ((PaidLocation) location).getOwnershipCertificate().contains(documentation)) {
			((PaidLocation) location).getOwnershipCertificate().remove(documentation);

			locationService.save(location);
			return ResponseEntity.ok("Removed");
		} else {
			return ResponseEntity.badRequest().body("error!");
		}
	}
}
