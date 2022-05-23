package hr.fer.fringilla.fringillasport.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.fringilla.fringillasport.domain.Documentation;
import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.Opening;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;
import hr.fer.fringilla.fringillasport.domain.Sport;
import hr.fer.fringilla.fringillasport.domain.SportEvent;
import hr.fer.fringilla.fringillasport.repository.LocationRepository;
import hr.fer.fringilla.fringillasport.repository.PaidLocationRepository;
import hr.fer.fringilla.fringillasport.repository.SportRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {
	@Autowired
	LocationRepository locationRepository;

	@Autowired
	PaidLocationRepository paidLocationRepository;

	@Autowired
	UtilitiesService utilitiesService;

	@Autowired
	SportEventService sportEventService;

	public List<Location> findAll() {
		return locationRepository.findAll();
	}

	public Optional<Location> findById(Long id) {
		return locationRepository.findById(id);
	}

	public Location save(Location location) {
		return locationRepository.save(location);
	}

	public void deleteById(Long id) {
		locationRepository.deleteById(id);
	}

	public List<Location> findAllCreatorId(Long user_id) {
		return locationRepository.findByCreatorId(user_id);
	}

	// public List<Location> findAllCreatorIdAndType(Long user_id, String type) {
	// switch location_type:
	// case "free":
	// return
	// ResponseEntity.ok(locationService.findAllCreatorId(user_id).parallelStream().filter(sc
	// -> sc instanceof PaidLocation).collect(Collectors.toList()));
	// }

	public Optional<Location> findByIdAndCreatorId(Long id, Long user_id) {
		return locationRepository.findByIdAndCreatorId(id, user_id);
	}

	public List<Location> getAvailableLocations(SportEvent mainSportEvent) {
		List<Location> allLocations = locationRepository.findAll();

		List<SportEvent> allSportEvents = sportEventService.findByLocationNotNull();

		for (SportEvent sportEvent : allSportEvents) {
			if (!mainSportEvent.equals(sportEvent) && utilitiesService.isOverlapping(mainSportEvent.getStartDateTime(),
					mainSportEvent.getEndDateTime(), sportEvent.getStartDateTime(), sportEvent.getEndDateTime())) {
				allLocations.remove(sportEvent.getLocation());
			}
		}

		List<Location> result = allLocations.stream().filter(location -> {
			if (!(location.getAvaliableSports().contains(mainSportEvent.getSport()))) {
				return false;
			}
			if (location instanceof PaidLocation) {
				List<Opening> openings = ((PaidLocation) location).getOpenings();
				for (Opening opening : openings) {
					if (opening.getWeekday().equals(mainSportEvent.getStartDateTime().getDayOfWeek())
							&& utilitiesService.isOverlapping(mainSportEvent.getStartDateTime().toLocalDate(),
									mainSportEvent.getEndDateTime().toLocalDate(), opening.getFromDate(),
									opening.getToDate())
							&& utilitiesService.isOverlapping(mainSportEvent.getStartDateTime().toLocalTime(),
									mainSportEvent.getEndDateTime().toLocalTime(), opening.getStartTime(),
									opening.getEndTime())) {
						return true;
					}
				}
			} else {
				return true;
			}
			return false;
		}).collect(Collectors.toList());

		return result;
	}

	public Boolean checkIfLocationIsFree(Location location, SportEvent mainSportEvent) {
		List<SportEvent> sportEventsForThatLocation = sportEventService.findByLocation(location);

		for (SportEvent sportEvent : sportEventsForThatLocation) {
			if (!mainSportEvent.equals(sportEvent) && utilitiesService.isOverlapping(mainSportEvent.getStartDateTime(),
					mainSportEvent.getEndDateTime(), sportEvent.getStartDateTime(), sportEvent.getEndDateTime())) {
				return false;
			}
		}

		if (!(location.getAvaliableSports().contains(mainSportEvent.getSport()))) {
			return false;
		}
		if (location instanceof PaidLocation) {
			List<Opening> openings = ((PaidLocation) location).getOpenings();
			for (Opening opening : openings) {
				if (opening.getWeekday().equals(mainSportEvent.getStartDateTime().getDayOfWeek())
						&& utilitiesService.isOverlapping(mainSportEvent.getStartDateTime().toLocalDate(),
								mainSportEvent.getEndDateTime().toLocalDate(), opening.getFromDate(),
								opening.getToDate())
						&& utilitiesService.isOverlapping(mainSportEvent.getStartDateTime().toLocalTime(),
								mainSportEvent.getEndDateTime().toLocalTime(), opening.getStartTime(),
								opening.getEndTime())) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}

	public List<PaidLocation> findByOwnershipCertificateContaining(Documentation documentation) {
		return paidLocationRepository.findByOwnershipCertificateContaining(documentation);
	}
}
