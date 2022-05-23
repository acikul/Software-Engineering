package hr.fer.fringilla.fringillasport.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import hr.fer.fringilla.fringillasport.domain.User;

@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends JpaRepository<T, Long> {
	Optional<T> findByUsername(String username);

	Boolean existsByUsername(String username);
}
