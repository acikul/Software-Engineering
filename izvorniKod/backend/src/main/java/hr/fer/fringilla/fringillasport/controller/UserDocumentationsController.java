package hr.fer.fringilla.fringillasport.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hr.fer.fringilla.fringillasport.domain.Coach;
import hr.fer.fringilla.fringillasport.domain.Documentation;
import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;
import hr.fer.fringilla.fringillasport.domain.Renter;
import hr.fer.fringilla.fringillasport.domain.Sport;
import hr.fer.fringilla.fringillasport.domain.User;
import hr.fer.fringilla.fringillasport.domain.Documentation.DocumentationType;
import hr.fer.fringilla.fringillasport.payload.request.DocumentationRequest;
import hr.fer.fringilla.fringillasport.service.DocumentationService;
import hr.fer.fringilla.fringillasport.service.FilesStorageService;
import hr.fer.fringilla.fringillasport.service.LocationService;
import hr.fer.fringilla.fringillasport.service.SportService;
import hr.fer.fringilla.fringillasport.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users/{user_id}/documentations")
public class UserDocumentationsController {

	@Autowired
	DocumentationService documentationService;

	@Autowired
	UserService userService;

	@Autowired
	FilesStorageService fileStorageService;

	@Autowired
	SportService sportService;

	private static final Logger logger = LoggerFactory.getLogger(UserLocationsDocumentationsController.class);

	@GetMapping("")
	@PreAuthorize("hasRole('COACH') and (#user_id == principal.id)")
	public ResponseEntity<?> getUserDocumentations(@PathVariable Long user_id) {
		if (!userService.findById(user_id).isPresent()) {
			logger.error("User id" + user_id + "is not existed");
			ResponseEntity.badRequest().build();
		}

		User user = userService.findById(user_id).get();

		if (user instanceof Coach) {
			return ResponseEntity.ok(((Coach) user).getCoachCertificate());
		} else {
			return ResponseEntity.badRequest().body("Only Coach can have documentation!");
		}
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('COACH') and (#user_id == principal.id)")
	public ResponseEntity<?> getUserDocumentation(@PathVariable Long user_id, @PathVariable Long id) {

		if (!userService.findById(user_id).isPresent()) {
			logger.error("User id" + user_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		if (!documentationService.findById(id).isPresent()) {
			logger.error("Documentation id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		Documentation documentation = documentationService.findById(id).get();

		User user = userService.findById(user_id).get();

		if (user instanceof Coach && ((Coach) user).getCoachCertificate().contains(documentation)) {
			return ResponseEntity.ok(documentation);
		} else {
			return ResponseEntity.badRequest().body("error!");
		}
	}

	@PostMapping("")
	@PreAuthorize("hasRole('COACH') and (#user_id == principal.id)")
	public ResponseEntity<?> createUserDocumentation(@PathVariable Long user_id,
			@RequestPart("documentation_info") DocumentationRequest documentationRequest,
			@RequestPart("file") MultipartFile file) {
		Documentation documentation = new Documentation();

		Optional<String> fileExtension = Optional.ofNullable(file.getOriginalFilename()).filter(f -> f.contains("."))
				.map(f -> f.substring(file.getOriginalFilename().lastIndexOf(".") + 1));

		String filename = UUID.randomUUID().toString() + "." + fileExtension.get();

		fileStorageService.save(file, filename);

		documentation.setDocumentInternUri(filename);
		documentation.setType(DocumentationType.CERTIFICATE);

		List<Sport> sports = new ArrayList<Sport>();

		for (Long sportId : documentationRequest.getSportsIds()) {
			if (sportService.findById(sportId).isPresent())
				sports.add(sportService.findById(sportId).get());
		}

		documentation.setSports(sports);

		if (!userService.findById(user_id).isPresent() && userService.findById(user_id).get() instanceof Coach) {
			logger.error("Something is wrong!");
			ResponseEntity.badRequest().build();
		}
		Coach user = (Coach) userService.findById(user_id).get();
		user.getCoachCertificate().add(documentation);
		userService.save(user);

		return ResponseEntity.ok(documentation);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('COACH') and (#user_id == principal.id)")
	public ResponseEntity<?> deleteUserDocumentation(@PathVariable Long user_id, @PathVariable Long id) {
		if (!userService.findById(user_id).isPresent()) {
			logger.error("User id" + user_id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		if (!documentationService.findById(id).isPresent()) {
			logger.error("Documentation id" + id + "is not existed");
			ResponseEntity.badRequest().build();
		}
		Documentation documentation = documentationService.findById(id).get();

		User user = userService.findById(user_id).get();

		if (user instanceof Coach && ((Coach) user).getCoachCertificate().contains(documentation)) {

			((Coach) user).getCoachCertificate().remove(documentation);

			userService.save(user);
			return ResponseEntity.ok("removed!");
		} else {
			return ResponseEntity.badRequest().body("error!");
		}
	}
}