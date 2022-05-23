package hr.fer.fringilla.fringillasport.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import hr.fer.fringilla.fringillasport.domain.Sport;

@Transactional
public interface SportRepository extends JpaRepository<Sport, Long> {
}
