package hr.fer.fringilla.fringillasport.repository;

import java.util.List;

import hr.fer.fringilla.fringillasport.domain.Coach;
import hr.fer.fringilla.fringillasport.domain.Documentation;
import hr.fer.fringilla.fringillasport.domain.PaidLocation;
import hr.fer.fringilla.fringillasport.domain.SportEvent;

public interface CoachRepository extends AthleteBaseRepository<Coach> {
	List<Coach> findByCoachCertificateContaining(Documentation documentation);
}
