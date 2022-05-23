package hr.fer.fringilla.fringillasport.repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import hr.fer.fringilla.fringillasport.domain.Documentation;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;

@Transactional
public interface PaidLocationRepository extends LocationBaseRepository<PaidLocation> {
	List<PaidLocation> findByOwnershipCertificateContaining(Documentation documentation);
}
