package hr.fer.fringilla.fringillasport.repository;

import javax.transaction.Transactional;

import hr.fer.fringilla.fringillasport.domain.Renter;

@Transactional
public interface RenterRepository extends PersonBaseRepository<Renter> {
}
