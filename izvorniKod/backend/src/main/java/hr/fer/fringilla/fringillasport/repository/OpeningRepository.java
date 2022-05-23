package hr.fer.fringilla.fringillasport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import hr.fer.fringilla.fringillasport.domain.Opening;

@Transactional
public interface OpeningRepository extends JpaRepository<Opening, Long> {

}
