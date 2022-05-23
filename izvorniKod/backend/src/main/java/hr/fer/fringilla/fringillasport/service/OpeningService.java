package hr.fer.fringilla.fringillasport.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.Opening;
import hr.fer.fringilla.fringillasport.repository.LocationRepository;
import hr.fer.fringilla.fringillasport.repository.OpeningRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpeningService {
	private final OpeningRepository openingRepository;

	public List<Opening> findAll() {
		return openingRepository.findAll();
	}

	public Optional<Opening> findById(Long id) {
		return openingRepository.findById(id);
	}

	public Opening save(Opening opening) {
		return openingRepository.save(opening);
	}

	public void deleteById(Long id) {
		openingRepository.deleteById(id);
	}

}
