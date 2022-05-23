package hr.fer.fringilla.fringillasport.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.fer.fringilla.fringillasport.domain.Administrator;
import hr.fer.fringilla.fringillasport.domain.Documentation;
import hr.fer.fringilla.fringillasport.domain.Documentation.DocumentationType;

public interface DocumentationRepository extends JpaRepository<Documentation, Long> {
	List<Documentation> findByType(DocumentationType type);

	Optional<Documentation> findByIdAndType(Long id, DocumentationType type);

	List<Documentation> findByApprovedBy(Administrator approvedBy);

	List<Documentation> findByApprovedByNull();
}
