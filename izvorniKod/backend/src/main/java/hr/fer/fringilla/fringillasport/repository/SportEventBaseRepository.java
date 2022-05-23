package hr.fer.fringilla.fringillasport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import hr.fer.fringilla.fringillasport.domain.Location;
import hr.fer.fringilla.fringillasport.domain.SportEvent;

@NoRepositoryBean
public interface SportEventBaseRepository<T extends SportEvent> extends JpaRepository<T, Long> {

	List<T> findByLocationNotNull();

	List<T> findByLocation(Location location);
}
