package hr.fer.fringilla.fringillasport.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.Opening;
import hr.fer.fringilla.fringillasport.domain.SportEvent;
import hr.fer.fringilla.fringillasport.repository.OpeningRepository;
import hr.fer.fringilla.fringillasport.repository.SportEventRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SportEventService {
	private final SportEventRepository sportEventRepository;

	public List<SportEvent> findAll() {
		return sportEventRepository.findAll();
	}

	public List<SportEvent> findByLocationNotNull() {
		return sportEventRepository.findByLocationNotNull();
	}

	public List<SportEvent> findByLocation(Location location) {
		return sportEventRepository.findByLocation(location);
	}

	public Optional<SportEvent> findById(Long id) {
		return sportEventRepository.findById(id);
	}

	public SportEvent save(SportEvent sportEvent) {
		return sportEventRepository.save(sportEvent);
	}

	public void deleteById(Long id) {
		sportEventRepository.deleteById(id);
	}
}
