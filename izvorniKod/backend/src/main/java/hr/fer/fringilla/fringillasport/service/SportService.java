package hr.fer.fringilla.fringillasport.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import hr.fer.fringilla.fringillasport.domain.Sport;
import hr.fer.fringilla.fringillasport.repository.SportRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SportService {
	private final SportRepository sportRepository;

	public List<Sport> findAll() {
		return sportRepository.findAll();
	}

	public Optional<Sport> findById(Long id) {
		return sportRepository.findById(id);
	}

	public Sport save(Sport sport) {
		return sportRepository.save(sport);
	}

	public void deleteById(Long id) {
		sportRepository.deleteById(id);
	}
}
