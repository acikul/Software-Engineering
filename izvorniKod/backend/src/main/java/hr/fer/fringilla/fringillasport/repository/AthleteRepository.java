package hr.fer.fringilla.fringillasport.repository;

import javax.transaction.Transactional;

import hr.fer.fringilla.fringillasport.domain.Athlete;

@Transactional
public interface AthleteRepository extends AthleteBaseRepository<Athlete> {
}
