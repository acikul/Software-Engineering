package hr.fer.fringilla.fringillasport.controller;

import hr.fer.fringilla.fringillasport.domain.Administrator;
import hr.fer.fringilla.fringillasport.domain.Coach;
import hr.fer.fringilla.fringillasport.domain.Documentation;
import hr.fer.fringilla.fringillasport.domain.Person;
import hr.fer.fringilla.fringillasport.domain.User;
import hr.fer.fringilla.fringillasport.domain.Documentation.DocumentationType;
import hr.fer.fringilla.fringillasport.payload.request.DocumentationRequest;
import hr.fer.fringilla.fringillasport.payload.response.MessageResponse;
import hr.fer.fringilla.fringillasport.repository.DocumentationRepository;
import hr.fer.fringilla.fringillasport.security.services.UserDetailsImpl;
import hr.fer.fringilla.fringillasport.service.DocumentationService;
import hr.fer.fringilla.fringillasport.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/documentations")
public class DocumentationController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	DocumentationService documentationService;

	@Autowired
	UserService userService;

	@GetMapping("/ownership")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> getAllOwnerships() {
		return ResponseEntity.ok(documentationService.findByType(DocumentationType.OWNERSHIP));
	}

	@GetMapping("/certificate")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> getAllCertificates() {
		return ResponseEntity.ok((documentationService.findByType(DocumentationType.CERTIFICATE)));
	}

	@GetMapping("/unresolved")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> getAllUnresolvedDocumentations() {
		return ResponseEntity.ok(documentationService.findByApprovedByNull());
	}

	@GetMapping("/resolved")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> getAllDocumentationsApprovedByCurrentAdmin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

		if (!userService.findById(userDetails.getId()).isPresent()) {
			return ResponseEntity.badRequest().build();
		}

		User user = userService.findById(userDetails.getId()).get();
		return ResponseEntity.ok(documentationService.findByApprovedBy((Administrator) user));
	}

	@PostMapping("/approve/{documentation_id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> approveDocumentationById(@PathVariable Long documentation_id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

		if (!documentationService.findById(documentation_id).isPresent()) {
			return ResponseEntity.badRequest().build();
		}
		if (!userService.findById(userDetails.getId()).isPresent()) {
			return ResponseEntity.badRequest().build();
		}

		User user = userService.findById(userDetails.getId()).get();
		Documentation documentation = documentationService.findById(documentation_id).get();
		documentation.setApprovedBy((Administrator) user);

		return ResponseEntity.ok(documentationService.save(documentation));
	}

	@DeleteMapping("/{documentation_id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> deleteDocumentationById(@PathVariable Long documentation_id) {
		if (!documentationService.findById(documentation_id).isPresent()) {
			return ResponseEntity.badRequest().build();
		}
		documentationService.deleteById(documentation_id);
		return ResponseEntity.ok("deleted!");
	}
}
