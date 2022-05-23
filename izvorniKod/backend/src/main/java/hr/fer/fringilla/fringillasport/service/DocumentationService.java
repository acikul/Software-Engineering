package hr.fer.fringilla.fringillasport.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.fringilla.fringillasport.domain.Administrator;
import hr.fer.fringilla.fringillasport.domain.Coach;
import hr.fer.fringilla.fringillasport.domain.Documentation;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;
import hr.fer.fringilla.fringillasport.domain.PaidSportEvent;
import hr.fer.fringilla.fringillasport.domain.Documentation.DocumentationType;
import hr.fer.fringilla.fringillasport.domain.Sport;
import hr.fer.fringilla.fringillasport.domain.SportEvent;
import hr.fer.fringilla.fringillasport.domain.User;
import hr.fer.fringilla.fringillasport.repository.CoachRepository;
import hr.fer.fringilla.fringillasport.repository.DocumentationRepository;
import hr.fer.fringilla.fringillasport.repository.SportRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentationService {
	@Autowired
	DocumentationRepository documentationRepository;

	@Autowired
	LocationService locationService;

	@Autowired
	SportEventService sportEventService;

	@Autowired
	UserService userService;

	public List<Documentation> findAll() {
		return documentationRepository.findAll();
	}

	public Optional<Documentation> findById(Long id) {
		return documentationRepository.findById(id);
	}

	public Documentation save(Documentation documentation) {
		return documentationRepository.save(documentation);
	}

	public List<Documentation> findByType(DocumentationType type) {
		return documentationRepository.findByType(type);
	}

	public List<Documentation> findByApprovedBy(Administrator administrator) {
		return documentationRepository.findByApprovedBy(administrator);
	}

	public List<Documentation> findByApprovedByNull() {
		return documentationRepository.findByApprovedByNull();
	}

	public void deleteById(Long id) {
		if (!documentationRepository.findById(id).isPresent())
			return;
		Documentation documentation = documentationRepository.findById(id).get();

		if (documentation.getType().equals(DocumentationType.CERTIFICATE)) {
			List<Coach> coachs = userService.findByCoachCertificateContaining(documentation);

			for (Coach coach : coachs) {
				coach.getCoachCertificate().remove(documentation);
				userService.save((User) coach);

				List<SportEvent> toRemove = new ArrayList<SportEvent>();

				for (SportEvent sportEvent : coach.getEventsOrganizer()) {
					if (sportEvent instanceof PaidSportEvent) {
						if (!coach.getCoachableSports().contains(sportEvent.getSport())) {
							toRemove.add(sportEvent);
						}
					}
				}

				coach.getEventsOrganizer().removeAll(toRemove);
				userService.save((User) coach);
			}

		}
		if (documentation.getType().equals(DocumentationType.OWNERSHIP)) {
			List<PaidLocation> locations = locationService.findByOwnershipCertificateContaining(documentation);

			for (PaidLocation location : locations) {
				location.getOwnershipCertificate().remove(documentation);
				locationService.save(location);

				List<SportEvent> sportEventsWithLocation = location.getHoldsSportEvent();

				for (SportEvent sportEvent : sportEventsWithLocation) {
					if (!location.getAvaliableSports().contains(sportEvent.getSport())) {
						sportEvent.setLocation(null);
						sportEventService.save(sportEvent);
					}
				}

				Set<SportEvent> sportEventsWithReservationInquery = location.getReservationQueue();

				for (SportEvent sportEvent : sportEventsWithReservationInquery) {
					if (!location.getAvaliableSports().contains(sportEvent.getSport())) {
						sportEvent.getLocationReservationInquery().remove(location);
						sportEventService.save(sportEvent);
					}
				}
			}
		}
	}
}
