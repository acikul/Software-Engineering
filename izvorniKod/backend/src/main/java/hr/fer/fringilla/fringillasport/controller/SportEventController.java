package hr.fer.fringilla.fringillasport.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.hibernate.hql.spi.id.local.LocalTemporaryTableBulkIdStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import hr.fer.fringilla.fringillasport.domain.SportEvent;
import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.Administrator;
import hr.fer.fringilla.fringillasport.domain.Athlete;
import hr.fer.fringilla.fringillasport.domain.Person;
import hr.fer.fringilla.fringillasport.domain.Renter;
import hr.fer.fringilla.fringillasport.domain.Sport;
import hr.fer.fringilla.fringillasport.domain.User;
import hr.fer.fringilla.fringillasport.payload.request.CalendarRequest;
import hr.fer.fringilla.fringillasport.payload.request.MapRequest;
import hr.fer.fringilla.fringillasport.repository.LocationRepository;
import hr.fer.fringilla.fringillasport.repository.SportEventRepository;

import hr.fer.fringilla.fringillasport.security.services.UserDetailsImpl;
import hr.fer.fringilla.fringillasport.service.SportEventService;
import hr.fer.fringilla.fringillasport.service.SportService;
import hr.fer.fringilla.fringillasport.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/sportevents")
public class SportEventController {

	private static final Logger logger = LoggerFactory.getLogger(SportEventController.class);

	@Autowired
	SportEventService sportEventService;

	@Autowired
	UserService userService;

	@PostMapping("/map")
	public ResponseEntity<?> getSportEventsByLocation(@RequestBody MapRequest mapRequest) {

		List<SportEvent> eventList = sportEventService.findByLocationNotNull();

		Double topRightLatReq = mapRequest.getTopRightLatitude();
		Double topRightLonReq = mapRequest.getTopRightLongitude();

		Double bottomLeftLatReq = mapRequest.getBottomLeftLatitude();
		Double bottomLeftLonReq = mapRequest.getBottomLeftLongitude();

		Double latDistance = topRightLatReq - bottomLeftLatReq;
		Double lonDistance = topRightLonReq - bottomLeftLonReq;

		List<SportEvent> retList = new ArrayList<>();

		for (SportEvent sportEvent : eventList) {
			Location locTemp = sportEvent.getLocation();
			// pretpostavka da je latituda pa longituda u bazi
			String[] arrCoords = locTemp.getGpxCoordinates().split(",");

			String latCoord = arrCoords[0];
			String lonCoord = arrCoords[1];

			if (lonCoord.contains("]]"))
				lonCoord = lonCoord.substring(0, lonCoord.length() - 1);

			Double latBaza = Double.parseDouble(latCoord.substring(2));
			Double lonBaza = Double.parseDouble(lonCoord.substring(0, lonCoord.length() - 1));

			if (((topRightLatReq - latBaza) < latDistance) && ((topRightLonReq - lonBaza) < lonDistance)) {
				retList.add(sportEvent);
			}
		}

		return ResponseEntity.ok(retList);
	}

	@GetMapping("/calendar")
	@PreAuthorize("hasRole('ATHLETE') or hasRole('COACH')")
	public ResponseEntity<?> getSportEventsByUser() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

		Athlete athlete = (Athlete) userService.findById(userDetails.getId()).get();

		return ResponseEntity.ok(athlete.getEventsParticipation());
	}

	@GetMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<List<SportEvent>> getSportEvents() {
		return ResponseEntity.ok(sportEventService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SportEvent> getSportEvent(@PathVariable Long id) {
		Optional<SportEvent> sportEvent = sportEventService.findById(id);
		if (!sportEvent.isPresent()) {
			logger.error("SportEvent id" + id + "is not existed");
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(sportEvent.get());
	}

	@PostMapping("")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> createSportEvent(@Valid @RequestBody SportEvent sportEvent) {
		return ResponseEntity.ok(sportEventService.save(sportEvent));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> updateSportEvent(@PathVariable Long id, @Valid @RequestBody SportEvent sportEvent) {
		if (!sportEventService.findById(id).isPresent()) {
			logger.error("SportEvent id" + id + "is not existed");
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(sportEventService.save(sportEvent));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRATOR')")
	public ResponseEntity<?> deleteSportEvent(@PathVariable Long id) {
		if (!sportEventService.findById(id).isPresent()) {
			logger.error("SportEvent id" + id + "is not existed");
			return ResponseEntity.badRequest().build();
		}

		sportEventService.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
