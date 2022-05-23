package hr.fer.fringilla.fringillasport.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.fer.fringilla.fringillasport.domain.Coach;
import hr.fer.fringilla.fringillasport.domain.Documentation;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;
import hr.fer.fringilla.fringillasport.domain.Sport;
import hr.fer.fringilla.fringillasport.domain.User;
import hr.fer.fringilla.fringillasport.repository.CoachRepository;
import hr.fer.fringilla.fringillasport.repository.SportRepository;
import hr.fer.fringilla.fringillasport.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	CoachRepository coachRepository;

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	public List<Coach> findByCoachCertificateContaining(Documentation documentation) {
		return coachRepository.findByCoachCertificateContaining(documentation);
	}
}
