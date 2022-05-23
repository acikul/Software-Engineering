package hr.fer.fringilla.fringillasport.repository;

import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;

import hr.fer.fringilla.fringillasport.domain.Person;

@NoRepositoryBean
public interface PersonBaseRepository<T extends Person> extends UserBaseRepository<T> {
	Optional<T> findByEmail(String email);

	Boolean existsByEmail(String email);
}
