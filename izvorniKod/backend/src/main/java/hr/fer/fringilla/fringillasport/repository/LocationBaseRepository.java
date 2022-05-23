package hr.fer.fringilla.fringillasport.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import hr.fer.fringilla.fringillasport.domain.Location;

@NoRepositoryBean
public interface LocationBaseRepository<T extends Location> extends JpaRepository<T, Long> {
	List<T> findByCreatorId(Long id);

	Optional<T> findByIdAndCreatorId(Long id, Long creator);
}
